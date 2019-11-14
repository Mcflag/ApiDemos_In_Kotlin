package com.ccooy.apidemos.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.save_restore_state.*

class SaveRestoreState : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_restore_state)
        msg.setText(R.string.save_restore_msg)
    }

    fun getSavedText(): CharSequence {
        return saved.text
    }

    fun setSavedText(text: CharSequence) {
        saved.setText(text)
    }
}