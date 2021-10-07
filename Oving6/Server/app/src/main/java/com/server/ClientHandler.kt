package com.server

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientHandler(
    private val clientSocket: Socket,
    private val infoText: TextView
) {

    fun start() {
        try {
            Log.i("ClientHandler()", this.toString())
            setUI("En Klient koblet seg til:\n$clientSocket")
            sendToClient(clientSocket, "Velkommen Klient!")
            readFromClient(clientSocket)
        } catch (e: Exception) {
            e.printStackTrace()
            setUI(e.message.toString())
        }
    }

    private fun readFromClient(socket: Socket) {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val message = reader.readLine()
        Log.i("readFromClient()", message!!)
        setUI("Klienten sier:\n$message")
    }
    private fun sendToClient(socket: Socket, message: String) {
        val writer = PrintWriter(socket.getOutputStream(), true)
        writer.println(message)
        Log.i("sendToClient()", message)
        setUI("Sendte følgende til klienten:\n$message")
    }
    private fun setUI(str: String) {
        MainScope().launch { infoText.text = str }
    }

}