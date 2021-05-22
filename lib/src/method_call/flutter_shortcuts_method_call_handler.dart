import 'package:flutter/services.dart';
import 'package:flutter_shortcuts/src/platform/flutter_shortcuts_platform.dart';
import 'package:flutter_shortcuts/src/types/types.dart';

class FlutterShortcutsMethodCallHandler extends FlutterShortcutsPlatform {
  final MethodChannel _channel =
      MethodChannel('com.divyanshushekhar.flutter_shortcuts');

  MethodChannel get channel => _channel;

  @override
  Future<void> initialize(FlutterShortcutAction action) async {
    // super.initialize(action);
    channel.setMethodCallHandler((MethodCall call) async {
      assert(call.method == 'launch');
      action(call.arguments);
    });
    final String actions =
        await channel.invokeMethod<String>('getLaunchAction');
    if (action != null) {
      action(actions);
    }
  }

  @override
  Future<void> setShortcutItems(List<FlutterShortcutItem> items) async {
    // super.setShortcutItems(items);
    final List<Map<String, String>> itemsList =
        items.map(_serializeItem).toList();
    await channel.invokeMethod<void>('setShortcutItems', itemsList);
  }

  Map<String, String> _serializeItem(FlutterShortcutItem item) {
    return <String, String>{
      'action': item.action,
      'title': item.title,
      'icon': item.icon,
    };
  }
}
