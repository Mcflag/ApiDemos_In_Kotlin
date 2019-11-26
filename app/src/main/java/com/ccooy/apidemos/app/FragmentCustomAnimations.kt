package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.fragment_stack.*

class FragmentCustomAnimations : AppCompatActivity() {
    private var mStackLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_stack)
        new_fragment.setOnClickListener {
            addFragmentToStack()
        }
        delete_fragment.setOnClickListener {
            minusFragmentToStack()
        }

        if (savedInstanceState == null) {
            val newFragment = CountingFragment.newInstance(mStackLevel)
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.simple_fragment, newFragment).commit()
        } else {
            mStackLevel = savedInstanceState.getInt("level")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("level", mStackLevel)
    }

    private fun addFragmentToStack() {
        mStackLevel++
        val newFragment = CountingFragment.newInstance(mStackLevel)
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(
            R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit
        )
        ft.replace(R.id.simple_fragment, newFragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun minusFragmentToStack() {
        if (mStackLevel == 1) {
            return
        }
        onBackPressed()
    }

    class CountingFragment : Fragment() {
        private var mNum: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            mNum = if (arguments != null) arguments!!.getInt("num") else 1
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.hello_world, container, false)
            val tv = v.findViewById<View>(R.id.text)
            (tv as TextView).text = "Fragment #$mNum"
            tv.setBackground(resources.getDrawable(android.R.drawable.gallery_thumb))
            return v
        }

        companion object {
            fun newInstance(num: Int): CountingFragment {
                val f = CountingFragment()
                val args = Bundle()
                args.putInt("num", num)
                f.arguments = args
                return f
            }
        }
    }
}