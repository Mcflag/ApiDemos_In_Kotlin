package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ccooy.apidemos.R

class FragmentStackFragment : Fragment() {
    private var mStackLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val newFragment = FragmentStack.CountingFragment.newInstance(mStackLevel)
            val ft = childFragmentManager.beginTransaction()
            ft.add(R.id.simple_fragment, newFragment).commit()
        } else {
            mStackLevel = savedInstanceState.getInt("level")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_stack, container, false)

        var button = v.findViewById<View>(R.id.new_fragment) as Button
        button.setOnClickListener { addFragmentToStack() }
        button = v.findViewById<View>(R.id.delete_fragment) as Button
        button.setOnClickListener { childFragmentManager.popBackStack() }

        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("level", mStackLevel)
    }

    private fun addFragmentToStack() {
        mStackLevel++

        val newFragment = FragmentStack.CountingFragment.newInstance(mStackLevel)

        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.simple_fragment, newFragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.addToBackStack(null)
        ft.commit()
    }
}