package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.fragment_stack.*

class FragmentStack : AppCompatActivity() {
    internal var mStackLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_stack)

        new_fragment.setOnClickListener { addFragmentToStack() }
        delete_fragment.setOnClickListener { supportFragmentManager.popBackStack() }

        if (savedInstanceState == null) {
            val newFragment = CountingFragment.newInstance(mStackLevel)
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.simple_fragment, newFragment).commit()
        } else {
            mStackLevel = savedInstanceState.getInt("level")
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("level", mStackLevel)
    }


    private fun addFragmentToStack() {
        mStackLevel++

        val newFragment = CountingFragment.newInstance(mStackLevel)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.simple_fragment, newFragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.addToBackStack(null)
        ft.commit()
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
            tv.setBackgroundDrawable(resources.getDrawable(android.R.drawable.gallery_thumb))
            return v
        }

        companion object {
            internal fun newInstance(num: Int): CountingFragment {
                val f = CountingFragment()

                val args = Bundle()
                args.putInt("num", num)
                f.arguments = args

                return f
            }
        }
    }

}