import 'package:flutter_shortcuts/src/method_call/flutter_shortcuts_method_channel.dart';
import 'package:flutter_shortcuts/src/types/types.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class FlutterShortcutsPlatform extends PlatformInterface {
  FlutterShortcutsPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterShortcutsPlatform _instance = FlutterShortcutsMethodChannel();

  /// returns the instance of the [FlutterShortcutsMethodChannel].
  static FlutterShortcutsPlatform get instance => _instance;

  static set instance(FlutterShortcutsPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> initialize(FlutterShortcutsHandler handler) async {
    throw UnimplementedError("initialize() has not been implemented.");
  }
}
