package com.fundamental.c0tt0n.worldclock

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_time_zone_select.clockList

class TimeZoneSelectActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_time_zone_select)

    setResult(Activity.RESULT_CANCELED)

    clockList.apply {
      val adapter = TimeZoneAdapter(this@TimeZoneSelectActivity)
      this.adapter = adapter
      setOnItemClickListener { _, _, position, _ ->
        val timeZone = adapter.getItem(position)
        setResult(Activity.RESULT_OK, Intent().putExtra(EXT_TIME_ZONE, timeZone))
        finish()
      }
    }
  }

  companion object {
    const val EXT_TIME_ZONE = "ext_time_zone"

    fun getIntent(context: Context) = Intent(context, TimeZoneSelectActivity::class.java)
  }
}
