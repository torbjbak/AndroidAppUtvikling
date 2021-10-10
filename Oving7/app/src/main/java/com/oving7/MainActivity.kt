package com.oving7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.oving7.databinding.ActivityMainBinding
import com.oving7.managers.MyDatabaseManager
import com.oving7.managers.MyPreferenceManager
import com.oving7.managers.MyFileManager

private const val FILE_ID = R.raw.films_input
private const val OUT_FILE_NAME = "films_output"

class MainActivity : AppCompatActivity() {
    private lateinit var mainLayout: ActivityMainBinding
    private lateinit var fileManager: MyFileManager
    private lateinit var dbManager: MyDatabaseManager
    private var inputFile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainLayout.root)

        MyPreferenceManager(this).updateNightMode()
        initFileManager()
        initDbManager()
    }

    private fun initFileManager() {
        fileManager = MyFileManager(this)
        inputFile = fileManager.readFileFromResFolder(FILE_ID)
        fileManager.createFile(inputFile, OUT_FILE_NAME)
    }

    private fun initDbManager() {
        dbManager = MyDatabaseManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent("com.oving7.SettingsActivity"))
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }
}