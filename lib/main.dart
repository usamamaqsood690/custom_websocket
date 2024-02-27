import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sockets/web_socket.dart';

void main() {
    runApp(MyApp());
}


class MyApp extends StatelessWidget {
  MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  StreamSubscription? _streamSubscription;
  MyWebSocketPlugin myWebSocketPlugin = MyWebSocketPlugin();

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    _streamSubscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('WebSocket Flutter App'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              onPressed: () {
                myWebSocketPlugin.startWebSocket("wss://ir.directfn.com/ws");
              },
              child: Text('Start WebSocket'),
            ), ElevatedButton(
              onPressed: () {
                myWebSocketPlugin.sendMessage('129{"AUTHVER":"10","LOGINIP":"192.168.0.1","CLVER":"1.0.0","PDM":"40","LAN":"EN","METAVER":"","UNM":"IR_DECYPHAWEB","PWD":"123456"} "');
              },
              child: Text('Send Message 1'),
            ),
            ElevatedButton(
              onPressed: () {
                myWebSocketPlugin.sendMessage('33{"80":"0","E":"CASE","S":"MASR"} "');
              },
              child: Text('Send Message 2'),
            ),
            ElevatedButton(
              onPressed: () {
                myWebSocketPlugin.disconnectWebSocket();
              },
              child: Text('Disconnect Socket'),
            ),

            ElevatedButton(
              onPressed: () {
                _streamSubscription = myWebSocketPlugin.onListenStream();
              },
              child: Text('Listen Stream'),
            ),          ],
        ),
      ),
    );
  }
}
