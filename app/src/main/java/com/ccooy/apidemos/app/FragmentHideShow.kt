package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ccooy.apidemos.R

class FragmentHideShow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_hide_show)

        val fragment1 = FirstFragment()
        val fragment2 = SecondFragment()

        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.fragment1, fragment1)
            ft.add(R.id.fragment2, fragment2)
            ft.commit()
        }

        addShowHideListener(R.id.frag1hide, fragment1)
        addShowHideListener(R.id.frag2hide, fragment2)
    }

    private fun addShowHideListener(buttonId: Int, fragment: Fragment) {
        val button = findViewById<Button>(buttonId)
        button.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            if (fragment.isHidden) {
                ft.show(fragment)
                button.text = "Hide"
            } else {
                ft.hide(fragment)
                button.text = "Show"
            }
            ft.commit()
        }
    }

    class FirstFragment : Fragment() {
        private lateinit var saved: TextView

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.labeled_text_edit, container, false)
            val msg = v.findViewById(R.id.msg) as TextView
            msg.text = "The fragment saves and restores this text."
            saved = v.findViewById(R.id.saved) as TextView
            if (savedInstanceState != null) {
                saved.text = savedInstanceState.getCharSequence("text")
            }
            return v
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putCharSequence("text", saved.text)
        }
    }

    class SecondFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.labeled_text_edit, container, false)
            val msg = v.findViewById(R.id.msg) as TextView
            msg.text = "The TextView saves and restores this text."
            val saved = v.findViewById(R.id.saved) as TextView
            saved.isSaveEnabled = true
            return v
        }
    }
}