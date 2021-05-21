
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterShortcuts {
  static const MethodChannel _channel =
      const MethodChannel('flutter_shortcuts');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
