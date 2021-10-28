package com.server

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ServerActivity : Activity() {
    private lateinit var userListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val nameList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUserList()

        val infoText = findViewById<TextView>(R.id.infoText)
        Server(infoText, ::updateList).start()
    }

    private fun initUserList() {
        userListView = findViewById(R.id.userList)
        adapter = ArrayAdapter(
            this,
            R.layout.custom_list_item,
            nameList
        )
        userListView.adapter = adapter
        userListView.choiceMode = ListView.CHOICE_MODE_NONE
    }

    private fun updateList(input: String) {
        MainScope().launch {
            nameList.add(input)
            adapter.notifyDataSetChanged()
        }
    }
}