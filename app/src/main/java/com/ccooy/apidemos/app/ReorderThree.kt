package com.ccooy.apidemos.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.reorder_three.*

class ReorderThree : AppCompatActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        setContentView(R.layout.reorder_three)

        reorder_launch_four.setOnClickListener{
            startActivity(Intent(this@ReorderThree, ReorderFour::class.java))
        }
    }
}