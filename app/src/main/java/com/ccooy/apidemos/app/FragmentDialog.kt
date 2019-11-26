package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ccooy.apidemos.R

class FragmentDialog : AppCompatActivity() {
    private var mStackLevel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dialog)

        val tv = findViewById<View>(R.id.text)
        (tv as TextView).text = ("Example of displaying dialogs with a DialogFragment.  "
                + "Press the show button below to see the first dialog; pressing "
                + "successive show buttons will display other dialog styles as a "
                + "stack, with dismissing or back going to the previous dialog.")

        val button = findViewById<View>(R.id.show) as Button
        button.setOnClickListener { showDialog() }

        if (savedInstanceState != null) {
            mStackLevel = savedInstanceState.getInt("level")
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("level", mStackLevel)
    }

    internal fun showDialog() {
        mStackLevel++

        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val newFragment = MyDialogFragment.newInstance(mStackLevel)
        newFragment.show(ft, "dialog")
    }

    class MyDialogFragment : DialogFragment() {
        private var mNum: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            mNum = arguments?.getInt("num") ?: 0

            var style = STYLE_NORMAL
            var theme = 0
            when ((mNum - 1) % 9) {
                1 -> style = STYLE_NO_TITLE
                2 -> style = STYLE_NO_FRAME
                3 -> style = STYLE_NO_INPUT
                4 -> style = STYLE_NORMAL
                5 -> style = STYLE_NORMAL
                6 -> style = STYLE_NO_TITLE
                7 -> style = STYLE_NO_FRAME
                8 -> style = STYLE_NORMAL
            }
            when ((mNum - 1) % 9) {
                4 -> theme = R.style.Theme_AppCompat
                5 -> theme = R.style.Theme_AppCompat_Light_Dialog
                6 -> theme = R.style.Theme_AppCompat_Light
                7 -> theme = android.R.style.Theme_Holo_Light_Panel
                8 -> theme = R.style.Theme_AppCompat_Light
            }
            setStyle(style, theme)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.fragment_dialog, container, false)
            val tv = v.findViewById<View>(R.id.text)
            (tv as TextView).text = ("Dialog #" + mNum + ": using style " + getNameForNum(mNum))

            val button = v.findViewById<View>(R.id.show) as Button
            button.setOnClickListener {
                (activity as FragmentDialog).showDialog()
            }

            return v
        }

        companion object {
            internal fun newInstance(num: Int): MyDialogFragment {
                val f = MyDialogFragment()
                val args = Bundle()
                args.putInt("num", num)
                f.arguments = args

                return f
            }
        }
    }

    companion object {
        internal fun getNameForNum(num: Int): String {
            when ((num - 1) % 9) {
                1 -> return "STYLE_NO_TITLE"
                2 -> return "STYLE_NO_FRAME"
                3 -> return "STYLE_NO_INPUT (this window can't receive input, so you will need to press the bottom show button)"
                4 -> return "STYLE_NORMAL with dark fullscreen theme"
                5 -> return "STYLE_NORMAL with light theme"
                6 -> return "STYLE_NO_TITLE with light theme"
                7 -> return "STYLE_NO_FRAME with light theme"
                8 -> return "STYLE_NORMAL with light fullscreen theme"
            }
            return "STYLE_NORMAL"
        }
    }
}