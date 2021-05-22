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

    /*
      flutterShortcuts.staticShortcutsInitialize((String id) {
        if(id == 'Homepage') {
          Navigator.pushNamed(context, '/Homepage');
        } else if(id == 'Secondpage') {
          Navigator.pushNamed(context, '/Secondpage');
        }
      }); 
    */
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Flutter Shortcuts Plugin'),
        ),
        body: Column(
          children: [
            Center(
              child: Text('ShortcutItem action : $action\n'),
            ),
            ElevatedButton(
              child: Text("Set Shortcuts"),
              onPressed: () async {
                await flutterShortcuts.setShortcutItems(<FlutterShortcutItem>[
                  const FlutterShortcutItem(
                    id: "1",
                    action: 'Homepage',
                    title: 'Play Ludo',
                    icon: 'ic_launcher',
                  ),
                  const FlutterShortcutItem(
                    id: "2",
                    action: 'Secondpage',
                    title: 'Play Snake Ladder',
                    icon: 'ic_launcher',
                  ),
                ]).then((value) {
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
            ElevatedButton(
              child: Text("Update all shortcuts"),
              onPressed: () async {
                await flutterShortcuts
                    .updateAllShortcutItems(shortcutList: <FlutterShortcutItem>[
                  const FlutterShortcutItem(
                    id: "1",
                    action: 'Homepage',
                    title: 'Home Page 1',
                    icon: 'ic_launcher',
                  ),
                  const FlutterShortcutItem(
                    id: "2",
                    action: 'Secondpage',
                    title: 'Second Page 1',
                    icon: 'ic_launcher',
                  ),
                ]);
              },
            ),
            ElevatedButton(
              child: Text("Change icon of 2nd Shortcut"),
              onPressed: () {
                // flutterShortcuts.updateShortcutItemIcon(id, icon);
              },
            ),
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
              child: Text("Change title of 2nd Shortcut"),
              onPressed: () {
                // flutterShortcuts.updateShortcutItemTitle(id, title);
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
    );
  }
}
