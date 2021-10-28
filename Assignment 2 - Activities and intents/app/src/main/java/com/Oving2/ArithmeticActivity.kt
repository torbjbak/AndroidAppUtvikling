package com.Oving2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

class ArithmeticActivity : Activity() {
    private val randomNumberCode1 = 1
    private val randomNumberCode2 = 2
    private var number1 = 3
    private var number2 = 5
    private var userAnswer = 8
    private var maxValue = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arithmetic)

        findViewById<TextView>(R.id.number1).text = number1.toString()
        findViewById<TextView>(R.id.number2).text = number2.toString()
        findViewById<TextView>(R.id.userAnswer).text = userAnswer.toString()
        findViewById<TextView>(R.id.maxValue).text = maxValue.toString()
    }

    fun onClickAdd(view: View) {
        updateValues()
        val answer = number1 + number2
        checkAnswer(answer)
        Thread.sleep(2000)
        getRandom1()
        getRandom2()
    }

    fun onClickMultiply(view: View) {
        updateValues()
        val answer = number1 * number2
        checkAnswer(answer)
        Thread.sleep(2000)
        getRandom1()
        getRandom2()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            Log.e("onActivityResult()", "Noe gikk galt")
            return
        }
        if (requestCode == randomNumberCode1) {
            number1 = data.getIntExtra("randomNumber", number1)
            Log.i( "INFO","number1: $number1")
            findViewById<TextView>(R.id.number1).text = number1.toString()
        }
        if (requestCode == randomNumberCode2) {
            number2 = data.getIntExtra("randomNumber", number2)
            Log.i( "INFO","number2: $number2")
            findViewById<TextView>(R.id.number2).text = number2.toString()
        }
    }

    private fun updateValues() {
        userAnswer = findViewById<TextView>(R.id.userAnswer).text.toString().toInt()
        maxValue = findViewById<TextView>(R.id.maxValue).text.toString().toInt()
    }

    private fun checkAnswer(answer: Int) {
        if(answer == userAnswer) {
            Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_LONG).show()
        } else {
            val errorString = "${getString(R.string.wrong)} $answer"
            Toast.makeText(this, errorString, Toast.LENGTH_LONG).show()
        }
    }

    private fun getRandom1() {
        val intent = Intent("RandomNumberActivity")
        intent.putExtra("randomNumber", number1)
        intent.putExtra("maxValue", maxValue)
        startActivityForResult(intent, randomNumberCode1)
    }
    private fun getRandom2() {
        val intent = Intent("RandomNumberActivity")
        intent.putExtra("randomNumber", number2)
        intent.putExtra("maxValue", maxValue)
        startActivityForResult(intent, randomNumberCode2)
    }

    fun onClickReturn(view: View?) {
        finish()
    }
}