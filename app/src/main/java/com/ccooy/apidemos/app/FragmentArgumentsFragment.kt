package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R

class FragmentArgumentsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val ft = childFragmentManager.beginTransaction()
            var newFragment = FragmentArguments.MyFragment.newInstance("From Arguments 1")
            ft.add(R.id.created1, newFragment)
            newFragment = FragmentArguments.MyFragment.newInstance("From Arguments 2")
            ft.add(R.id.created2, newFragment)
            ft.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_arguments_fragment, container, false)
    }

}