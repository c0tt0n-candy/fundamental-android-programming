package com.fundamental.c0tt0n.rssreader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

private const val CHANNEL_ID = "update_channel"
private const val CHANNEL_NAME = "新着記事"
private const val NOTIFICATION_ID = 1
private const val REQUEST_CODE = 1
private const val NOTIFICATION_TITLE = "記事が更新されました"
private const val NOTIFICATION_TEXT = "新しい記事をチェックしましょう"

fun createChannel(context: Context) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
      enableLights(false)
      enableVibration(true)
      setShowBadge(true)
    }

    context.getSystemService(NotificationManager::class.java).apply {
      createNotificationChannel(channel)
    }
  }
}

fun notifyUpdate(context: Context) {
  val pendingIntent = PendingIntent.getActivity(
      context,
      REQUEST_CODE,
      Intent(context, MainActivity::class.java),
      PendingIntent.FLAG_ONE_SHOT
  )

  val notification = NotificationCompat.Builder(context, CHANNEL_ID)
      .setContentTitle(NOTIFICATION_TITLE)
      .setContentText(NOTIFICATION_TEXT)
      .setContentIntent(pendingIntent)
      .setSmallIcon(R.drawable.ic_notification)
      .setAutoCancel(true)
      .build()

  NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
}