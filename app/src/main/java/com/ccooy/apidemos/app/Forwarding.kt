package com.ccooy.apidemos.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.forwarding.*

class Forwarding:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forwarding)
        go.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@Forwarding, ForwardTarget::class.java)
            startActivity(intent)
            finish()
        }
    }
}