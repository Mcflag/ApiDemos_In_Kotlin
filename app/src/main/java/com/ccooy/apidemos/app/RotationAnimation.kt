package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.rotation_animation.*

class RotationAnimation : AppCompatActivity() {

    private var mRotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_ROTATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRotationAnimation(mRotationAnimation)

        setContentView(R.layout.rotation_animation)

        windowFullscreen.setOnCheckedChangeListener { buttonView, isChecked ->
            setFullscreen(isChecked)
        }

        rotation_radio_group.setOnCheckedChangeListener { group, checkedId ->
            mRotationAnimation = when (checkedId) {
                R.id.rotate -> WindowManager.LayoutParams.ROTATION_ANIMATION_ROTATE
                R.id.crossfade -> WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE
                R.id.jumpcut -> WindowManager.LayoutParams.ROTATION_ANIMATION_JUMPCUT
                else -> WindowManager.LayoutParams.ROTATION_ANIMATION_ROTATE
            }
            setRotationAnimation(mRotationAnimation)
        }
    }

    private fun setFullscreen(on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        }
        win.attributes = winParams
    }

    private fun setRotationAnimation(rotationAnimation: Int) {
        val win = window
        val winParams = win.attributes
        winParams.rotationAnimation = rotationAnimation
        win.attributes = winParams
    }
}