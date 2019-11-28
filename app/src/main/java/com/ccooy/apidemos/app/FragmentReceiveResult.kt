package com.ccooy.apidemos.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R

class FragmentReceiveResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val frame = FrameLayout(this)
        frame.id = R.id.simple_fragment
        setContentView(frame, lp)

        if (savedInstanceState == null) {
            val newFragment = ReceiveResultFragment()
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.simple_fragment, newFragment).commit()
        }
    }

    class ReceiveResultFragment : Fragment() {

        private var mResults: TextView? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.receive_result, container, false)

            mResults = v.findViewById<View>(R.id.results) as TextView

            mResults!!.setText(mResults!!.text, TextView.BufferType.EDITABLE)

            val getButton = v.findViewById<View>(R.id.get) as Button
            getButton.setOnClickListener {
                val intent = Intent(activity, SendResult::class.java)
                startActivityForResult(intent, GET_CODE)
            }

            return v
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == GET_CODE) {

                val text = mResults!!.text as Editable

                if (resultCode == Activity.RESULT_CANCELED) {
                    text.append("(cancelled)")
                } else {
                    text.append("(okay ")
                    text.append(Integer.toString(resultCode))
                    text.append(") ")
                    if (data != null) {
                        text.append(data.action)
                    }
                }

                text.append("\n")
            }
        }

        companion object {
            private val GET_CODE = 0
        }
    }
}