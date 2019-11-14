package com.ccooy.apidemos.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.activity_finish_affinity.*

class FinishAffinity : AppCompatActivity() {

    private var mNesting: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_finish_affinity)

        mNesting = intent.getIntExtra("nesting", 1)
        seq.text = "Current nesting: $mNesting"

        nest.setOnClickListener{
            val intent = Intent(this@FinishAffinity, FinishAffinity::class.java)
            intent.putExtra("nesting", mNesting + 1)
            startActivity(intent)
        }
        finish.setOnClickListener{
            finishAffinity()
        }
    }
}