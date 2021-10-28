package com.oving4

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment

class PictureListFragment : ListFragment() {
    private var pictureNames: Array<String> = arrayOf()
    private var mListener: OnFragmentInteractionListener? = null

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(index: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pictureNames = resources.getStringArray(R.array.picture_names)

        listAdapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                pictureNames
            )
        }
    }

    override fun onListItemClick(
        l: ListView,
        v: View,
        position: Int,
        id: Long,
    ) {
        super.onListItemClick(l, v, position, id)

        mListener!!.onFragmentInteraction(position)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            activity as OnFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}