/* 

              Copyright (c) 2021 divshekhar (Divyanshu Shekhar). 
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'dart:async';

import 'package:flutter_shortcuts/src/platform/flutter_shortcuts_platform.dart';
import 'package:flutter_shortcuts/src/helper/helper.dart';

export 'package:flutter_shortcuts/src/helper/helper.dart';

class FlutterShortcuts {
  /// [initialize()] performs action when shortcuts is opened.
  Future<void> initialize(FlutterShortcutAction action) async {
    FlutterShortcutsPlatform.instance.initialize(action);
  }

  /// [setShortcutItems()] will set all the shortcut items.
  Future<void> setShortcutItems(
      {List<FlutterShortcutItem> shortcutItems}) async {
    return FlutterShortcutsPlatform.instance.setShortcutItems(shortcutItems);
  }

  /// [clearShortcutItems()] will remove all the shortcut items.
  Future<void> clearShortcutItems() async {
    return FlutterShortcutsPlatform.instance.clearShortcutItems();
  }

  /// [pushShortcutItem()] will push a new shortcut item.
  /// If there is already a dynamic or pinned shortcut with the same ID,
  /// the shortcut will be updated.
  Future<void> pushShortcutItem({FlutterShortcutItem shortcut}) async {
    return FlutterShortcutsPlatform.instance.pushShortcutItem(shortcut);
  }

  /// [updateShortcutItems()] will update all the shortcut items.
  Future<void> updateAllShortcutItems({
    List<FlutterShortcutItem> shortcutList,
  }) async {
    return FlutterShortcutsPlatform.instance
        .updateAllShortcutItems(shortcutList);
  }

  /// [updateShortcutItem()] will update a single shortcut item based on id.
  Future<void> updateShortcutItem({
    String id,
    FlutterShortcutItem shortcut,
  }) async {
    return FlutterShortcutsPlatform.instance.updateShortcutItem(id, shortcut);
  }

  /// [changeShortcutItemIcon()] will change the icon of the shortcut based on id.
  Future<void> changeShortcutItemIcon({String id, String icon}) async {
    return FlutterShortcutsPlatform.instance.changeShortcutItemIcon(id, icon);
  }
}
