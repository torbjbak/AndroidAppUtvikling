package com.server

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server(
	private val infoText: TextView,
	private val PORT: Int = 12345
) {

	private var ui: String? = ""
		set(str) {
			MainScope().launch { infoText.text = str }
			field = str
		}

	fun start() {
		CoroutineScope(Dispatchers.IO).launch {

			try {
				ui = "Starter Tjener ..."

				ServerSocket(PORT).use { serverSocket: ServerSocket ->

					ui = "ServerSocket opprettet, venter på at en klient kobler seg til...."

					serverSocket.accept().use { clientSocket: Socket ->

						ui = "En Klient koblet seg til:\n$clientSocket"

						//send tekst til klienten
						sendToClient(clientSocket, "Velkommen Klient!")

						// Hent tekst fra klienten
						readFromClient(clientSocket)
					}
				}
			} catch (e: IOException) {
				e.printStackTrace()
				ui = e.message
			}
		}
	}

	private fun readFromClient(socket: Socket) {
		val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
		val message = reader.readLine()
		Log.i("readFromClient()", message)
		ui = "Klienten sier:\n$message"
	}

	private fun sendToClient(socket: Socket, message: String) {
		val writer = PrintWriter(socket.getOutputStream(), true)
		writer.println(message)
		Log.i("sendToClient()", message)
		ui = "Sendte følgende til klienten:\n$message"
	}
}
