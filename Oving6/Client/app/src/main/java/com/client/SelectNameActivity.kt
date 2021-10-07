package com.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

class SelectNameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_name)

        initNameButton()
    }

    private fun initNameButton() {
        val nameButton = findViewById<Button>(R.id.nameButton)
        val nameInput = findViewById<EditText>(R.id.nameInput)

        nameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                nameButton.isEnabled = nameInput.text.toString() != ""
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        nameButton.setOnClickListener {
            setResult(RESULT_OK,
                Intent().putExtra("name", nameInput.text.toString())
            )
            finish()
        }
    }
}