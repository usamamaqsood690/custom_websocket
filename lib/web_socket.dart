import 'dart:async';

import 'package:flutter/services.dart';

class MyWebSocketPlugin {
   static const platform = MethodChannel('com.example.sockets/websocket');
   static const platformEvent = EventChannel('com.example.sockets/websocket_listen');


    onListenStream(){
    return platformEvent.receiveBroadcastStream().listen((event) {
print("Data: ${event.toString()}");
     },
    );
   }

   Future<void> startWebSocket(String url) async {
    var args = {
      "url": url
    };
    String connectionResult = await platform.invokeMethod('connect', {'args': args});
    print(connectionResult);
  }

  Future<void> sendMessage(String message) async {
   var args = {
      "message": message
      };
    platform.invokeMethod("message", {'args': args}).then((value) => print(value));
  }

  Future<void> disconnectWebSocket() async {
     platform.invokeMethod("disconnect",).then((value) => print(value));
   }

}