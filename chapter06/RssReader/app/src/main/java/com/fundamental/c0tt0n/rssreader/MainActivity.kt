package com.fundamental.c0tt0n.rssreader

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.articles
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Rss> {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    @Suppress("DEPRECATION")
    supportLoaderManager.initLoader(1, null, this)

    createChannel(this)

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      val fetchJobs = JobInfo.Builder(1, ComponentName(this, PollingJob::class.java))
          .setPeriodic(TimeUnit.HOURS.toMillis(6))
          .setPersisted(true)
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
          .build()

      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        getSystemService(JobScheduler::class.java).schedule(fetchJobs)
      }
    }
  }

  // [LoaderManager.LoaderCallbacks]
  override fun onCreateLoader(id: Int, args: Bundle?) = RssLoader(this)

  override fun onLoaderReset(loader: Loader<Rss>) {
    // need not implemented
  }

  override fun onLoadFinished(loader: Loader<Rss>, data: Rss?) {
    if (data != null) {
      articles.apply {
        layoutManager = GridLayoutManager(this@MainActivity, 2)
        adapter = ArticlesAdapter(this@MainActivity, data.articles) { article ->
          CustomTabsIntent.Builder().build().launchUrl(this@MainActivity, Uri.parse(article.link))
        }
      }
    }
  }
}
