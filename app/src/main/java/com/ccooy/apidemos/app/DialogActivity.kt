package com.ccooy.apidemos.app

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.dialog_activity.*

class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_LEFT_ICON)
        setContentView(R.layout.dialog_activity)
        window.setTitle("This is just a test")
        window.setFeatureDrawableResource(
            Window.FEATURE_LEFT_ICON,
            android.R.drawable.ic_dialog_alert
        )
        add.setOnClickListener {
            val iv = ImageView(this)
            iv.setImageDrawable(resources.getDrawable(R.drawable.icon48x48_1))
            iv.setPadding(4, 4, 4, 4)
            inner_content.addView(iv)
        }
        remove.setOnClickListener {
            val num = inner_content.childCount
            if (num > 0) {
                inner_content.removeViewAt(num - 1)
            }
        }
    }
}