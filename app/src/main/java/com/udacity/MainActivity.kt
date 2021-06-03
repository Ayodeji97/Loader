package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var customButton : LoadingButton



    private var urlToDownload = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        customButton = findViewById(R.id.custom_button)

        customButton.setOnClickListener {
            download()
        }


    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {

        if(urlToDownload == "") {
            Toast.makeText(this, "Please select an item", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "downloading", Toast.LENGTH_LONG).show()
            val request =
                DownloadManager.Request(Uri.parse(urlToDownload))
                    .setTitle(getString(R.string.app_name))
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
                        urlToDownload = GLIDE_URL
                    }

                R.id.content_main_udacity_rb ->
                    if (checked) {
                        urlToDownload = UDACITY_URL
                    }

                R.id.content_main_retrofit_rb -> {
                    if (checked) {
                        urlToDownload = RETROFIT_URL
                    }
                }

                else -> {
                    Toast.makeText(this, "Please select a file to down", Toast.LENGTH_LONG).show()
                }


            }
        }

    }

    companion object {
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }

}
