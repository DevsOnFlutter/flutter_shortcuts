import 'package:flutter_shortcuts/src/method_call/flutter_shortcuts_method_call_handler.dart';
import 'package:flutter_shortcuts/src/types/types.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class FlutterShortcutsPlatform extends PlatformInterface {
  FlutterShortcutsPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterShortcutsPlatform _instance =
      FlutterShortcutsMethodCallHandler();

  /// returns the instance of the [FlutterShortcutsMethodChannel].
  static FlutterShortcutsPlatform get instance => _instance;

  static set instance(FlutterShortcutsPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> initialize(FlutterShortcutAction action) async {
    throw UnimplementedError("initialize() has not been implemented.");
  }

  Future<void> setShortcutItems(List<FlutterShortcutItem> items) async {
    throw UnimplementedError("setShortcutItems() has not been implemented.");
  }

  Future<void> updateShortcutItems(List<FlutterShortcutItem> items) async {
    throw UnimplementedError("updateShortcutItems() has not been implemented.");
  }
}
