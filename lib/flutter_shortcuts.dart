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

  Future<void> clearShortcutItems() async {
    return FlutterShortcutsPlatform.instance.clearShortcutItems();
  }

  /// updateShortcutItems() will update all the shortcut items.
  Future<void> updateAllShortcutItems({
    List<FlutterShortcutItem> shortcutList,
  }) async {
    return FlutterShortcutsPlatform.instance
        .updateAllShortcutItems(shortcutList);
  }

  /// updateShortcutItems() will update a single shortcut item based on id.
  Future<void> updateShortcutItem({
    String id,
    FlutterShortcutItem shortcut,
  }) async {
    return FlutterShortcutsPlatform.instance.updateShortcutItem(id, shortcut);
  }
}
