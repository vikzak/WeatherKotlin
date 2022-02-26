package ru.gb.weatherkotlin.experiment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.gb.weatherkotlin.R

//const val PUSH_KEY_TITLE = ""
//const val PUSH_KEY_MESSAGE = ""
class MyMessageService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteData = remoteMessage.data
        if (remoteData.isNotEmpty()){
            handleMessage(remoteData.toMap())
        }
    //super.onMessageReceived(remoteMessage)
    }

    private fun handleMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()){
            showNotification(title,message)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder = NotificationCompat
            .Builder(applicationContext, CHANNEL_ID)
            .apply { setSmallIcon(R.drawable.ic_kotlin_logo)
                setContentTitle(title)
                setContentText(message)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Channel name"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) { //Отправить токен на сервер

    }


    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 37
    }


}