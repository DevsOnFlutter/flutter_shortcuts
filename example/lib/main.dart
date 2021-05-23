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
  int maxLimit;

  @override
  void initState() {
    super.initState();

    flutterShortcuts.initialize((String incomingAction) {
      setState(() {
        if (incomingAction != null) {
          action = incomingAction;
        }
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
                        int result =
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
                          shortcutItems: <FlutterShortcutItem>[
                            const FlutterShortcutItem(
                              id: "1",
                              action: 'Home page action',
                              shortLabel: 'Home Page',
                              icon: 'ic_launcher',
                            ),
                            const FlutterShortcutItem(
                              id: "2",
                              action: 'Second page action',
                              shortLabel: 'Second Page',
                              icon: 'ic_launcher',
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
                          shortcut: FlutterShortcutItem(
                            id: "5",
                            action: "Fifth action",
                            shortLabel: "Shortcut 5",
                          ),
                        );
                      },
                    ),
                    ElevatedButton(
                      child: Text("Update all shortcuts"),
                      onPressed: () async {
                        await flutterShortcuts.updateShortcutItems(
                          shortcutList: <FlutterShortcutItem>[
                            const FlutterShortcutItem(
                              id: "1",
                              action: 'Home page action updated',
                              shortLabel: 'Home Page updated',
                              icon: 'ic_launcher',
                            ),
                            const FlutterShortcutItem(
                              id: "2",
                              action: 'Second page action updated',
                              shortLabel: 'Second Page updated',
                              icon: 'ic_launcher',
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
                            shortcutList: <FlutterShortcutItem>[
                              const FlutterShortcutItem(
                                id: "1",
                                action: 'homepage new action',
                                shortLabel: 'Home Page',
                                icon: 'ic_launcher',
                              ),
                              const FlutterShortcutItem(
                                id: "2",
                                action: 'second page new action',
                                shortLabel: 'Second Page',
                                icon: 'ic_launcher',
                              ),
                              const FlutterShortcutItem(
                                id: "3",
                                action: 'Thirdpage',
                                shortLabel: 'Third Page',
                                icon: 'ic_launcher',
                              ),
                            ]);
                      },
                    ),
                    ElevatedButton(
                      child: Text("Update Shortcut with ID"),
                      onPressed: () {
                        flutterShortcuts.updateShortcutItem(
                          id: "1",
                          shortcut: FlutterShortcutItem(
                            id: "1",
                            action: 'action updated with ID',
                            shortLabel: 'Homepage',
                            icon: 'ic_launcher',
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
                      child: Text("Change icon of 2nd Shortcut"),
                      onPressed: () {
                        flutterShortcuts.changeShortcutItemIcon(
                          id: "2",
                          icon: "bookmark_icon",
                        );
                      },
                    ),
                  ],
                ),
                Divider(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
