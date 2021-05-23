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
                Text("Flutter Shortcuts Max Limit"),
                Row(
                  children: [
                    ElevatedButton(
                      child: Text("Get Max Shortcut Limit"),
                      onPressed: () async {
                        int result =
                            await flutterShortcuts.getMaxShortcutLimit();
                        print("======> max: $result");
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
                              action: 'Homepage',
                              shortLabel: 'Play Ludo',
                              icon: 'ic_launcher',
                            ),
                            const FlutterShortcutItem(
                              id: "2",
                              action: 'Secondpage',
                              shortLabel: 'Play Snake Ladder',
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
                            id: "2",
                            action: "fifthaction",
                            shortLabel: "shortLabel",
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
                              action: 'Homepage',
                              shortLabel: 'Home Page 1',
                              icon: 'ic_launcher',
                            ),
                            const FlutterShortcutItem(
                              id: "2",
                              action: 'Secondpage',
                              shortLabel: 'Second Page 2',
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
                        await flutterShortcuts.addShortcutItems(
                            shortcutList: <FlutterShortcutItem>[
                              const FlutterShortcutItem(
                                id: "1",
                                action: 'Homepage',
                                shortLabel: 'Home Page 1',
                                icon: 'ic_launcher',
                              ),
                              const FlutterShortcutItem(
                                id: "2",
                                action: 'Secondpage',
                                shortLabel: 'Second Page 2',
                                icon: 'ic_launcher',
                              ),
                              const FlutterShortcutItem(
                                id: "3",
                                action: 'Thirdpage',
                                shortLabel: 'Third Page 3',
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
                            action: 'Fourthpage',
                            shortLabel: 'Fourth Page 4',
                            icon: 'ic_launcher',
                          ),
                        );
                      },
                    ),
                  ],
                ),
                Divider(),
                Text("Long/Short Label"),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    ElevatedButton(
                      child: Text("Change Short Title"),
                      onPressed: () {
                        // flutterShortcuts.updateShortcutItemTitle(id, title);
                      },
                    ),
                    ElevatedButton(
                      child: Text("Change Long Title"),
                      onPressed: () {
                        // flutterShortcuts.updateShortcutItemTitle(id, title);
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
                      child: Text("Icon Properties"),
                      onPressed: () async {
                        Map<String, int> properties =
                            await flutterShortcuts.getIconProperties();
                        print(properties);
                      },
                    ),
                    ElevatedButton(
                      child: Text("Change icon of 2nd Shortcut"),
                      onPressed: () {
                        flutterShortcuts.changeShortcutItemIcon(
                            id: "2", icon: "bookmark_icon");
                      },
                    ),
                  ],
                ),
                Divider(),
                ElevatedButton(
                  child: Text("change icon color of 2nd Shortcut"),
                  onPressed: () {
                    // disable title
                    // flutterShortcuts.changeIconColor(id, color);
                  },
                ),
                ElevatedButton(
                  child: Text("change icon backgroud color of 2nd Shortcut"),
                  onPressed: () {
                    // disable title
                    // flutterShortcuts.changeIconBackgroundColor(id, color);
                  },
                ),
                ElevatedButton(
                  child: Text("set animated icon of 2nd Shortcut"),
                  onPressed: () {
                    // disable title
                    // flutterShortcuts.setAnimatedIcon(id, AnimatedIcon);
                  },
                ),
                ElevatedButton(
                  child: Text("set icon backgroud gradient of 2nd Shortcut"),
                  onPressed: () {
                    // disable title
                    // flutterShortcuts.iconBackgroundGradient(id,[start,end], [start color,end color]);
                  },
                ),
                ElevatedButton(
                  child: Text("Toggle disable state of 2nd Shortcut"),
                  onPressed: () {
                    // disable title
                    // flutterShortcuts.updateDisableStateShortcutItem(id, state(bool), disable title);
                  },
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
