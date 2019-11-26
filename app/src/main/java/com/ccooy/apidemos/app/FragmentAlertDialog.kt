package com.ccooy.apidemos.app

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.fragment_dialog.*

class FragmentAlertDialog :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dialog)
        text.text = "Example of displaying an alert dialog with a DialogFragment"
        show.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        val newFragment = MyAlertDialogFragment.newInstance(
            R.string.alert_dialog_two_buttons_title
        )
        newFragment.show(supportFragmentManager, "dialog")
    }

    fun doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!")
    }

    fun doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!")
    }

    class MyAlertDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val title = arguments?.getInt("title")

            return AlertDialog.Builder(activity!!)
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title!!)
                .setCancelable(false)
                .setPositiveButton(
                    R.string.alert_dialog_ok
                ) { dialog, whichButton -> (activity as FragmentAlertDialog).doPositiveClick() }
                .setNegativeButton(
                    R.string.alert_dialog_cancel
                ) { dialog, whichButton -> (activity as FragmentAlertDialog).doNegativeClick() }
                .create()
        }

        companion object {
            fun newInstance(title: Int): MyAlertDialogFragment {
                val frag = MyAlertDialogFragment()
                val args = Bundle()
                args.putInt("title", title)
                frag.arguments = args
                return frag
            }
        }
    }
}