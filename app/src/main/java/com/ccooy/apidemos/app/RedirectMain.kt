package com.ccooy.apidemos.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.redirect_main.*

class RedirectMain : AppCompatActivity() {

    private var mTextPref: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.redirect_main)
        clear.setOnClickListener {
            val preferences = getSharedPreferences("RedirectData", 0)
            preferences.edit().remove("text").commit()
            finish()
        }
        newView.setOnClickListener {
            val intent = Intent(this@RedirectMain, RedirectGetter::class.java)
            startActivityForResult(intent, NEW_TEXT_REQUEST)
        }
        if (!loadPrefs()) {
            val intent = Intent(this, RedirectGetter::class.java)
            startActivityForResult(intent, INIT_TEXT_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INIT_TEXT_REQUEST) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else {
                loadPrefs()
            }
        } else if (requestCode == NEW_TEXT_REQUEST) {
            if (resultCode != Activity.RESULT_CANCELED) {
                loadPrefs()
            }

        }
    }

    private fun loadPrefs(): Boolean {
        val preferences = getSharedPreferences("RedirectData", 0)

        mTextPref = preferences.getString("text", null)
        if (mTextPref != null) {
            text.text = mTextPref
            return true
        }

        return false
    }

    companion object {
        internal const val INIT_TEXT_REQUEST = 0
        internal const val NEW_TEXT_REQUEST = 1
    }
}