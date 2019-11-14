package com.ccooy.apidemos.app

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.activity_animation.*

class Animation :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        fade_animation.setOnClickListener {
            startActivity(Intent(this@Animation, HelloWorld::class.java))
            overridePendingTransition(R.anim.fade, R.anim.hold)
        }
        zoom_animation.setOnClickListener {
            startActivity(Intent(this@Animation, HelloWorld::class.java))
            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            modern_fade_animation.setOnClickListener {
                val opts = ActivityOptions.makeCustomAnimation(
                    this@Animation,
                    R.anim.fade, R.anim.hold
                )
                startActivity(Intent(this@Animation, HelloWorld::class.java), opts.toBundle())
            }
            modern_zoom_animation.setOnClickListener {
                val opts = ActivityOptions.makeCustomAnimation(
                    this@Animation,
                    R.anim.zoom_enter, R.anim.zoom_enter
                )
                startActivity(Intent(this@Animation, HelloWorld::class.java), opts.toBundle())
            }
            scale_up_animation.setOnClickListener {v ->
                val opts = ActivityOptions.makeScaleUpAnimation(
                    v, 0, 0, v.width, v.height
                )
                startActivity(Intent(this@Animation, HelloWorld::class.java), opts.toBundle())
            }
            zoom_thumbnail_animation.setOnClickListener {  v ->
                v.isDrawingCacheEnabled = true
                v.isPressed = false
                v.refreshDrawableState()
                val bm = v.drawingCache
                val c = Canvas(bm)
                c.drawARGB(255, 255, 0, 0)
                val opts = ActivityOptions.makeThumbnailScaleUpAnimation(
                    v, bm, 0, 0
                )
                startActivity(Intent(this@Animation, HelloWorld::class.java), opts.toBundle())
                v.isDrawingCacheEnabled = false
            }
        } else {
            modern_fade_animation.isEnabled = false
            modern_zoom_animation.isEnabled = false
            scale_up_animation.isEnabled = false
            zoom_thumbnail_animation.isEnabled = false
        }

    }
}