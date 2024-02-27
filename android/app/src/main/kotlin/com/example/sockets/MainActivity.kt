package com.example.sockets

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import org.json.JSONObject

class MainActivity: FlutterActivity() {
    private val web = WebSocketManager();
    private val CHANNEL = "com.example.sockets/websocket"
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                if (call.method == "connect") {
                    val args = call.argument<HashMap<String,String>>("args")
                    if (args != null) {
                        web.URL = args["url"].toString()
                        if (args["type"] == "connect") {
                            web.startWebSocket()
                            result.success("socket connected")
                        }else if(args["type"]=="message"){
                            web.sendMessage(args["message"].toString())
                            result.success("message sent")
                        }
                    }
                }
                else{
                    result.notImplemented()
                }
            }
    }
}
