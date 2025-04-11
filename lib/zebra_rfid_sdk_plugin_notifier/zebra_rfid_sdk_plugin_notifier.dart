import 'dart:convert';

import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:loggy/loggy.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';
import 'package:zebra_rfid_flutter_demo/zebra_rfid_sdk_plugin_notifier/action_notifier.dart';
import 'package:zebra_rfid_reader_sdk/zebra_rfid_reader_sdk.dart';

part 'zebra_rfid_sdk_plugin_notifier.freezed.dart';

part 'zebra_rfid_sdk_plugin_notifier.g.dart';

final zebraConnectionActionProvider = ActionProvider.autoDispose<ReaderDevice>();

@riverpod
class ZebraRfidSdkPluginNotifier extends _$ZebraRfidSdkPluginNotifier {
  @override
  ZebraRfidSdkPluginState build() {
    final zebraRfidReaderSdkPlugin = ZebraRfidReaderSdk();
    final connectedReader = ReaderDevice.initial();

    ref.onDispose(() {
      disconnectFromZebra();
      ref.invalidate(zebraRfidSdkPluginNotifierProvider);
    });

    return ZebraRfidSdkPluginState(
      zebraRfidReaderSdkPlugin: zebraRfidReaderSdkPlugin,
      connectedReader: connectedReader,
      softInventoryReadInProgress: false,
      lastConnectionStatus: ConnectionStatus.notConnected,
    );
  }

  Future<void> checkPermissionsApproved() async {
    final bluetoothScan = await Permission.bluetoothScan
        .request()
        .isGranted;
    final bluetoothConnect = await Permission.bluetoothConnect
        .request()
        .isGranted;
    final location = await Permission.location
        .request()
        .isGranted;

    if (!bluetoothScan || !bluetoothConnect || !location) return;

    _setInitialReader();
  }

  Future<void> _setInitialReader() async {
    _listenToConnection();
    final readers = await state.zebraRfidReaderSdkPlugin.getAvailableReaderList();
    if (readers.isNotEmpty) {
      final reader = readers.first;
      logInfo("Found reader: ${reader.name}");
      connectToZebra(reader);
    } else {
      logInfo("ERROR: No readers available");
    }
  }

  void connectToZebra(ReaderDevice reader) {
    logInfo("Connecting to reader: ${reader.name}");
    final name = 'RFIDEM45';
    state.zebraRfidReaderSdkPlugin.connect(name);
  }

  void disconnectFromZebra() {
    state.zebraRfidReaderSdkPlugin.disconnect();
    state = state.copyWith(connectedReader: ReaderDevice.initial());
  }

  void _listenToConnection() {
    state.zebraRfidReaderSdkPlugin.connectedReaderDevice.listen((event) {
      final result = jsonDecode(event.toString());
      final readerDevice = ReaderDevice.fromJson(result);
      if (readerDevice.connectionStatus != state.lastConnectionStatus) {
        ref.read(zebraConnectionActionProvider.notifier).emit(readerDevice);
        setLastConnectionStatus(readerDevice.connectionStatus);
      }
    });
  }

  void triggerInventoryRead() {
    if (state.softInventoryReadInProgress) {
      state = state.copyWith(softInventoryReadInProgress: false);
      state.zebraRfidReaderSdkPlugin.stopInventory();
    } else {
      state = state.copyWith(softInventoryReadInProgress: true);
      state.zebraRfidReaderSdkPlugin.startInventory();
    }
  }

  void setLastConnectionStatus(ConnectionStatus connectionStatus) {
    state = state.copyWith(lastConnectionStatus: connectionStatus);
  }

  void setConnectedReaderDevice(ReaderDevice readerDevice) {
    state = state.copyWith(connectedReader: readerDevice);
    _listenToReadTags();
  }

  void _listenToReadTags() {
    state.zebraRfidReaderSdkPlugin.readTags.listen((event) {
      decodeTagEvent(event);
    });
  }

  void decodeTagEvent(dynamic tagEvent) {
    final result = jsonDecode(tagEvent.toString());
    final hex = TagDataModel
        .fromJson(result)
        .tagId;

    /// The following harcdoded strings to be replaced by server rules
    const prefixes = ['4D4D', '464C'];
    if (prefixes.any((prefix) => hex.startsWith(prefix))) {
      final tagNumber = hex.hexToAscii();
      print("tagNumber is: $tagNumber");
    }
  }
}

@freezed
class ZebraRfidSdkPluginState with _$ZebraRfidSdkPluginState {
  const ZebraRfidSdkPluginState._();

  const factory ZebraRfidSdkPluginState({
    required ZebraRfidReaderSdk zebraRfidReaderSdkPlugin,
    required ReaderDevice connectedReader,
    required bool softInventoryReadInProgress,
    required ConnectionStatus lastConnectionStatus,
  }) = _ZebraRfidSdkPluginState;
}

extension StringX on String {
  String hexToAscii() {
    if (length % 2 != 0) {
      throw ArgumentError('Hex string must have an even length');
    }
    return String.fromCharCodes(
        List.generate(length ~/ 2, (i) => int.parse(substring(i * 2, i * 2 + 2), radix: 16))
    );
  }
}
