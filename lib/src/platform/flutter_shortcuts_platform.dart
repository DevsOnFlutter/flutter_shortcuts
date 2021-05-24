/* 

              Copyright (c) 2021 divshekhar (Divyanshu Shekhar). 
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'package:flutter_shortcuts/src/method_call/flutter_shortcuts_method_call_handler.dart';
import 'package:flutter_shortcuts/src/helper/helper.dart';
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

  Future<int?> getMaxShortcutLimit() {
    throw UnimplementedError("getMaxShortcutLimit() has not been implemented.");
  }

  Future<Map<String, int>> getIconProperties() {
    throw UnimplementedError("getIconProperties() has not been implemented.");
  }

  Future<void> setShortcutItems(List<FlutterShortcutItem> items) async {
    throw UnimplementedError("setShortcutItems() has not been implemented.");
  }

  Future<void> clearShortcutItems() async {
    throw UnimplementedError("clearShortcutItems() has not been implemented.");
  }

  Future<void> pushShortcutItem(FlutterShortcutItem shortcut) async {
    throw UnimplementedError("pushShortcutItem() has not been implemented.");
  }

  Future<void> pushShortcutItems(List<FlutterShortcutItem> items) async {
    throw UnimplementedError("pushShortcutItems() has not been implemented.");
  }

  Future<void> updateShortcutItems(List<FlutterShortcutItem> items) async {
    throw UnimplementedError("updateShortcutItems() has not been implemented.");
  }

  Future<void> updateShortcutItem(FlutterShortcutItem shortcut) async {
    throw UnimplementedError("updateShortcutItem() has not been implemented.");
  }

  // Future<void> updateShortLabel(String id, String shortLabel) async {
  //   throw UnimplementedError("updateShortcutItem() has not been implemented.");
  // }

  // Future<void> updateLongLabel(String id, String longLabel) async {
  //   throw UnimplementedError("updateShortcutItem() has not been implemented.");
  // }

  Future<void> changeShortcutItemIcon(String id, String icon) async {
    throw UnimplementedError(
        "changeShortcutItemIcon() has not been implemented.");
  }
}
