package com.Oving2

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class RandomNumberActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val maxValue = intent.extras?.getInt("maxValue")
        val randomNumber = (0..maxValue!!).random()

        setResult(RESULT_OK, Intent()
            .putExtra("randomNumber", randomNumber)
        )
        finish()
    }
}