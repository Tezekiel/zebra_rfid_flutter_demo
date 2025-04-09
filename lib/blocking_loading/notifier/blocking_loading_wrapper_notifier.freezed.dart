// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'blocking_loading_wrapper_notifier.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$BlockingLoadingState {
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() noLoading,
    required TResult Function(String? message) loading,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? noLoading,
    TResult? Function(String? message)? loading,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? noLoading,
    TResult Function(String? message)? loading,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_NoLoading value) noLoading,
    required TResult Function(_Loading value) loading,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_NoLoading value)? noLoading,
    TResult? Function(_Loading value)? loading,
  }) =>
      throw _privateConstructorUsedError;
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_NoLoading value)? noLoading,
    TResult Function(_Loading value)? loading,
    required TResult orElse(),
  }) =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $BlockingLoadingStateCopyWith<$Res> {
  factory $BlockingLoadingStateCopyWith(BlockingLoadingState value,
          $Res Function(BlockingLoadingState) then) =
      _$BlockingLoadingStateCopyWithImpl<$Res, BlockingLoadingState>;
}

/// @nodoc
class _$BlockingLoadingStateCopyWithImpl<$Res,
        $Val extends BlockingLoadingState>
    implements $BlockingLoadingStateCopyWith<$Res> {
  _$BlockingLoadingStateCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of BlockingLoadingState
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc
abstract class _$$NoLoadingImplCopyWith<$Res> {
  factory _$$NoLoadingImplCopyWith(
          _$NoLoadingImpl value, $Res Function(_$NoLoadingImpl) then) =
      __$$NoLoadingImplCopyWithImpl<$Res>;
}

/// @nodoc
class __$$NoLoadingImplCopyWithImpl<$Res>
    extends _$BlockingLoadingStateCopyWithImpl<$Res, _$NoLoadingImpl>
    implements _$$NoLoadingImplCopyWith<$Res> {
  __$$NoLoadingImplCopyWithImpl(
      _$NoLoadingImpl _value, $Res Function(_$NoLoadingImpl) _then)
      : super(_value, _then);

  /// Create a copy of BlockingLoadingState
  /// with the given fields replaced by the non-null parameter values.
}

/// @nodoc

class _$NoLoadingImpl implements _NoLoading {
  const _$NoLoadingImpl();

  @override
  String toString() {
    return 'BlockingLoadingState.noLoading()';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType && other is _$NoLoadingImpl);
  }

  @override
  int get hashCode => runtimeType.hashCode;

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() noLoading,
    required TResult Function(String? message) loading,
  }) {
    return noLoading();
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? noLoading,
    TResult? Function(String? message)? loading,
  }) {
    return noLoading?.call();
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? noLoading,
    TResult Function(String? message)? loading,
    required TResult orElse(),
  }) {
    if (noLoading != null) {
      return noLoading();
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_NoLoading value) noLoading,
    required TResult Function(_Loading value) loading,
  }) {
    return noLoading(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_NoLoading value)? noLoading,
    TResult? Function(_Loading value)? loading,
  }) {
    return noLoading?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_NoLoading value)? noLoading,
    TResult Function(_Loading value)? loading,
    required TResult orElse(),
  }) {
    if (noLoading != null) {
      return noLoading(this);
    }
    return orElse();
  }
}

abstract class _NoLoading implements BlockingLoadingState {
  const factory _NoLoading() = _$NoLoadingImpl;
}

/// @nodoc
abstract class _$$LoadingImplCopyWith<$Res> {
  factory _$$LoadingImplCopyWith(
          _$LoadingImpl value, $Res Function(_$LoadingImpl) then) =
      __$$LoadingImplCopyWithImpl<$Res>;
  @useResult
  $Res call({String? message});
}

/// @nodoc
class __$$LoadingImplCopyWithImpl<$Res>
    extends _$BlockingLoadingStateCopyWithImpl<$Res, _$LoadingImpl>
    implements _$$LoadingImplCopyWith<$Res> {
  __$$LoadingImplCopyWithImpl(
      _$LoadingImpl _value, $Res Function(_$LoadingImpl) _then)
      : super(_value, _then);

  /// Create a copy of BlockingLoadingState
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? message = freezed,
  }) {
    return _then(_$LoadingImpl(
      message: freezed == message
          ? _value.message
          : message // ignore: cast_nullable_to_non_nullable
              as String?,
    ));
  }
}

/// @nodoc

class _$LoadingImpl implements _Loading {
  const _$LoadingImpl({this.message});

  @override
  final String? message;

  @override
  String toString() {
    return 'BlockingLoadingState.loading(message: $message)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$LoadingImpl &&
            (identical(other.message, message) || other.message == message));
  }

  @override
  int get hashCode => Object.hash(runtimeType, message);

  /// Create a copy of BlockingLoadingState
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$LoadingImplCopyWith<_$LoadingImpl> get copyWith =>
      __$$LoadingImplCopyWithImpl<_$LoadingImpl>(this, _$identity);

  @override
  @optionalTypeArgs
  TResult when<TResult extends Object?>({
    required TResult Function() noLoading,
    required TResult Function(String? message) loading,
  }) {
    return loading(message);
  }

  @override
  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>({
    TResult? Function()? noLoading,
    TResult? Function(String? message)? loading,
  }) {
    return loading?.call(message);
  }

  @override
  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>({
    TResult Function()? noLoading,
    TResult Function(String? message)? loading,
    required TResult orElse(),
  }) {
    if (loading != null) {
      return loading(message);
    }
    return orElse();
  }

  @override
  @optionalTypeArgs
  TResult map<TResult extends Object?>({
    required TResult Function(_NoLoading value) noLoading,
    required TResult Function(_Loading value) loading,
  }) {
    return loading(this);
  }

  @override
  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>({
    TResult? Function(_NoLoading value)? noLoading,
    TResult? Function(_Loading value)? loading,
  }) {
    return loading?.call(this);
  }

  @override
  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>({
    TResult Function(_NoLoading value)? noLoading,
    TResult Function(_Loading value)? loading,
    required TResult orElse(),
  }) {
    if (loading != null) {
      return loading(this);
    }
    return orElse();
  }
}

abstract class _Loading implements BlockingLoadingState {
  const factory _Loading({final String? message}) = _$LoadingImpl;

  String? get message;

  /// Create a copy of BlockingLoadingState
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$LoadingImplCopyWith<_$LoadingImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
