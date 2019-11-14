package com.ccooy.apidemos.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.send_result.*

class SendResult :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.send_result)

        corky.setOnClickListener{
            setResult(Activity.RESULT_OK, Intent().setAction("Corky!"))
            finish()
        }
        violet.setOnClickListener{
            setResult(Activity.RESULT_OK, Intent().setAction("Violet!"))
            finish()
        }
    }
}