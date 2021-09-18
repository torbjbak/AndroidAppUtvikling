package com.oving4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class PictureViewFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        return inflater.inflate(
            R.layout.fragment_picture,
            container,
            false
        )
    }

    fun setPicture(index: Int) {
        val imageArray = resources.obtainTypedArray(R.array.pictures)
        val descriptionArray = resources.getStringArray(R.array.picture_descriptions)

        requireView()
            .findViewById<ImageView>(R.id.imageView)
            .setImageDrawable(imageArray.getDrawable(index))

        requireView()
            .findViewById<TextView>(R.id.pictureDescription)
            .text = descriptionArray[index]
    }

    fun getNext(): Int {
        val descriptionArray = resources.getStringArray(R.array.picture_descriptions)
        val desc = requireView().findViewById<TextView>(R.id.pictureDescription).text
        val last = descriptionArray.size - 1

        for(i in (0..last)) {
            if(descriptionArray[i] == desc) {
                if(i == last) {
                    return 0
                }
                return i + 1
            }
        }
        return -1
    }

    fun getPrevious(): Int {
        val descriptionArray = resources.getStringArray(R.array.picture_descriptions)
        val desc = requireView().findViewById<TextView>(R.id.pictureDescription).text
        val last = descriptionArray.size - 1

        for(i in (0..last)) {
            if(descriptionArray[i] == desc) {
                if(i == 0) {
                    return last
                }
                return i - 1
            }
        }
        return -1
    }
}