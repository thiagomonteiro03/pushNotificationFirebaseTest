package br.com.alura.meetups.firebase

import android.util.Log
import br.com.alura.meetups.model.Device
import br.com.alura.meetups.notification.Notification
import br.com.alura.meetups.preferences.FirebaseTokenPreferences
import br.com.alura.meetups.repository.DispositivoRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

private const val TAG = "MeetupsFCM"


class MeetupsFirebaseMessagingService: FirebaseMessagingService() {

    private val deviceRepository: DispositivoRepository by inject()
    private val preferences: FirebaseTokenPreferences by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken: $token")
        preferences.newToken()
        deviceRepository.save(
            Device(token = token)
        )
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        Notification(this).show(data)
    }

}