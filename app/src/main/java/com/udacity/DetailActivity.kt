package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.utils.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {


    private lateinit var notificationManager: NotificationManager
    private lateinit var fileName : String
    private lateinit var status : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        // fine name and download status
        fileName = intent.getStringExtra("fileName").toString()
        status = intent.getStringExtra("downloadStatus").toString()

        content_detail_file_name_tv.text = fileName
        content_detail_status_name_tv.text = status

        // Initialize Notification Manager
        notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
        ) as NotificationManager


        /**
         * Finish the detail activity
         * */
        detail_btn.setOnClickListener {
            finish()
        }
        // cancel notification
       notificationManager.cancelNotifications()

    }

}
