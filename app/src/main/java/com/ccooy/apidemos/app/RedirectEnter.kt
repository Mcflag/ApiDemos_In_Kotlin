package com.ccooy.apidemos.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.redirect_enter.*

class RedirectEnter :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.redirect_enter)
        go.setOnClickListener {
            val intent = Intent(this@RedirectEnter, RedirectMain::class.java)
            startActivity(intent)
        }
    }
}