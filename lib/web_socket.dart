import 'dart:async';

import 'package:flutter/services.dart';

class MyWebSocketPlugin {
   static const platform = MethodChannel('com.example.sockets/websocket');

  static Future<void> startWebSocket(String url) async {
    var connect = {
      "type": "connect",
      "url": url
    };
    String connectionResult = await platform.invokeMethod('connect', {'args': connect});
    print(connectionResult);
  }

  
 static Future<void> sendMessage(String message) async {
   var message = {
      "type": "message",
      "url": "wss://ir.directfn.com/ws",
      "message": '129{"AUTHVER":"10","LOGINIP":"192.168.0.1","CLVER":"1.0.0","PDM":"40","LAN":"EN","METAVER":"","UNM":"IR_DECYPHAWEB","PWD":"123456"} "'
      };
    platform.invokeMethod("connect", {'args': message}).then((value) => print(value));
  }

   static Future<void> sendMessage2(String message) async {
   var message = {
      "type": "message",
      "url": "wss://ir.directfn.com/ws",
      "message": '33{"80":"0","E":"CASE","S":"COMI"} "'
      };
    platform.invokeMethod("connect", {'args': message}).then((value) => print(value));
  }

}