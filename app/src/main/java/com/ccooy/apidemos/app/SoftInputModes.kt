package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.soft_input_modes.*

class SoftInputModes : AppCompatActivity() {
    internal val mResizeModeLabels =
        arrayOf<CharSequence>("Unspecified", "Resize", "Pan", "Nothing")

    internal val mResizeModeValues = intArrayOf(
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED,
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE,
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.soft_input_modes)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, mResizeModeLabels
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resize_mode.adapter = adapter
        resize_mode.setSelection(0)
        resize_mode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                window.setSoftInputMode(mResizeModeValues[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                window.setSoftInputMode(mResizeModeValues[0])
            }
        }
    }
}