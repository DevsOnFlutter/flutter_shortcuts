/* 

              Copyright (c) 2021 divshekhar (Divyanshu Shekhar). 
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

class FlutterShortcutItem {
  /// Create a flutter shortcut item.
  /// Eg.
  /// ```
  /// const FlutterShortcutItem(
  /// ```
  /// ```
  ///   id: "1",
  /// ```
  /// ```
  ///   action: 'Home page action',
  /// ```
  /// ```
  ///   shortLabel: 'Home Page',
  /// ```
  /// ```
  ///   icon: 'assets/icons/home.png',
  /// ```
  /// ```
  /// );
  /// ```

  const FlutterShortcutItem({
    required this.id,
    required this.action,
    required this.shortLabel,
    this.longLabel,
    this.icon,
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
}
