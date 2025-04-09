import 'package:flutter_riverpod/flutter_riverpod.dart';

class ActionNotifier<T> extends AutoDisposeNotifier<T?> {
  @override
  T? build() {
    return null;
  }

  void emit(T action) {
    state = action;
  }

  T? get action => state;

  @override
  bool updateShouldNotify(T? previous, T? next) {
    return true;
  }
}

abstract class ActionProvider {
  ActionProvider._();

  static AutoDisposeNotifierProvider<ActionNotifier<T>, T?> autoDispose<T>() =>
      NotifierProvider.autoDispose<ActionNotifier<T>, T?>(() => ActionNotifier<T>());
}
