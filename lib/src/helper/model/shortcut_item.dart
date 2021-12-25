/* 

              Copyright (c) 2021 divshekhar (Divyanshu Shekhar). 
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import '../enums/shortcut_icon_asset.dart';

class ShortcutItem {
  /// Create a shortcut item.
  /// Eg.
  /// ```dart
  /// const ShortcutItem(
  ///   id: "1",
  ///   action: 'Home page action',
  ///   shortLabel: 'Home Page',
  ///   icon: 'assets/icons/home.png',
  /// );
  /// ```

  const ShortcutItem({
    required this.id,
    required this.action,
    required this.shortLabel,
    this.shortcutIconAsset = ShortcutIconAsset.flutterAsset,
    this.longLabel,
    this.icon,
    this.conversationShortcut = false,
    this.isImportant = false,
    this.isBot = false,
  });

  /// ID of the shortcut that differentiates it from other shortcuts.
  final String id;

  /// Action performed by the shortcut
  final String action;

  /// Short label of the shortcut
  final String shortLabel;

  /// Long label of the shortcut
  final String? longLabel;

  /// Flutter asset path. Only Supports image files. Eg. .png/.jpg
  final String? icon;

  /// `ShortcutIconType.androidAsset` or `ShortcutIconType.flutterAsset`
  final ShortcutIconAsset shortcutIconAsset;

  /// `true` if the shortcut is a person.
  final bool conversationShortcut;

  /// true if the person shortcut is a person
  final bool isImportant;

  /// true if the person shortcut is a machine/bot
  final bool isBot;

  Map<String, dynamic> serialize() {
    return <String, dynamic>{
      'id': id,
      'action': action,
      'shortLabel': shortLabel,
      'longLabel': longLabel,
      'icon': icon,
      'shortcutIconType': shortcutIconAsset.index.toString(),
      'conversationShortcut': conversationShortcut,
      'isImportant': isImportant,
      'isBot': isBot,
    };
  }
}
