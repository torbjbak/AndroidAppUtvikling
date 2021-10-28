package com.server

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientHandler(
    private val clientSocket: Socket,
    private val updateList: (input: String) -> Unit,
    private val infoText: TextView
) {

    fun start() {
        CoroutineScope(IO).launch {
            try {
                readFromClient(clientSocket)
            } catch (e: Exception) {
                e.printStackTrace()
                setUI(e.message.toString())
            }
        }
    }

    private fun readFromClient(socket: Socket) {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val message = reader.readLine().split("][")
        Log.i("readFromClient()", message.toString())
        if(message[0] == "nameID") {
            setUI("En Klient koblet seg til: ${socket.port}")
            val name = message[1]
            sendToClient(socket, "Velkommen, $name!")
            updateList(name)
        } else {
            sendToClient(socket, "${message[0]}: ${message[1]}")
            setUI("${message[0]}: ${message[1]}")
        }
    }

    private fun sendToClient(socket: Socket, message: String) {
        val writer = PrintWriter(socket.getOutputStream(), true)
        writer.println(message)
        Log.i("sendToClient()", message)
        setUI("Sendte følgende til klienten: $message")
    }

    private fun setUI(str: String) {
        MainScope().launch { infoText.text = str }
    }

}