package com.ccooy.apidemos.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBar.Tab
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentNestingTabs : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        FragmentManager.enableDebugLogging(true)
        super.onCreate(savedInstanceState)
        val bar = supportActionBar
        bar!!.navigationMode = ActionBar.NAVIGATION_MODE_TABS
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE)
        bar.addTab(
            bar.newTab()
                .setText("Menus")
                .setTabListener(
                    TabListener<FragmentMenuFragment>(
                        this, "menus", FragmentMenuFragment::class.java
                    )
                )
        )
        bar.addTab(
            bar.newTab()
                .setText("Args")
                .setTabListener(
                    TabListener<FragmentArgumentsFragment>(
                        this, "args", FragmentArgumentsFragment::class.java
                    )
                )
        )
        bar.addTab(
            bar.newTab()
                .setText("Stack")
                .setTabListener(
                    TabListener<FragmentStackFragment>(
                        this, "stack", FragmentStackFragment::class.java
                    )
                )
        )
        bar.addTab(
            bar.newTab()
                .setText("Tabs")
                .setTabListener(
                    TabListener<FragmentTabsFragment>(
                        this, "tabs", FragmentTabsFragment::class.java
                    )
                )
        )

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0))
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("tab", supportActionBar!!.selectedNavigationIndex)
    }

    class TabListener<T : Fragment> @JvmOverloads constructor(
        private val mActivity: AppCompatActivity,
        private val mTag: String,
        private val mClass: Class<T>,
        private val mArgs: Bundle? = null
    ) : ActionBar.TabListener {
        private var mFragment: Fragment? = null

        init {
            mFragment = mActivity.supportFragmentManager.findFragmentByTag(mTag)
            if (mFragment != null && !mFragment!!.isDetached) {
                val ft = mActivity.supportFragmentManager.beginTransaction()
                ft.detach(mFragment!!)
                ft.commit()
            }
        }

        override fun onTabSelected(tab: Tab, ft: FragmentTransaction) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.name, mArgs)
                ft.add(android.R.id.content, mFragment!!, mTag)
            } else {
                ft.attach(mFragment!!)
            }
        }

        override fun onTabUnselected(tab: Tab, ft: FragmentTransaction) {
            if (mFragment != null) {
                ft.detach(mFragment!!)
            }
        }

        override fun onTabReselected(tab: Tab, ft: FragmentTransaction) {
            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show()
        }
    }
}