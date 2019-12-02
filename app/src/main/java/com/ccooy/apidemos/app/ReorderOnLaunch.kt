package com.ccooy.apidemos.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.reorder_on_launch.*

class ReorderOnLaunch : AppCompatActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        setContentView(R.layout.reorder_on_launch)

        reorder_launch_two.setOnClickListener{
            startActivity(Intent(this@ReorderOnLaunch, ReorderTwo::class.java))
        }
    }
}