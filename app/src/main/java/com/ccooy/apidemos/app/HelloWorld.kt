package com.ccooy.apidemos.app

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity

import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.hello_world.*

class HelloWorld : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hello_world)

        val s1 = SpannableString("3.07万元")
        s1.setSpan(
            AbsoluteSizeSpan(18, true),
            0,
            s1.length - 2,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        s1.setSpan(
            AbsoluteSizeSpan(10, true),
            s1.length - 2,
            s1.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv1.setTextColor(Color.parseColor("#217aff"))
        tv1.text = s1

        val s2 = SpannableString("您的估价低于80%车主的估价")
        s2.setSpan(
            ForegroundColorSpan(Color.parseColor("#656565")),
            0,
            4,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        s2.setSpan(
            ForegroundColorSpan(Color.parseColor("#f48421")),
            4,
            6,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        s2.setSpan(
            ForegroundColorSpan(Color.parseColor("#656565")),
            6,
            s2.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv2.text = s2

        val s3 = SpannableString("3.07万元")
        s3.setSpan(
            AbsoluteSizeSpan(18, true),
            0,
            s1.length - 2,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        s3.setSpan(
            AbsoluteSizeSpan(10, true),
            s1.length - 2,
            s1.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        s3.setSpan(
            ForegroundColorSpan(Color.parseColor("#f48421")),
            0,
            s1.length - 2,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        s3.setSpan(
            ForegroundColorSpan(Color.parseColor("#217aff")),
            s1.length - 2,
            s1.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv3.text = s3
    }
}
