package com.example.projekatv2

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SportSpotApp : Application() {
    // Kreiraj instancu Firebase Firestore baze podataka
    val db by lazy { Firebase.firestore }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        // Kreiraj Notification Channel za Android 8.0 (API 26) ili novije verzije
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notification_channel_id"
            val channelName = "Notification name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance)

            // Pribavi Notification Manager i registruj Notification Channel
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }*/
    }
}
