package com.fundamental.c0tt0n.rssreader

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build.VERSION_CODES
import android.support.annotation.RequiresApi

@RequiresApi(VERSION_CODES.LOLLIPOP)
class PollingJob : JobService() {

  override fun onStartJob(params: JobParameters?): Boolean {
    return false
  }

  override fun onStopJob(params: JobParameters?): Boolean {
    Thread {
      val response = httpGet(HOT_TOPICS_URL)
      if (response != null) {
        val rss = parseRss(response)
        val prefs = getSharedPreferences(PREFS_POLLING, Context.MODE_PRIVATE)
        val lastFetchTime = prefs.getLong(LAST_PUBLISH_TIME, 0L)

        if (lastFetchTime > 0 && lastFetchTime < rss.pubDate.time) {
          notifyUpdate(this)
        }

        prefs.edit().putLong(LAST_PUBLISH_TIME, rss.pubDate.time).apply()
      }
    }.start()

    return true
  }

  companion object {
    private const val HOT_TOPICS_URL = "https://www.sbbit.jp/rss/HotTopics.rss"
    private const val PREFS_POLLING = "prefs_polling"
    private const val LAST_PUBLISH_TIME = "last_publish_time"
  }
}