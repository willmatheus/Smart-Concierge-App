package com.example.smartconciergeapp.service

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.smartconciergeapp.R
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response

private const val NOTIFICATION_ID = 155
private const val CHANNEL_ID = "primary_notification_channel"

data class RegisterMessage(val type: String)

class WebSocketService(private val context: Context) : WebSocketListener() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var webSocket: WebSocket

    private val _visitorName = mutableStateOf("")
    val visitorName: State<String> get() = _visitorName

    private val _visitorCPF = mutableStateOf("")
    val visitorCPF: State<String> get() = _visitorCPF

    var onNewVisitorReceived: ((String, String) -> Unit)? = null

    // Inicia o WebSocket
    fun start() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://192.168.1.8:1880/ws") // URL do servidor WebSocket do Node-RED
            .build()

        // Conecta ao WebSocket
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocket", "Conexão aberta!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                try {
                    val message = Gson().fromJson(text, Map::class.java)

                    val visitorName = message["name"] as? String
                    val visitorCpf = message["cpf"] as? String

                    if (visitorName != null && visitorCpf != null) {
                        _visitorName.value = visitorName
                        _visitorCPF.value = visitorCpf

                        // Callback para informar MainActivity
                        onNewVisitorReceived?.invoke(visitorName, visitorCpf)

                        createNotificationChannel()
                        sendNotification(visitorName)
                    } else {
                        Log.d("WebSocket", "Mensagem vazia ou inválida")
                    }

                } catch (e: Exception) {
                    Log.e("WebSocket", "Erro ao processar a mensagem: ${e.message}")
                }
            }



            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("WebSocket", "Conexão fechando: $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("WebSocket", "Conexão fechada: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d("WebSocket", "Erro no WebSocket: ${t.message}")
            }
        })
    }

    fun sendVisitorResponse(isAccepted: Boolean) {
        val payload = mapOf(
            "type" to "visitorResponse",
            "isAccepted" to isAccepted
        )
        val jsonPayload = Gson().toJson(payload) // Serializa o payload para JSON
        webSocket.send(jsonPayload)
        Log.d("WebSocket", "Payload enviado: $jsonPayload")
    }


    private fun createNotificationChannel() {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelName = "Canal Padrão"
        val channelDescription = "Canal de notificações padrão"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
        notificationChannel.description = channelDescription

        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationManager.createNotificationChannel(notificationChannel)

        Log.d("Notification", "Canal criado com sucesso.")
    }


    private fun sendNotification(name: String) {
        val builder = getNotificationBuilder(name)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getNotificationBuilder(name: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Ícone da notificação
            .setContentTitle("$name deseja visitar você!") // Título
            .setContentText("Você autoriza a entrada?") // Texto
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)// Prioridade
    }

}
