package com.oving5

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val URL = "https://bigdata.idi.ntnu.no/mobil/tallspill.jsp"

class MainActivity : AppCompatActivity() {
    private val network: HttpWrapper = HttpWrapper(URL)
    private lateinit var nameInfo: TextView
    private lateinit var cardInfo: TextView
    private lateinit var gameInfo: TextView
    private lateinit var nameInput: EditText
    private lateinit var cardInput: EditText
    private lateinit var gameInput: EditText
    private lateinit var startButton: Button
    private lateinit var gameButton: Button
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameInfo = findViewById(R.id.nameTextView)
        cardInfo = findViewById(R.id.cardTextView)
        gameInfo = findViewById(R.id.gameInfo)
        nameInput = findViewById(R.id.editTextName)
        cardInput = findViewById(R.id.editTextCard)
        gameInput = findViewById(R.id.gameInput)
        startButton = findViewById(R.id.startButton)
        gameButton = findViewById(R.id.playButton)

        initStartButton()
        initPlayButton()
    }

    private fun initStartButton() {
        startButton.isEnabled = false

        nameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                startButton.isEnabled =
                    !(nameInput.text.toString() == "" || cardInput.text.toString() == "")
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        cardInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                startButton.isEnabled =
                    !(nameInput.text.toString() == "" || cardInput.text.toString() == "")
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        startButton.setOnClickListener {
            name = nameInput.text.toString()

            CoroutineScope(IO).launch {
                val response = performRequest(
                    startParameters(name, cardInput.text.toString())
                )

                MainScope().launch {
                    gameInfo.text = response

                    if(!response.contains("cookies")) {
                        nameInfo.isGone = true
                        cardInfo.isGone = true
                        nameInput.isGone = true
                        cardInput.isGone = true
                        startButton.isGone = true

                        gameInput.isGone = false
                        gameButton.isGone = false
                    }
                }
            }
        }
    }

    private fun initPlayButton() {
        gameButton.isEnabled = false

        gameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                gameButton.isEnabled =
                    gameInput.text.toString() != ""
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        gameButton.setOnClickListener {
            CoroutineScope(IO).launch {
                val response: String = try {
                    network.get(playParameters(gameInput.text.toString()))
                } catch (e: Exception) {
                    Log.e("performRequest()", e.message!!)
                    e.toString()
                }

                MainScope().launch {
                    gameInfo.text = response

                    if(response.contains("Beklager")
                        || response.contains("$name,")
                        || response.contains("cookies")) {
                        gameInput.isGone = true
                        gameButton.isGone = true

                        nameInfo.isGone = false
                        cardInfo.isGone = false
                        nameInput.isGone = false
                        cardInput.isGone = false
                        startButton.isGone = false
                    }
                }
            }
        }
    }

    private fun startParameters(name: String, card: String): Map<String, String> {
        return mapOf(
            "navn" to name,
            "kortnummer" to card,
        )
    }

    private fun playParameters(number: String): Map<String, String> {
        return mapOf(
            "tall" to number,
        )
    }

    private fun performRequest(parameterList: Map<String, String>): String {
        val response = try {
            network.get(parameterList)
        } catch (e: Exception) {
            Log.e("performRequest()", e.message!!)
            e.toString()
        }
        return response
    }
}