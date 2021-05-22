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

  @override
  void initState() {
    super.initState();
    final FlutterShortcuts flutterShortcuts = FlutterShortcuts();
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

    flutterShortcuts.setShortcutItems(<FlutterShortcutItem>[
      const FlutterShortcutItem(
        action: 'Homepage',
        title: 'Home Page',
        icon: 'ic_launcher',
      ),
      const FlutterShortcutItem(
        action: 'Secondpage',
        title: 'Second Page',
        icon: 'ic_launcher',
      ),
    ]).then((value) {
      setState(() {
        if (action == 'No Action') {
          action = 'Flutter Shortcuts Ready';
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
        body: Center(
          child: Text('ShortcutItem action : $action\n'),
        ),
      ),
    );
  }
}
