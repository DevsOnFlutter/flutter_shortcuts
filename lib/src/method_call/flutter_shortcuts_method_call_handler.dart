/* 

            Copyright (c) 2021 divshekhar (Divyanshu Shekhar).
                          All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'package:flutter/services.dart';
import 'package:flutter_shortcuts/src/platform/flutter_shortcuts_platform.dart';
import 'package:flutter_shortcuts/src/helper/helper.dart';
import 'package:flutter/foundation.dart';

class FlutterShortcutsMethodCallHandler extends FlutterShortcutsPlatform {
  final MethodChannel _channel =
      MethodChannel('com.divyanshushekhar.flutter_shortcuts');

  MethodChannel get channel => _channel;

  @override
  Future<void> initialize(bool debug) async {
    Map<String, String> initValue = {
      'debug': kReleaseMode ? false.toString() : debug.toString(),
    };
    await channel.invokeMethod('initialize', [initValue]);
  }

  @override
  Future<void> listenAction(ShortcutAction actionHandler) async {
    channel.setMethodCallHandler((MethodCall call) async {
      assert(call.method == 'launch');
      actionHandler(call.arguments);
    });
    final String? action =
        await channel.invokeMethod<String>('getLaunchAction');
    if (action != null) {
      actionHandler(action);
    }
  }

  @override
  Future<int?> getMaxShortcutLimit() {
    return channel.invokeMethod<int>('getMaxShortcutLimit');
  }

  @override
  Future<Map<String, int>> getIconProperties() async {
    return Map.castFrom<dynamic, dynamic, String, int>(
        await channel.invokeMethod('getIconProperties'));
  }

  @override
  Future<void> setShortcutItems(List<ShortcutItem> items) async {
    final List<Map<String, dynamic>> itemsList =
        items.map((item) => item.serialize()).toList();
    await channel.invokeMethod<void>('setShortcutItems', itemsList);
  }

  @override
  Future<void> clearShortcutItems() async {
    await channel.invokeMethod<void>('clearShortcutItems');
  }

  @override
  Future<void> pushShortcutItem(ShortcutItem shortcut) async {
    final Map<String, dynamic> item = shortcut.serialize();
    await channel.invokeMethod<void>('pushShortcutItem', [item]);
  }

  @override
  Future<void> pushShortcutItems(List<ShortcutItem> items) async {
    final List<Map<String, dynamic>> itemsList =
        items.map((item) => item.serialize()).toList();
    await channel.invokeMethod<void>('pushShortcutItems', itemsList);
  }

  @override
  Future<void> updateShortcutItems(List<ShortcutItem> items) async {
    final List<Map<String, dynamic>> itemsList =
        items.map((item) => item.serialize()).toList();
    await channel.invokeMethod<void>('updateShortcutItems', itemsList);
  }

  @override
  Future<void> updateShortcutItem(ShortcutItem shortcut) async {
    final Map<String, dynamic> item = shortcut.serialize();
    await channel.invokeMethod<void>('updateShortcutItem', [item]);
  }

  @override
  Future<void> changeShortcutItemIcon(String id, String icon) async {
    await channel.invokeMethod<void>('changeShortcutItemIcon', [id, icon]);
  }
}
