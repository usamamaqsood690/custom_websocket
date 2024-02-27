package com.example.sockets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import org.json.JSONObject

class MainActivity: FlutterActivity() {
    private val web = WebSocketManager();
    private val CHANNEL = "com.example.sockets/websocket"
    private val CHANNEL_LISTEN_EVENTS = "com.example.sockets/websocket_listen"
    private lateinit var channel: MethodChannel
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
       channel =  MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)

        EventChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_LISTEN_EVENTS).setStreamHandler(
            object : EventChannel.StreamHandler {
                private var receiver: BroadcastReceiver? = null
                override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                    receiver = initReceiver(events)
                    context.registerReceiver(receiver, IntentFilter())

                }

                private fun initReceiver(events: EventChannel.EventSink): BroadcastReceiver? {
                    return object : BroadcastReceiver(){
                        override fun onReceive(context: Context?, intent: Intent?) {
                            events.success("Testing")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            Handler(Looper.getMainLooper()).postDelayed({
                channel.setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                    if (call.method == "connect") {
                        val args = call.argument<HashMap<String,String>>("args")
                        if (args != null) {
                            web.URL = args["url"].toString()
                            web.startWebSocket()
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
                        println("Wah Wah -------")
                        result.notImplemented()
                    }
                }
            },0)
    }


}
