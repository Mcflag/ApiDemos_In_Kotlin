package com.ccooy.apidemos.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import com.ccooy.apidemos.graphics.CubeRenderer
import kotlinx.android.synthetic.main.secure_surface_view_activity.*

class SecureSurfaceViewActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secure_surface_view_activity)
        surface_view.setRenderer(CubeRenderer(false))
        surface_view.setSecure(true)
    }

    override fun onResume() {
        super.onResume()
        surface_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        surface_view.onPause()
    }
}