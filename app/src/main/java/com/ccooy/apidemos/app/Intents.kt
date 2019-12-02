package com.ccooy.apidemos.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R

class Intents : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.intents)
    }

    fun onGetMusic(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        startActivity(Intent.createChooser(intent, "Select music"))
    }

    fun onGetImage(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivity(Intent.createChooser(intent, "Select image"))
    }

    fun onGetStream(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivity(Intent.createChooser(intent, "Select stream"))
    }
}