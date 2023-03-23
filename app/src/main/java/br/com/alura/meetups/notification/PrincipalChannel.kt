package br.com.alura.meetups.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import br.com.alura.meetups.R

const val CHANNEL_ID = "f15f9a5b-cb31-44f2-95be-ce1a4f54d245"

class PrincipalChannel(
    private val context: Context,
    private val manager: NotificationManager
) {

    fun createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText

            manager.createNotificationChannel(mChannel)
        }
    }

}