package com.oving7

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.oving7.databinding.ActivityMainBinding
import com.oving7.managers.MyFileManager
import com.oving7.managers.MyPreferenceManager
import com.oving7.service.Database
import org.json.JSONArray
import java.util.*

private const val FILE_ID = R.raw.films_input
private const val OUT_FILE_NAME = "films_output"

class MainActivity : AppCompatActivity() {
    private val settingsRequestCode = 1
    private lateinit var mainLayout: ActivityMainBinding
    private lateinit var fileManager: MyFileManager
    private lateinit var preferenceManager: MyPreferenceManager
    private lateinit var database: Database
    private var inputFile = ""
    private var color = R.color.white

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainLayout.root)

        initPreferenceManager()
        initFileManager()
        database = Database(this, JSONArray(inputFile))
    }

    private fun initFileManager() {
        fileManager = MyFileManager(this)
        inputFile = fileManager.readFileFromResFolder(FILE_ID)
        fileManager.createFile(inputFile, OUT_FILE_NAME)
    }

    private fun initPreferenceManager() {
        preferenceManager = MyPreferenceManager(this)
        preferenceManager.updateColorTheme()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "All directors")
        menu.add(0, 2, 0, "All films")
        menu.add(0, 3, 0, "All actors")
        menu.add(0, 4, 0, "All directors and films")
        menu.add(0, 5, 0, "Films by Denis Villeneuve")
        menu.add(0, 6, 0, "Actors in \"Blade Runner 2049\"")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent("com.oving7.SettingsActivity")
                intent.putExtra("color", R.color.white)
                startActivityForResult(intent, settingsRequestCode)
            }
            1             -> showResults(database.allDirectors)
            2             -> showResults(database.allFilms)
            3             -> showResults(database.allActors)
            4             -> showResults(database.allDirectorsAndFilms)
            5             -> showResults(database.getFilmsByDirector("Denis Villeneuve"))
            6             -> showResults(database.getActorsByFilm("Blade Runner 2049"))
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showResults(list: ArrayList<String>) {
        val res = StringBuffer("")
        for (s in list) res.append("$s\n")
        mainLayout.resultView.text = res
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode == settingsRequestCode) {
            if (data != null) {
                mainLayout.resultView.setBackgroundResource(
                    data.getIntExtra("color", color)
                )
            }
        }
    }
}