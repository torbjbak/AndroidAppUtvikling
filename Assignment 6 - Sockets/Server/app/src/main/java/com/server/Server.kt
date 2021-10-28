package com.server

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server(
	private val infoText: TextView,
	private val updateList: (input: String) -> Unit,
	private val PORT: Int = 12345) {

	fun start() {
		CoroutineScope(IO).launch {
			try {
				setUI("Starter Tjener ...")
				ServerSocket(PORT).use { serverSocket: ServerSocket ->
					delay(2000)
					setUI("ServerSocket opprettet, venter på at en klient kobler seg til....")
					Log.i("serverSocket", serverSocket.toString())

					while(true) {
						try {
							val clientSocket = serverSocket.accept()
							setUI("En Klient koblet seg til:\n$clientSocket")
							Log.i("clientSocket", " $clientSocket")
							ClientHandler(clientSocket, updateList, infoText).start()

						} catch (e: Exception) {
							e.printStackTrace()
							setUI(e.message.toString())
						}
					}
				}
			} catch (e: IOException) {
				e.printStackTrace()
				setUI(e.message.toString())
			}
		}
	}

	private fun setUI(str: String) {
		MainScope().launch { infoText.text = str }
	}
}
