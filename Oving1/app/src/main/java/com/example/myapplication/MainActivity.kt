package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var personalName = ""
    private var familyName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun writePersonalName(view: View) {
        personalName = "Torbjørn"

        findViewById<TextView>(R.id.nameDisplay).apply {
            text = getName()
        }

        Log.d("WARNING", personalName)
    }

    fun writeFamilyName(view: View) {
        familyName = "Bakke"

        findViewById<TextView>(R.id.nameDisplay).apply {
            text = getName()
        }

        Log.d("ERROR", familyName)
    }

    private fun getName(): String = "$personalName $familyName"
}