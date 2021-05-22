import 'dart:async';

import 'package:flutter_shortcuts/src/platform/flutter_shortcuts_platform.dart';
import 'package:flutter_shortcuts/src/types/types.dart';

export 'package:flutter_shortcuts/src/types/types.dart';

class FlutterShortcuts {
  Future<void> initialize(FlutterShortcutAction action) async {
    FlutterShortcutsPlatform.instance.initialize(action);
  }

  Future<void> setShortcutItems(List<FlutterShortcutItem> items) async {
    return FlutterShortcutsPlatform.instance.setShortcutItems(items);
  }
}
