package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var customButton : LoadingButton
    private lateinit var fileName : String



    private var urlToDownload = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        // Initialize the notification manager
        notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
        ) as NotificationManager


        customButton = findViewById(R.id.custom_button)

        customButton.setOnClickListener {
            download()
        }

        createChannel(
                getString(R.string.loading_app_notification_id),
                getString(R.string.loading_app_notification_channel_name)
        )

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            when (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
                downloadID -> {

                    customButton.buttonState = ButtonState.Completed

                    notificationManager.sendNotification(urlToDownload, applicationContext, getString(R.string.success))
                }

                else -> {
                    customButton.buttonState = ButtonState.Completed

                    notificationManager.sendNotification(urlToDownload, applicationContext, getString(R.string.failure))
                }
            }
        }
    }

    private fun download() {

        if(urlToDownload == "") {
            Toast.makeText(this, "Please select an item", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "downloading", Toast.LENGTH_LONG).show()
            val request =
                DownloadManager.Request(Uri.parse(urlToDownload))
                    .setTitle(fileName)
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)


            customButton.buttonState = ButtonState.Loading

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
    }


    fun onRadioButtonClicked (view : View) {

        if (view is RadioButton) {

            val checked = view.isChecked


            // check which radio button was clicked
            when (view.id) {
                R.id.content_main_glide_rb ->
                    if (checked) {
                        // change the url
                        fileName = getString(R.string.glide_str)
                        urlToDownload = GLIDE_URL
                    }

                R.id.content_main_udacity_rb ->
                    if (checked) {
                        fileName = getString(R.string.udacity_str)
                        urlToDownload = UDACITY_URL
                    }

                R.id.content_main_retrofit_rb -> {
                    if (checked) {
                        fileName = getString(R.string.retrofit_str)
                        urlToDownload = RETROFIT_URL
                    }
                }

                else -> {
                    Toast.makeText(this, "Please select a file to down", Toast.LENGTH_LONG).show()
                }


            }
        }

    }

    private fun createChannel (channelId : String, channelName : String) {

        // create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            ).apply {
                        setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download completed"


            // Get an instance of notification manager
            val notificationManager = this.getSystemService(
                    NotificationManager::class.java
            )

            // create the notification channel
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object {
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }

}
