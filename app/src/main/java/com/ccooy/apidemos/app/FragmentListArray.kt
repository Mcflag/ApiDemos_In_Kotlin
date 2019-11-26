package com.ccooy.apidemos.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import com.ccooy.apidemos.Shakespeare

class FragmentListArray : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
            val list = ArrayListFragment()
            supportFragmentManager.beginTransaction().add(android.R.id.content, list).commit()
        }
    }

    class ArrayListFragment : ListFragment() {
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            listAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_list_item_1, Shakespeare.TITLES
            )
        }

        override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
            Log.i("FragmentList", "Item clicked: $id")
        }
    }
}