package com.ccooy.apidemos.app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R

class LocalServiceActivities {

    class Controller : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.local_service_controller)

            var button = findViewById<View>(R.id.start) as Button
            button.setOnClickListener {
                startService(
                    Intent(
                        this@Controller,
                        LocalService::class.java
                    )
                )
            }
            button = findViewById<View>(R.id.stop) as Button
            button.setOnClickListener {
                stopService(
                    Intent(
                        this@Controller,
                        LocalService::class.java
                    )
                )
            }
        }
    }

    class Binding : AppCompatActivity() {
        private var mIsBound: Boolean = false

        private var mBoundService: LocalService? = null

        private val mConnection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                mBoundService = (service as LocalService.LocalBinder).service

                Toast.makeText(
                    this@Binding, R.string.local_service_connected,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onServiceDisconnected(className: ComponentName) {
                mBoundService = null
                Toast.makeText(
                    this@Binding, R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.local_service_binding)

            var button = findViewById<View>(R.id.bind) as Button
            button.setOnClickListener { doBindService() }
            button = findViewById<View>(R.id.unbind) as Button
            button.setOnClickListener { doUnbindService() }
        }

        private fun doBindService() {
            bindService(
                Intent(
                    this@Binding,
                    LocalService::class.java
                ), mConnection, Context.BIND_AUTO_CREATE
            )
            mIsBound = true
        }

        private fun doUnbindService() {
            if (mIsBound) {
                unbindService(mConnection)
                mIsBound = false
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            doUnbindService()
        }
    }
}