import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part 'blocking_loading_wrapper_notifier.freezed.dart';
part 'blocking_loading_wrapper_notifier.g.dart';

@riverpod
class BlockingLoadingWrapperNotifier extends _$BlockingLoadingWrapperNotifier {
  @override
  BlockingLoadingState build() {
    return const BlockingLoadingState.noLoading();
  }

  void showLoading({String? message}) {
    state = BlockingLoadingState.loading(message: message);
  }

  void hideLoading() {
    state = const BlockingLoadingState.noLoading();
  }
}

@Freezed()
class BlockingLoadingState with _$BlockingLoadingState {
  const factory BlockingLoadingState.noLoading() = _NoLoading;

  const factory BlockingLoadingState.loading({String? message}) = _Loading;
}
