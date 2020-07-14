package com.example.mynoteapp

import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(c: Context, i: Intent) {

        val mBuilder = NotificationCompat.Builder(c, "1")
            .setSmallIcon(R.drawable.splash)
            .setContentTitle("My Notes App")
            .setContentText(i.getStringExtra("title"))
            .setAutoCancel(true)
            .setVibrate(listOf(1000L,1000L, 1000L, 1000L, 1000L).toLongArray())
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val mNotificationManager = c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(100, mBuilder.build())


    }
}