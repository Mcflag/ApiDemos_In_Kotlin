package com.ccooy.apidemos.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.receive_result.*

class ReceiveResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.receive_result)
        results.setText(results.text, TextView.BufferType.EDITABLE)

        get.setOnClickListener{
            val intent = Intent(this@ReceiveResult, SendResult::class.java)
            startActivityForResult(intent, GET_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GET_CODE) {
            val text = results.text as Editable

            if (resultCode == Activity.RESULT_CANCELED) {
                text.append("(cancelled)")
            } else {
                text.append("(okay ")
                text.append(resultCode.toString())
                text.append(") ")
                if (data != null) {
                    text.append(data.action)
                }
            }

            text.append("\n")
        }
    }

    companion object {
        private const val GET_CODE = 0
    }
}