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
  /// [initialize] performs action when shortcut is initiated.
  Future<void> initialize(FlutterShortcutAction action) async {
    FlutterShortcutsPlatform.instance.initialize(action);
  }

  /// [getMaxShortcutLimit] returns the maximum number of static or dynamic
  /// shortcuts that each launcher icon can have at a time.
  Future<int> getMaxShortcutLimit() {
    return FlutterShortcutsPlatform.instance.getMaxShortcutLimit();
  }

  /// [setShortcutItems] will set all the shortcut items.
  Future<void> setShortcutItems(
      {List<FlutterShortcutItem> shortcutItems}) async {
    return FlutterShortcutsPlatform.instance.setShortcutItems(shortcutItems);
  }

  /// [clearShortcutItems] will remove all the shortcut items.
  Future<void> clearShortcutItems() async {
    return FlutterShortcutsPlatform.instance.clearShortcutItems();
  }

  /// [pushShortcutItem] will push a new shortcut item.
  /// If there is already a dynamic or pinned shortcut with the same ID,
  /// the shortcut will be updated and pushed at the end of the shortcut list.
  Future<void> pushShortcutItem({FlutterShortcutItem shortcut}) async {
    return FlutterShortcutsPlatform.instance.pushShortcutItem(shortcut);
  }

  /// [addShortcutItems] updates dynamic or pinned shortcuts with same IDs
  /// and pushes new shortcuts with different IDs.
  Future<void> addShortcutItems(
      {List<FlutterShortcutItem> shortcutList}) async {
    return FlutterShortcutsPlatform.instance.addShortcutItems(shortcutList);
  }

  /// [updateAllShortcutItems] updates all the shortcut items.
  /// If the IDs of the shortcuts are not same, no changes will be reflected.
  Future<void> updateAllShortcutItems(
      {List<FlutterShortcutItem> shortcutList}) async {
    return FlutterShortcutsPlatform.instance
        .updateAllShortcutItems(shortcutList);
  }

  /// [updateShortcutItem] updates a single shortcut item based on id.
  /// If the ID of the shortcut is not same, no changes will be reflected.
  Future<void> updateShortcutItem(
      {String id, FlutterShortcutItem shortcut}) async {
    return FlutterShortcutsPlatform.instance.updateShortcutItem(id, shortcut);
  }

  /// [changeShortcutItemIcon] will change the icon of the shortcut based on id.
  /// If the ID of the shortcut is not same, no changes will be reflected.
  Future<void> changeShortcutItemIcon({String id, String icon}) async {
    return FlutterShortcutsPlatform.instance.changeShortcutItemIcon(id, icon);
  }
}
