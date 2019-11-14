package com.ccooy.apidemos.app

import android.app.WallpaperManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.wallpaper_2.*
import java.io.IOException

class SetWallpaperActivity : AppCompatActivity() {
    companion object {
        private val mColors = intArrayOf(
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.CYAN,
            Color.YELLOW,
            Color.WHITE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(R.layout.wallpaper_2)
        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaperDrawable = wallpaperManager.drawable
        imageview.isDrawingCacheEnabled = true
        imageview.setImageDrawable(wallpaperDrawable)

        randomize.setOnClickListener {
            val mColor = Math.floor(Math.random() * mColors.size).toInt()
            wallpaperDrawable.setColorFilter(mColors[mColor], PorterDuff.Mode.MULTIPLY)
            imageview.setImageDrawable(wallpaperDrawable)
            imageview.invalidate()
        }

        setwallpaper.setOnClickListener {
            try {
                wallpaperManager.setBitmap(imageview.drawingCache)
                finish()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}