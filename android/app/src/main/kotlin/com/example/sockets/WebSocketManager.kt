package com.example.sockets

import android.content.Intent
import io.flutter.plugin.common.MethodChannel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.net.URL
import io.flutter.embedding.engine.FlutterEngine

class WebSocketManager : WebSocketListener() {
    public var webSocket: WebSocket? = null

    private val CHANNEL = "com.example.sockets/websocket"


    public var URL : String = "";

    fun startWebSocket(webSocketListener: WebSocketListener) {
        val client = OkHttpClient()
        val request = Request.Builder().url(URL).build()
        webSocket = client.newWebSocket(request, webSocketListener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        println("socket open");
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        println("message received: $text");
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        println("Message : $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        closeWebSocket();
        println("socket closed")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        if (response != null) {
            println("failed : ${response.message}")
        }
    }

    fun closeWebSocket(){
        if(webSocket == null){
            println("Socket is Null");
        }
        webSocket?.close(1000, null);
    }
    // Add other WebSocketListener methods as needed
}