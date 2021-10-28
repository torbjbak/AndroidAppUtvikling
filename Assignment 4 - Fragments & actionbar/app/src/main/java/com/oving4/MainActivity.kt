package com.oving4

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(),
    PictureListFragment.OnFragmentInteractionListener {
    private lateinit var pictureFragment: PictureViewFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pictureFragment = supportFragmentManager
            .findFragmentById(R.id.picture_fragment) as PictureViewFragment
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_next -> pictureFragment.setPicture(pictureFragment.getNext())
            R.id.menu_previous -> pictureFragment.setPicture(pictureFragment.getPrevious())
            R.id.menu_exit -> finish()
            else -> return false
        }
        return true
    }

    override fun onFragmentInteraction(index: Int) {
        pictureFragment.setPicture(index)
    }
}