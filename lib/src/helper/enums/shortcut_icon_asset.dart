/* 

              Copyright (c) 2021 divshekhar (Divyanshu Shekhar). 
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

enum ShortcutIconAsset {
  /// Creates Icon from the native resources `drawable` & `mipmap` directory.
  androidAsset,

  /// Creates Icon from the flutter resources `assets/icons/flutter.png`.
  flutterAsset,

  /// Creates Icon from base64 **JPEG** image.
  ///
  /// You can use helper function like so:
  /// ```dart
  /// Uint8List bytes = ...;
  ///
  /// // Pass this string to [icon]
  /// ShortcutMemoryIcon(bytes).toString()
  /// ```
  memoryAsset,
}
