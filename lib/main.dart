import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sockets/web_socket.dart';

void main() {
    runApp(MyApp());
  
}


class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
   static const platform = MethodChannel('com.example.sockets/websocket');
  @override
  void initState() {
  platform.setMethodCallHandler((call) {
        if(call.method == "socketCallBack"){
          print("ma a gya");
        }
        return Future(() => null);
      });

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
                MyWebSocketPlugin.startWebSocket("wss://ir.directfn.com/ws");
              },
              child: Text('Start WebSocket'),
            ), ElevatedButton(
              onPressed: () {
                MyWebSocketPlugin.sendMessage("wss://ir.directfn.com/ws");
              },
              child: Text('Send Message'),
            ),
            ElevatedButton(
              onPressed: () {
                MyWebSocketPlugin.sendMessage2("wss://ir.directfn.com/ws");
              },
              child: Text('Send 2nd Message'),
            ),
          ],
        ),
      ),
    );
  }
}
