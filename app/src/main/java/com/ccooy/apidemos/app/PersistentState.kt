package com.ccooy.apidemos.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.save_restore_state.*

class PersistentState : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_restore_state)
        msg.setText(R.string.persistent_msg)
    }

    override fun onResume() {
        super.onResume()

        val prefs = getPreferences(0)
        val restoredText = prefs.getString("text", null)
        if (restoredText != null) {
            saved.setText(restoredText, TextView.BufferType.EDITABLE)

            val selectionStart = prefs.getInt("selection-start", -1)
            val selectionEnd = prefs.getInt("selection-end", -1)
            if (selectionStart != -1 && selectionEnd != -1) {
                saved.setSelection(selectionStart, selectionEnd)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        val editor = getPreferences(0).edit()
        editor.putString("text", saved.text.toString())
        editor.putInt("selection-start", saved.selectionStart)
        editor.putInt("selection-end", saved.selectionEnd)
        editor.commit()
    }
}