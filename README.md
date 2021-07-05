# Flutter Shortcuts

<img src="https://i.imgur.com/462Y6wf.gif" title="Flutter_Shortcuts" />

![GitHub](https://img.shields.io/github/license/DevsOnFlutter/flutter_shortcuts?style=plastic) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/DevsOnFlutter/flutter_shortcuts?style=plastic) ![GitHub top language](https://img.shields.io/github/languages/top/DevsOnFlutter/flutter_shortcuts?style=plastic) ![GitHub language count](https://img.shields.io/github/languages/count/DevsOnFlutter/flutter_shortcuts?style=plastic) ![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/DevsOnFlutter/flutter_shortcuts?style=plastic) ![GitHub issues](https://img.shields.io/github/issues/DevsOnFlutter/flutter_shortcuts?style=plastic) ![GitHub Repo stars](https://img.shields.io/github/stars/DevsOnFlutter/flutter_shortcuts?style=social) ![GitHub forks](https://img.shields.io/github/forks/DevsOnFlutter/flutter_shortcuts?style=social)

##  Compatibility

✅ &nbsp; Android </br>
❌ &nbsp; iOS (active issue: [iOS support for quick actions](https://github.com/DevsOnFlutter/flutter_shortcuts/issues/1))

## Show some :heart: and :star: the repo

## Why use Flutter Shortcuts?

Flutter Shortcuts Plugin is known for :

| Flutter Shortcuts |
| :--------------------------------- |
| Fast, performant & compatible |
| Free & Open-source |
| Production ready | 
| Make App Reactive |

## Features

All the features listed below can be performed at the runtime.

✅ &nbsp; Create Shortcuts </br>
✅ &nbsp; Clear Shortcuts </br>
✅ &nbsp; Update Shortcuts </br>
✅ &nbsp; Use both flutter and android asset as shortcut icon </br>

## Demo

|<img height=500 src="https://i.imgur.com/UPcyPEl.gif"/>|
|---|

## Quick Start

### Step 1: Include plugin to your project

```yml
dependencies:
  flutter_shortcuts: <latest version>
```

Run pub get and get packages.

### Step 2: Instantiate Flutter Shortcuts Plugin

```dart
final FlutterShortcuts flutterShortcuts = FlutterShortcuts();
```

### Step 3: Initialize Flutter Shortcuts

```dart
flutterShortcuts.initialize(debug: true);
```

## Example

### Define Shortcut Action

```dart
flutterShortcuts.listenAction((String incomingAction) {
    setState(() {
    if (incomingAction != null) {
        action = incomingAction;
    }
  });
});
```

### Get Max Shortcut Limit

Return the maximum number of static and dynamic shortcuts that each launcher icon can have at a time.

```dart
int? result = await flutterShortcuts.getMaxShortcutLimit();
```

### Shortcut Icon Asset

Flutter Shortcuts allows you to create shortcut icon from both android `drawable` or `mipmap` and flutter Assets.

* If you want to use icon from Android resources, `drawable` or `mipmap`.

use: `ShortcutIconAsset.androidAsset`

```dart
FlutterShortcutItem(
  id: "2",
  action: 'Bookmark page action',
  shortLabel: 'Bookmark Page',
  icon: "ic_launcher",
  shortcutIconAsset: ShortcutIconAsset.androidAsset,
),
```

* If you want to create shortcut icon from flutter asset. (DEFAULT)

use: `ShortcutIconAsset.flutterAsset`

```dart
FlutterShortcutItem(
  id: "2",
  action: 'Bookmark page action',
  shortLabel: 'Bookmark Page',
  icon: 'assets/icons/bookmark.png',
  shortcutIconAsset: ShortcutIconAsset.flutterAsset,
),
```

### Set shortcut items

Publishes the list of shortcuts. All existing shortcuts will be replaced.

```dart
flutterShortcuts.setShortcutItems(
  shortcutItems: <FlutterShortcutItem>[
    const FlutterShortcutItem(
      id: "1",
      action: 'Home page action',
      shortLabel: 'Home Page',
      icon: 'assets/icons/home.png',
    ),
    const FlutterShortcutItem(
      id: "2",
      action: 'Bookmark page action',
      shortLabel: 'Bookmark Page',
      icon: "ic_launcher",
      shortcutIconAsset: ShortcutIconAsset.androidAsset,
    ),
  ],
),
```

### Clear shortcut item

Delete all dynamic shortcuts from the app.

```dart
flutterShortcuts.clearShortcutItems();
```

### Push Shortcut Item

Push a new shortcut item. If there is already a dynamic or pinned shortcut with the same **ID**, the shortcut will be updated and pushed at the end of the shortcut list.

```dart
flutterShortcuts.pushShortcutItem(
  shortcut: FlutterShortcutItem(
    id: "5",
    action: "Play Music Action",
    shortLabel: "Play Music",
    icon: 'assets/icons/music.png',
  ),
);
```

### Push Shortcut Items

Pushes a list of shortcut item. If there is already a dynamic or pinned shortcut with the same **ID**, the shortcut will be updated and pushed at the end of the shortcut list.

```dart
flutterShortcuts.pushShortcutItems(
  shortcutList: <FlutterShortcutItem>[
    const FlutterShortcutItem(
      id: "1",
      action: 'Home page new action',
      shortLabel: 'Home Page',
      icon: 'assets/icons/home.png',
    ),
    const FlutterShortcutItem(
      id: "2",
      action: 'Bookmark page new action',
      shortLabel: 'Bookmark Page',
      icon: 'assets/icons/bookmark.png',
    ),
    const FlutterShortcutItem(
      id: "3",
      action: 'Settings Action',
      shortLabel: 'Setting',
      icon: 'assets/icons/settings.png',
    ),
  ],
);
```

### Update Shortcut Item

Updates a single shortcut item based on id. If the ID of the shortcut is not same, no changes will be reflected.

```dart
flutterShortcuts.updateShortcutItem(
  shortcut: FlutterShortcutItem(
    id: "1",
    action: 'Go to url action',
    shortLabel: 'Visit Page',
    icon: 'assets/icons/url.png',
  ),
);
```

### Update Shortcut Items

 Updates shortcut items. If the IDs of the shortcuts are not same, no changes will be reflected.

 ```dart
flutterShortcuts.updateShortcutItems(
  shortcutList: <FlutterShortcutItem>[
    const FlutterShortcutItem(
      id: "1",
      action: 'Resume playing Action',
      shortLabel: 'Resume playing',
      icon: 'assets/icons/play.png',
    ),
    const FlutterShortcutItem(
      id: "2",
      action: 'Search Songs Action',
      shortLabel: 'Search Songs',
      icon: 'assets/icons/search.png',
    ),
  ],
);
 ```

### Change Shortcut Item Icon

Change the icon of the shortcut based on id. If the ID of the shortcut is not same, no changes will be reflected.

```dart
flutterShortcuts.changeShortcutItemIcon(
  id: "2",
  icon: "assets/icons/next.png",
);
```

### Get shortcut icon properties

Get the icon properties of your shortcut icon.

```dart
Map<String, int> result = await flutterShortcuts.getIconProperties();
print( "maxHeight: ${result["maxHeight"]}, maxWidth: ${result["maxWidth"]}");
```

## Project Created & Maintained By

### Divyanshu Shekhar

<a href="https://twitter.com/dshekhar17"><img src="https://github.com/aritraroy/social-icons/blob/master/twitter-icon.png?raw=true" width="60"></a> <a href="https://in.linkedin.com/in/divyanshu-shekhar-a8a04a162"><img src="https://github.com/aritraroy/social-icons/blob/master/linkedin-icon.png?raw=true" width="60"></a> <a href="https://instagram.com/dshekhar17"><img src="https://github.com/aritraroy/social-icons/blob/master/instagram-icon.png?raw=true" width="60"></a>

[![GitHub followers](https://img.shields.io/github/followers/divshekhar.svg?style=social&label=Follow)](https://github.com/divshekhar/)

### Subham Praharaj

<a href="https://twitter.com/SubhamPraharaj6"><img src="https://github.com/aritraroy/social-icons/blob/master/twitter-icon.png?raw=true" width="60"></a> <a href="https://www.linkedin.com/in/subham-praharaj-66b172179/"><img src="https://github.com/aritraroy/social-icons/blob/master/linkedin-icon.png?raw=true" width="60"></a> <a href="https://instagram.com/the_champ_subham_865"><img src="https://github.com/aritraroy/social-icons/blob/master/instagram-icon.png?raw=true" width="60"></a>

[![GitHub followers](https://img.shields.io/github/followers/skpraharaj.svg?style=social&label=Follow)](https://github.com/skpraharaj/)

## Contributions

Contributions are welcomed!

If you feel that a hook is missing, feel free to open a pull-request.

For a custom-hook to be merged, you will need to do the following:

Describe the use-case.

* Open an issue explaining why we need this hook, how to use it, ...
  This is important as a hook will not get merged if the hook doens't appeal to
  a large number of people.

* If your hook is rejected, don't worry! A rejection doesn't mean that it won't
  be merged later in the future if more people shows an interest in it.
  In the mean-time, feel free to publish your hook as a package on https://pub.dev.

* A hook will not be merged unles fully tested, to avoid breaking it inadvertendly
  in the future.
  
## Stargazers
[![Stargazers repo roster for @DevsOnFlutter/flutter_shortcuts](https://reporoster.com/stars/dark/DevsOnFlutter/flutter_shortcuts)](https://github.com/DevsOnFlutter/flutter_shortcuts/stargazers)

## Forkers

[![Forkers repo roster for @DevsOnFlutter/flutter_shortcuts](https://reporoster.com/forks/dark/DevsOnFlutter/flutter_shortcuts)](https://github.com/DevsOnFlutter/flutter_shortcuts/network/members)

## Copyright & License

Code and documentation Copyright (c) 2021 [Divyanshu Shekhar](https://divyanshushekhar.com). Code released under the [BSD 3-Clause License](./LICENSE).
