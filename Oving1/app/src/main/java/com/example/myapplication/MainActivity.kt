package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun writePersonalName(view: View) {
        Log.d("INFO", "Personal name!")
    }

    fun writeFamilyName(view: View) {
        Log.d("INFO", "Family name!")
    }
}