package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R


private val NOTIFICATION_ID = 0

/**
 * Extension func to send notification
 * */
fun NotificationManager.sendNotification(messageBody : String, applicationContext: Context, downloadStatus : String) {

    /**
     * Create an intent
     * */
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
            .putExtra("fileName", messageBody)
            .putExtra("downloadStatus", downloadStatus)


    /**
     * Create pending Intent
     * */
    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    /**
     * Style the notification
     * */
    val downloadedImage = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.computing_download_icon
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(downloadedImage)
            .bigLargeIcon(null)


    /**
     * Instance of notification builder
     * */
    val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.loading_app_notification_id)
    )

            .setSmallIcon(R.drawable.computing_download_icon)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setStyle(bigPicStyle)
            .setLargeIcon(downloadedImage)

            /**
             * Actions to open detail activity
             * */
            .addAction(
                    R.drawable.computing_download_icon,
                    applicationContext.getString(R.string.check_download_status),
                    contentPendingIntent
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    // Deliver the notification
    /**
     * [NOTIFICATION_ID] is need to get the instance of this notification to create or delete the notification
     * */
            notify(NOTIFICATION_ID, builder.build())

}



/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}