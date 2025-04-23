import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:zebra_rfid_flutter_demo/zebra_lib/models/reader_device.dart';
import 'package:zebra_rfid_flutter_demo/zebra_rfid_sdk_plugin_notifier/zebra_rfid_sdk_plugin_notifier.dart';

import 'blocking_loading/notifier/blocking_loading_wrapper_notifier.dart';

class HomeScreen extends HookConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    ref.watch(zebraRfidSdkPluginNotifierProvider);
    _setUpListeners(ref, context);

    ref.read(zebraRfidSdkPluginNotifierProvider.notifier).startReader();

    return CupertinoPageScaffold(
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Theme.of(context).colorScheme.inversePrimary,
          title: Text("Zebra RFID demo"),
        ),
        body: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              MaterialButton(
                color: Theme.of(context).colorScheme.primary,
                child: Text(
                  "Connect to Zebra",
                  style: TextStyle(color: Theme.of(context).colorScheme.onPrimary, fontSize: 14),
                ),
                onPressed: () {
                  ref.read(zebraRfidSdkPluginNotifierProvider.notifier).connectToZebra(ReaderDevice.initial());
                },
              ),
              MaterialButton(
                color: Theme.of(context).colorScheme.primary,
                child: Text(
                  "Disconnect",
                  style: TextStyle(color: Theme.of(context).colorScheme.onPrimary, fontSize: 14),
                ),
                onPressed: () {
                  ref.read(zebraRfidSdkPluginNotifierProvider.notifier).disconnectFromZebra();
                },
              ),
              MaterialButton(
                color: Theme.of(context).colorScheme.primary,
                child: Text(
                  "Stop/start Scan",
                  style: TextStyle(color: Theme.of(context).colorScheme.onPrimary, fontSize: 14),
                ),
                onPressed: () {
                  ref.read(zebraRfidSdkPluginNotifierProvider.notifier).triggerInventoryRead();
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _setUpListeners(WidgetRef ref, BuildContext context) {
    ref.listen(zebraConnectionActionProvider, (_, reader) async {
      switch (reader?.connectionStatus) {
        case ConnectionStatus.connected:
          ref.read(blockingLoadingWrapperNotifierProvider.notifier).hideLoading();
          ref.read(zebraRfidSdkPluginNotifierProvider.notifier).setConnectedReaderDevice(reader!);
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Connected to ${reader.name ?? 'RFID Reader'}'),
            ),
          );
        case ConnectionStatus.connecting:
          ref
              .read(blockingLoadingWrapperNotifierProvider.notifier)
              .showLoading(message: 'Connecting to ${reader?.name ?? 'RFID Reader'}');
        case ConnectionStatus.notConnected:
          ref.read(blockingLoadingWrapperNotifierProvider.notifier).hideLoading();
        case ConnectionStatus.failed:
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Connection failed. Please try again. ${reader?.name}'),
            ),
          );
          break;
        case ConnectionStatus.disconnected:
          print("Disconnected from ${reader?.name}");
        case null:

          /// Should not get here
          throw UnimplementedError();
      }
    });
  }
}
