package com.ccooy.apidemos.app

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.redirect_getter.*

class RedirectGetter : AppCompatActivity() {
    private var mTextPref: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.redirect_getter)
        bt_apply.setOnClickListener {
            val preferences = getSharedPreferences("RedirectData", 0)
            val editor = preferences.edit()
            editor.putString("text", text.text.toString())

            if (editor.commit()) {
                setResult(Activity.RESULT_OK)
            }

            finish()
        }

        loadPrefs()
    }

    private fun loadPrefs() {
        val preferences = getSharedPreferences("RedirectData", 0)

        mTextPref = preferences.getString("text", null)
        if (mTextPref != null) {
            text.setText(mTextPref)
        } else {
            text.setText("")
        }
    }
}