package com.server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ServerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val infoText = findViewById<TextView>(R.id.infoText)
        Server(infoText).start()
    }
}