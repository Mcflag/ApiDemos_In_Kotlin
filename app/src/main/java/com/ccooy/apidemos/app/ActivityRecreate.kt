package com.ccooy.apidemos.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.activity_recreate.*

class ActivityRecreate : AppCompatActivity() {

    private var mCurTheme: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            mCurTheme = savedInstanceState.getInt("theme")

            mCurTheme = when (mCurTheme) {
                R.style.Theme_AppCompat_Light -> R.style.Theme_AppCompat_Light_Dialog
                R.style.Theme_AppCompat_Light_Dialog -> R.style.ThemeOverlay_AppCompat_Dark
                else -> R.style.Theme_AppCompat_Light
            }
            setTheme(mCurTheme)
        }

        setContentView(R.layout.activity_recreate)
        recreate.setOnClickListener {
            recreate()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("theme", mCurTheme)
    }
}