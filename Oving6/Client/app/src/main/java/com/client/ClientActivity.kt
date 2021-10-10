package com.client

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ClientActivity : Activity() {
    private val selectNameRequestCode = 1
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*startActivityForResult(
            Intent("SelectNameActivity"),
            selectNameRequestCode
        )*/

        val infoText = findViewById<TextView>(R.id.infoText)
        val messageButton = findViewById<Button>(R.id.messageButton)
        val messageInput = findViewById<EditText>(R.id.messageInput)

        Client(infoText, messageButton, messageInput, name).start()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode == selectNameRequestCode) {
            name = data.getStringExtra("name").toString()
            Log.i("onActivityResult()", name)
        }
    }
}