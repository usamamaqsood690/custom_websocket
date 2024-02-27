import 'dart:async';

import 'package:flutter/services.dart';

class MyWebSocketPlugin {
   static const platform = MethodChannel('com.example.sockets/websocket');
   static const platformEvent = EventChannel('com.example.sockets/websocket_listen');


   static onListenStream(){
    return platformEvent.receiveBroadcastStream().listen((event) {
print("HAHAHAHAH   ${event.toString()}");
     });
   }

  static Future<void> startWebSocket(String url) async {
    var args = {
      "url": url
    };
    String connectionResult = await platform.invokeMethod('connect', {'args': args});
    print(connectionResult);
  }

  
 static Future<void> sendMessage(String message) async {
   var args = {
      "message": message
      };
    platform.invokeMethod("message", {'args': args}).then((value) => print(value));
  }

   static Future<void> disconnectWebSocket() async {
     platform.invokeMethod("disconnect",).then((value) => print(value));
   }

}