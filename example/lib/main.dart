import 'package:flutter/material.dart';
import 'package:flutter_shortcuts/flutter_shortcuts.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String action = 'No Action';
  final FlutterShortcuts flutterShortcuts = FlutterShortcuts();
  int? maxLimit;

  @override
  void initState() {
    super.initState();
    flutterShortcuts.initialize(debug: true);
    flutterShortcuts.listenAction((String incomingAction) {
      setState(() {
        action = incomingAction;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Flutter Shortcuts Plugin'),
        ),
        body: SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.all(10),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Center(
                    child: Text(
                      'ShortcutItem action : $action\n',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
                Text(
                    "Flutter Shortcuts Max Limit -> ${maxLimit == null ? '' : maxLimit}"),
                Row(
                  children: [
                    ElevatedButton(
                      child: Text("Get Max Shortcut Limit"),
                      onPressed: () async {
                        int? result =
                            await flutterShortcuts.getMaxShortcutLimit();
                        setState(() {
                          maxLimit = result;
                        });
                      },
                    ),
                  ],
                ),
                Divider(),
                Text("Flutter Shortcuts Set and Clear"),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    ElevatedButton(
                      child: Text("Set Shortcuts"),
                      onPressed: () async {
                        await flutterShortcuts.setShortcutItems(
                          shortcutItems: <ShortcutItem>[
                            const ShortcutItem(
                              id: "1",
                              action: 'Home page action',
                              shortLabel: 'Home Page',
                              icon: 'assets/icons/home.png',
                            ),
                            const ShortcutItem(
                              id: "2",
                              action: 'Bookmark page action',
                              shortLabel: 'Bookmark Page',
                              // icon: 'assets/icons/bookmark.png',
                              icon: "ic_launcher",
                              shortcutIconAsset: ShortcutIconAsset.androidAsset,
                            ),
                          ],
                        ).then((value) {
                          setState(() {
                            if (action == 'No Action') {
                              action = 'Flutter Shortcuts Ready';
                            }
                          });
                        });
                      },
                    ),
                    ElevatedButton(
                      child: Text("Clear All Shortcuts"),
                      onPressed: () {
                        flutterShortcuts.clearShortcutItems();
                      },
                    ),
                  ],
                ),
                Divider(),
                Text("Flutter Shortcuts Add and Update"),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    ElevatedButton(
                      child: Text("push Shortcut Item"),
                      onPressed: () async {
                        await flutterShortcuts.pushShortcutItem(
                          shortcut: ShortcutItem(
                            id: "5",
                            action: "Play Music Action",
                            shortLabel: "Play Music",
                            icon: 'assets/icons/music.png',
                          ),
                        );
                      },
                    ),
                    ElevatedButton(
                      child: Text("Update all shortcuts"),
                      onPressed: () async {
                        await flutterShortcuts.updateShortcutItems(
                          shortcutList: <ShortcutItem>[
                            const ShortcutItem(
                              id: "1",
                              action: 'Resume playing Action',
                              shortLabel: 'Resume playing',
                              icon: 'assets/icons/play.png',
                            ),
                            const ShortcutItem(
                              id: "2",
                              action: 'Search Songs Action',
                              shortLabel: 'Search Songs',
                              icon: 'assets/icons/search.png',
                            ),
                          ],
                        );
                      },
                    ),
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    ElevatedButton(
                      child: Text("Add Shortcut"),
                      onPressed: () async {
                        await flutterShortcuts.pushShortcutItems(
                          shortcutList: <ShortcutItem>[
                            const ShortcutItem(
                              id: "1",
                              action: 'Home page new action',
                              shortLabel: 'Home Page',
                              icon: 'assets/icons/home.png',
                            ),
                            const ShortcutItem(
                              id: "2",
                              action: 'Bookmark page new action',
                              shortLabel: 'Bookmark Page',
                              icon: 'assets/icons/bookmark.png',
                            ),
                            const ShortcutItem(
                              id: "3",
                              action: 'Settings Action',
                              shortLabel: 'Setting',
                              icon: 'assets/icons/settings.png',
                            ),
                          ],
                        );
                      },
                    ),
                    ElevatedButton(
                      child: Text("Update Shortcut with ID"),
                      onPressed: () {
                        flutterShortcuts.updateShortcutItem(
                          shortcut: ShortcutItem(
                            id: "1",
                            action: 'Go to url action',
                            shortLabel: 'Visit Page',
                            icon: 'assets/icons/url.png',
                          ),
                        );
                      },
                    ),
                  ],
                ),
                Divider(),
                Text("Icons"),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    ElevatedButton(
                      child: Text("icon Properties"),
                      onPressed: () async {
                        Map<String, int> result =
                            await flutterShortcuts.getIconProperties();
                        print(
                          "maxHeight: ${result["maxHeight"]}, maxWidth: ${result["maxWidth"]}",
                        );
                      },
                    ),
                    ElevatedButton(
                      child: Text("Change icon of 2nd Shortcut"),
                      onPressed: () {
                        flutterShortcuts.changeShortcutItemIcon(
                          id: "2",
                          icon: "assets/icons/next.png",
                        );
                      },
                    ),
                  ],
                ),
                Text("Conversation Shortcuts"),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    ElevatedButton(
                      child: Text("Set conversation Shortcut"),
                      onPressed: () async {
                        await flutterShortcuts.setShortcutItems(
                          shortcutItems: <ShortcutItem>[
                            const ShortcutItem(
                              id: "1",
                              action: 'open_chat_1',
                              shortLabel: 'Divyanshu Shekhar',
                              icon: 'assets/icons/home.png',
                              conversationShortcut: true,
                              isImportant: true,
                            ),
                            const ShortcutItem(
                              id: "2",
                              action: 'oepn_chat_2',
                              shortLabel: 'Subham Praharaj',
                              icon: 'assets/icons/bookmark.png',
                              conversationShortcut: true,
                            ),
                            const ShortcutItem(
                              id: "3",
                              action: 'oepn_chat_3',
                              shortLabel: 'Auto Reply Bot',
                              icon: 'assets/icons/url.png',
                              conversationShortcut: true,
                              isBot: true,
                            ),
                          ],
                        );
                      },
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
