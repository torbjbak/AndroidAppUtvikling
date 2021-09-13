package com.oving3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class MainActivity : Activity() {
    private val addFriendRequestCode = 1
    private val editFriendRequestCode = 2
    private var nameList: MutableList<String> = mutableListOf()
    private var dateList: MutableList<String> = mutableListOf()
    private lateinit var friendListView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameList.add("Torbjørn Bakke")
        dateList.add("16/05/1993")

        initAddFriendButton()
        initFriendList()

        Log.i("INFO", nameList.toString())
        Log.i("INFO", dateList.toString())
    }

    private fun initAddFriendButton() {
        val addFriendButton = findViewById<Button>(R.id.addFriendButton)
        val intent = Intent("AddFriendActivity")
        intent.putExtra("isNewFriend", true)

        addFriendButton.setOnClickListener {
            startActivityForResult(intent, addFriendRequestCode)
        }
    }

    private fun initFriendList() {
        friendListView = findViewById(R.id.friendList)

        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_activated_1, nameList)

        friendListView.adapter = adapter
        friendListView.choiceMode = ListView.CHOICE_MODE_SINGLE

        friendListView.onItemClickListener =
            AdapterView.OnItemClickListener {
                    _: AdapterView<*>?,
                    _: View,
                    position: Int,
                    _: Long ->
                startActivityForResult(
                    Intent("AddFriendActivity")
                        .putExtra("isNewFriend", false)
                        .putExtra("name", nameList[position])
                        .putExtra("date", dateList[position])
                        .putExtra("index", position),
                    editFriendRequestCode
                )
            }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            return
        }

        val index = data.getIntExtra("index", -1)
        if (requestCode == addFriendRequestCode) {
            nameList.add(data.getStringExtra("name")!!)
            dateList.add(data.getStringExtra("date")!!)
        } else if(requestCode == editFriendRequestCode && index >= 0) {
            nameList[index] = data.getStringExtra("name")!!
            dateList[index] = data.getStringExtra("date")!!
        }
        adapter.notifyDataSetChanged()
    }
}