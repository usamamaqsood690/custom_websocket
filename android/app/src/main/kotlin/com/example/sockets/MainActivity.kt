package com.example.sockets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class MainActivity: FlutterActivity() {
    private val web = WebSocketManager();
    private val CHANNEL = "com.example.sockets/websocket"
    private val CHANNEL_LISTEN_EVENTS = "com.example.sockets/websocket_listen"
    private val CUSTOM_INTENT_ACTION = "com.example.ACTION_CUSTOM_INTENT"



    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                if (call.method == "connect") {
                    val args = call.argument<HashMap<String,String>>("args")
                    if (args != null) {
                        web.URL = args["url"].toString()
                        web.startWebSocket( MyWebSocketListener(context))
                        result.success("socket connected")
                    }else{
                        result.error("","Invalid Arguments: url is required","")
                    }
                }
                else if (call.method == "message") {
                    val args = call.argument<HashMap<String,String>>("args")
                    if (args != null) {
                        web.sendMessage(args["message"].toString())
                        result.success("Message Sent")
                    }else{
                        result.error("","Invalid Arguments: message is required","")
                    }
                }else if (call.method == "disconnect") {
                    web.closeWebSocket();
                        result.success("Web Socket Closed")
                } else {
                    result.notImplemented()
                }
            }
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_LISTEN_EVENTS).setStreamHandler(
            object : EventChannel.StreamHandler {
                private var receiver: BroadcastReceiver? = null
                override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                    receiver = initReceiver(events)
                    context.registerReceiver(receiver, IntentFilter(CUSTOM_INTENT_ACTION))

                }

                private fun initReceiver(events: EventChannel.EventSink): BroadcastReceiver? {
                    return object : BroadcastReceiver(){
                        override fun onReceive(context: Context?, intent: Intent?) {
                            if (intent?.action == CUSTOM_INTENT_ACTION) {
                                // Handle your custom intent here
                                val message = intent.getStringExtra("message")
                                events.success(message)
                            }

                        }
                    }
                }

                override fun onCancel(arguments: Any?) {
                    context.unregisterReceiver(receiver)
                    receiver = null
                }
            }
        )
    }


}

class  MyWebSocketListener(private val context: Context) : WebSocketListener(
) {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        // Handle WebSocket open event
        println("WebSocket opened")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val CUSTOM_INTENT_ACTION = "com.example.ACTION_CUSTOM_INTENT"
        // Handle WebSocket message event
        val intent = Intent(CUSTOM_INTENT_ACTION)
        intent.putExtra("message", text)
        context.sendBroadcast(intent)
        println("WebSocket Message ---------- ")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        // Handle WebSocket closing event
        println("WebSocket closing")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        // Handle WebSocket failure event
        println("WebSocket failure: ${t.message}")
    }
}