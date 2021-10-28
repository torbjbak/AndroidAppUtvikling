package com.client

import android.text.Editable
import android.text.TextWatcher
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
	private val updateChat: (input: String) -> Unit,
	private val name: String,
	private val SERVER_IP: String = "10.0.2.2",
	private val SERVER_PORT: Int = 12345) {

	fun start() {
		initMessageButton()

		CoroutineScope(IO).launch {
			setUI("Kobler til tjener...")
			try {
				Socket(SERVER_IP, SERVER_PORT).use { clientSocket: Socket ->
					setUI("Koblet til tjener: $clientSocket")
					delay(1000)
					sendToServer(clientSocket, "nameID][$name")
				}
			} catch (e: IOException) {
				e.printStackTrace()
				setUI(e.message.toString())
			}
		}
	}

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
				try {
					Socket(SERVER_IP, SERVER_PORT).use { clientSocket: Socket ->
						sendToServer(clientSocket, "$name][${messageInput.text}")
					}
					clearInput()
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
		if(message.length >= 10 && message.slice(0..10) == "Velkommen, ")
			setUI("Melding fra tjeneren:\n$message")
		else {
			updateChat(message)
		}
	}

	private fun sendToServer(socket: Socket, message: String) {
		val writer = PrintWriter(socket.getOutputStream(), true)
		writer.println(message)
		setUI("Sendte følgende til tjeneren: \n\"$message\"")
		readFromServer(socket)
	}

	private fun setUI(str: String) {
		MainScope().launch { infoText.text = str }
	}

	private fun clearInput() {
		MainScope().launch { messageInput.setText("") }
	}
}
