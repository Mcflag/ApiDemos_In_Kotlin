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

class FragmentDialogOrActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dialog_or_activity)

        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            val newFragment = MyDialogFragment.newInstance()
            ft.add(R.id.embedded, newFragment)
            ft.commit()
        }
        val button = findViewById<View>(R.id.show_dialog) as Button
        button.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val newFragment = MyDialogFragment.newInstance()
        newFragment.show(supportFragmentManager, "dialog")
    }

    class MyDialogFragment : DialogFragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.hello_world, container, false)
            val tv = v.findViewById<View>(R.id.text)
            (tv as TextView).text = "This is an instance of MyDialogFragment"
            return v
        }

        companion object {
            internal fun newInstance(): MyDialogFragment {
                return MyDialogFragment()
            }
        }
    }
}