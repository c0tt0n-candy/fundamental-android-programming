package com.fundamental.c0tt0n.worldclock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class TimeZoneSelectActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_time_zone_select)
  }

  companion object {
    fun getIntent(context: Context) = Intent(context, TimeZoneSelectActivity::class.java)
  }
}
