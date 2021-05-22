import 'package:flutter/services.dart';
import 'package:flutter_shortcuts/src/platform/flutter_shortcuts_platform.dart';
import 'package:flutter_shortcuts/src/types/types.dart';

class FlutterShortcutsMethodCallHandler extends FlutterShortcutsPlatform {
  final MethodChannel _channel =
      MethodChannel('com.divyanshushekhar.flutter_shortcuts');

  MethodChannel get channel => _channel;

  @override
  Future<void> initialize(FlutterShortcutAction actionHandler) async {
    channel.setMethodCallHandler((MethodCall call) async {
      assert(call.method == 'launch');
      actionHandler(call.arguments);
    });
    final String action = await channel.invokeMethod<String>('getLaunchAction');
    if (action != null) {
      actionHandler(action);
    }
  }

  @override
  Future<void> setShortcutItems(List<FlutterShortcutItem> items) async {
    final List<Map<String, String>> itemsList =
        items.map(_serializeItem).toList();
    await channel.invokeMethod<void>('setShortcutItems', itemsList);
  }

  @override
  Future<void> clearShortcutItems() async {
    await channel.invokeMethod<void>('clearShortcutItems');
  }

  @override
  Future<void> updateAllShortcutItems(List<FlutterShortcutItem> items) async {
    final List<Map<String, String>> itemsList =
        items.map(_serializeItem).toList();
    await channel.invokeMethod<void>('updateShortcutItems', itemsList);
  }

  Map<String, String> _serializeItem(FlutterShortcutItem item) {
    return <String, String>{
      'id': item.id,
      'action': item.action,
      'title': item.title,
      'icon': item.icon,
    };
  }
}
