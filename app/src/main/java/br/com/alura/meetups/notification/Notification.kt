package br.com.alura.meetups.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import br.com.alura.meetups.R
import br.com.alura.meetups.ui.activity.MainActivity
import coil.imageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Notification(
    private val context: Context
) {

    private val manager by lazy {
        context.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        private var id = 1
    }

    fun show(data: Map<String, String?>) {
        CoroutineScope(Dispatchers.IO).launch {
            val image = tryFindImage(data["imagem"])
            val style = createStyle(image, data)
            val notification = createNotification(data, style)


            manager.notify(id, notification)
            id++
        }
    }

    private fun createStyle(
        image: Bitmap?,
        data: Map<String, String?>,
    ): NotificationCompat.Style {
        return image?.let {
            NotificationCompat.BigPictureStyle().bigPicture(it)
        } ?:
        NotificationCompat.BigTextStyle().bigText(data["descrição"])
    }

    private fun createNotification(
        data: Map<String, String?>,
        style: NotificationCompat.Style,
    ): Notification? {

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(data["titulo"])
            .setContentText(data["descricao"])
            .setSmallIcon(R.drawable.ic_new_event)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createPendingIntent())
            .setAutoCancel(true)
            .setStyle(style)
            .build()
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private suspend fun tryFindImage(image: String?): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(image)
            .build()
        return context.imageLoader.execute(request).drawable?.toBitmap()
    }

}
