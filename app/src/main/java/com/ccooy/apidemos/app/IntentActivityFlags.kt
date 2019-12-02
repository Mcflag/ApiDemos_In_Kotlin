package com.ccooy.apidemos.app

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ccooy.apidemos.R
import kotlinx.android.synthetic.main.intent_activity_flags.*

class IntentActivityFlags : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.intent_activity_flags)

        flag_activity_clear_task.setOnClickListener{
            startActivities(buildIntentsToViewsLists())
        }
        flag_activity_clear_task_pi.setOnClickListener{
            val context = this@IntentActivityFlags

            val pi = PendingIntent.getActivities(
                context, 0,
                buildIntentsToViewsLists(), PendingIntent.FLAG_UPDATE_CURRENT
            )

            try {
                pi.send()
            } catch (e: PendingIntent.CanceledException) {
                Log.w("IntentActivityFlags", "Failed sending PendingIntent", e)
            }
        }
    }

    private fun buildIntentsToViewsLists(): Array<Intent?> {
        val intents = arrayOfNulls<Intent>(3)

        intents[0] = Intent.makeRestartActivityTask(
            ComponentName(
                this,
                com.ccooy.apidemos.ApiDemos::class.java
            )
        )

        var intent = Intent(Intent.ACTION_MAIN)
        intent.setClass(this@IntentActivityFlags, com.ccooy.apidemos.ApiDemos::class.java!!)
        intent.putExtra("com.ccooy.apidemos.Path", "Views")
        intents[1] = intent

        intent = Intent(Intent.ACTION_MAIN)
        intent.setClass(this@IntentActivityFlags, com.ccooy.apidemos.ApiDemos::class.java!!)
        intent.putExtra("com.ccooy.apidemos.Path", "Views/Lists")

        intents[2] = intent
        return intents
    }
}