package com.client

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ClientActivity : Activity() {
    private val selectNameRequestCode = 1
    private lateinit var infoText: TextView
    private lateinit var messageButton: Button
    private lateinit var messageInput: EditText
    private lateinit var chatListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val chatList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infoText = findViewById(R.id.infoText)
        messageButton = findViewById(R.id.messageButton)
        messageInput = findViewById(R.id.messageInput)

        initUserList()

        startActivityForResult(
            Intent("SelectNameActivity"),
            selectNameRequestCode
        )
    }

    private fun initUserList() {
        chatListView = findViewById(R.id.chatList)

        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            chatList
        )

        chatListView.adapter = adapter
        chatListView.choiceMode = ListView.CHOICE_MODE_NONE
    }

    private fun updateChat(input: String) {
        MainScope().launch {
            chatList.add(input)
            adapter.notifyDataSetChanged()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode == selectNameRequestCode) {
            val name = data.getStringExtra("name").toString()
            Log.i("onActivityResult()", name)
            Client(infoText, messageButton, messageInput, ::updateChat, name).start()
        }
    }
}