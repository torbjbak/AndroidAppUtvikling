package com.oving3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class AddFriendActivity : Activity() {
    private var isNewFriend: Boolean = false
    private var name: String = ""
    private var date: String = ""
    private var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        isNewFriend = intent.extras!!.getBoolean("isNewFriend")
        if(!isNewFriend) {
            name = intent.extras!!.getString("name")!!
            date = intent.extras!!.getString("date")!!
            index = intent.extras!!.getInt("index")

            findViewById<TextView>(R.id.nameValue).text = name
            findViewById<TextView>(R.id.dateValue).text = date
        }

        initCancelButton()
        initSaveButton()
    }

    private fun initCancelButton() {
        val cancelButton = findViewById<Button>(R.id.cancelEditFriendButton)

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED, Intent())
            finish()
        }
    }

    private fun initSaveButton() {
        val saveButton = findViewById<Button>(R.id.saveEditFriendButton)

        saveButton.setOnClickListener {
            name = findViewById<EditText>(R.id.nameValue).text.toString()
            date = findViewById<EditText>(R.id.dateValue).text.toString()

            setResult(RESULT_OK, Intent()
                .putExtra("name", name)
                .putExtra("date", date)
                .putExtra("index", index)
            )
            finish()
        }
    }
}