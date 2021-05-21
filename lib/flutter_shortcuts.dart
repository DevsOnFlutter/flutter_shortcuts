import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_shortcuts/src/platform/flutter_shortcuts_platform.dart';
import 'package:flutter_shortcuts/src/types/types.dart';

class FlutterShortcuts {
  static const MethodChannel _channel =
      const MethodChannel('flutter_shortcuts');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<void> initialize(FlutterShortcutsHandler handler) async {
    FlutterShortcutsPlatform.instance.initialize(handler);
  }
}
