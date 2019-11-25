package com.ccooy.apidemos.app

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.secure_dialog_activity.*

class SecureDialogActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secure_dialog_activity)
        show.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
                .setPositiveButton(android.R.string.ok, null)
                .setMessage(R.string.secure_dialog_dialog_text)
                .create()
            alertDialog.window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
            alertDialog.show()
        }
    }
}