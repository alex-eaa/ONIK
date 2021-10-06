package com.example.onik.model.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.onik.R
import com.example.onik.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "Firebase"
const val PUSH_KEY_ID_MOVIE = "idMovie"
private const val PUSH_KEY_TITLE = "title"
private const val PUSH_KEY_MESSAGE = "message"
private const val CHANNEL_ID = "channel_id"
private const val NOTIFICATION_ID = 37

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val remoteMessageData = p0.data
        if (remoteMessageData.isNotEmpty()) {
            handleDataMessage(remoteMessageData.toMap())
        }
    }


    private fun handleDataMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]
        val idMovie = data[PUSH_KEY_ID_MOVIE]
        if (!title.isNullOrBlank() && !message.isNullOrBlank() && !idMovie.isNullOrBlank()) {
            showNotification(title, message, idMovie)
        }
    }


    private fun showNotification(title: String, message: String, idMovie: String) {
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_local_movies_24)
                setContentTitle(title)
                setContentText(message)
                setAutoCancel(true)
                setContentIntent(createPendingIntent(idMovie))
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
        val name = getString(R.string.channel1_name)
        val descriptionText = getString(R.string.сhannel1_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }


    private fun createPendingIntent(idMovie: String): PendingIntent {
        val notifyIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(PUSH_KEY_ID_MOVIE, idMovie)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    override fun onNewToken(token: String) {
//        Отправить токен на сервер
    }

}