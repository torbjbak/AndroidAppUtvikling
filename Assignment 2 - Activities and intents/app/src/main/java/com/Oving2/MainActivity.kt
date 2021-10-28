package com.Oving2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val randomRequestCode = 1
    private var randomNumber = 0
    private var maxValue = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickRandomNumberActivity(view: View?) {
        val intent = Intent("RandomNumberActivity")
        intent.putExtra("randomNumber", randomNumber)
        intent.putExtra("maxValue", maxValue)
        startActivityForResult(intent, randomRequestCode)
    }

    fun onClickArithmeticActivity(view: View?) {
        val intent = Intent("ArithmeticActivity")
        startActivity(intent)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            Log.e("onActivityResult()", "Noe gikk galt")
            return
        }
        if (requestCode == randomRequestCode) {
            maxValue = data.getIntExtra("MaxValue", maxValue)
            Log.i( "INFO","maxValue: $maxValue")
            randomNumber = data.getIntExtra("randomNumber", randomNumber)

            Toast.makeText(this, randomNumber.toString(), Toast.LENGTH_LONG).show()
            findViewById<TextView>(R.id.randomNumberResult).apply {
                text = showRandomResult()
            }
        }
    }

    private fun showRandomResult(): String {
        return "Random value: $randomNumber (max value: $maxValue)"
    }
}