package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.fragment_menu.*

class FragmentMenu : AppCompatActivity() {

    private var mFragment1: Fragment? = null
    private var mFragment2: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_menu)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        mFragment1 = fm.findFragmentByTag("f1")
        if (mFragment1 == null) {
            mFragment1 = MenuFragment()
            ft.add(mFragment1!!, "f1")
        }
        mFragment2 = fm.findFragmentByTag("f2")
        if (mFragment2 == null) {
            mFragment2 = Menu2Fragment()
            ft.add(mFragment2!!, "f2")
        }
        ft.commit()

        menu1.setOnClickListener{
            updateFragmentVisibility()
        }
        menu2.setOnClickListener{
            updateFragmentVisibility()
        }

        updateFragmentVisibility()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        updateFragmentVisibility()
    }

    private fun updateFragmentVisibility() {
        val ft = supportFragmentManager.beginTransaction()
        if (menu1.isChecked)
            ft.show(mFragment1!!)
        else
            ft.hide(mFragment1!!)

        if (menu2.isChecked)
            ft.show(mFragment2!!)
        else
            ft.hide(mFragment2!!)
        ft.commit()
    }

    class MenuFragment : Fragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            menu.add("Menu 1a").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            menu.add("Menu 1b").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }

    class Menu2Fragment : Fragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            menu.add("Menu 2").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }
}