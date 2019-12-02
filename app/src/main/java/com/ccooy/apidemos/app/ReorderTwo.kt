package com.ccooy.apidemos.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.reorder_two.*

class ReorderTwo : AppCompatActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        setContentView(R.layout.reorder_two)

        reorder_launch_three.setOnClickListener{
            startActivity(Intent(this@ReorderTwo, ReorderThree::class.java))
        }
    }
}