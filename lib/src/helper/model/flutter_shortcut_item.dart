/* 

              Copyright (c) 2021 divshekhar (Divyanshu Shekhar). 
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'package:flutter/material.dart';

class FlutterShortcutItem {
  const FlutterShortcutItem({
    @required this.id,
    @required this.action,
    @required this.shortLabel,
    this.longLabel,
    this.icon,
  });

  // Id of the shortcut
  final String id;

  // Action performed by the shortcut
  final String action;

  // Short label of the shortcut
  final String shortLabel;

  // Long label of the shortcut
  final String longLabel;

  // Icon of the shortcut
  final String icon;
}
