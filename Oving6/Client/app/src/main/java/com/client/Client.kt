package com.client

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client(
	private val infoText: TextView,
	private val messageButton: Button,
	private val messageInput: EditText,
	private val name: String,
	private val SERVER_IP: String = "10.0.2.2",
	private val SERVER_PORT: Int = 12345,
) {

	fun start() {
		initMessageButton()
	}

	/*fun start() {
		initMessageButton()

		CoroutineScope(IO).launch {
			ui = "Kobler til tjener..."
			try {
				Socket(SERVER_IP, SERVER_PORT).use { socket: Socket ->
					ui = "Koblet til tjener:\n$socket"
				}
			} catch (e: IOException) {
				e.printStackTrace()
				ui = e.message
			}
		}
	}*/

	private fun initMessageButton() {
		messageInput.addTextChangedListener(object: TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				messageButton.isEnabled = messageInput.text.toString() != ""
			}
			override fun afterTextChanged(p0: Editable?) {}
		})

		messageButton.setOnClickListener {
			CoroutineScope(IO).launch {
				setUI("Kobler til tjener...")
				try {
					Socket(SERVER_IP, SERVER_PORT).use { socket: Socket ->
						setUI("Koblet til tjener:\n$socket")
						delay(1000)
						readFromServer(socket)
						delay(1000)
						sendToServer(socket, messageInput.text.toString())
					}
				} catch (e: IOException) {
					e.printStackTrace()
					setUI(e.message.toString())
				}
			}
		}
	}

	private fun readFromServer(socket: Socket) {
		val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
		val message = reader.readLine()
		Log.i("readFromServer()", "Message: $message")
		setUI("Melding fra tjeneren:\n$message")
	}

	private fun sendToServer(socket: Socket, message: String) {
		val writer = PrintWriter(socket.getOutputStream(), true)
		writer.println("$name: $message")
		Log.i("sendToServer()", "$name: $message")
		setUI("Sendte følgende til tjeneren: \n\"$name: $message\"")
	}

	private fun setUI(str: String) {
		MainScope().launch { infoText.text = str }
	}
}
