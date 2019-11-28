package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R

class FragmentMenuFragment : Fragment() {
    private var mFragment1: Fragment? = null
    private var mFragment2: Fragment? = null
    private var menu1:CheckBox? = null
    private var menu2:CheckBox? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_menu, container, false)

        val fm = childFragmentManager
        val ft = fm.beginTransaction()
        mFragment1 = fm.findFragmentByTag("f1")
        if (mFragment1 == null) {
            mFragment1 = FragmentMenu.MenuFragment()
            ft.add(mFragment1!!, "f1")
        }
        mFragment2 = fm.findFragmentByTag("f2")
        if (mFragment2 == null) {
            mFragment2 = FragmentMenu.Menu2Fragment()
            ft.add(mFragment2!!, "f2")
        }
        ft.commit()

        menu1 = v.findViewById(R.id.menu1)
        menu1?.setOnClickListener { updateFragmentVisibility() }
        menu2 = v.findViewById(R.id.menu2)
        menu2?.setOnClickListener { updateFragmentVisibility() }

        updateFragmentVisibility()

        return v
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        updateFragmentVisibility()
    }

    private fun updateFragmentVisibility() {
        val ft = childFragmentManager.beginTransaction()
        if (menu1!!.isChecked)
            ft.show(mFragment1!!)
        else
            ft.hide(mFragment1!!)
        if (menu2!!.isChecked)
            ft.show(mFragment2!!)
        else
            ft.hide(mFragment2!!)
        ft.commit()
    }
}