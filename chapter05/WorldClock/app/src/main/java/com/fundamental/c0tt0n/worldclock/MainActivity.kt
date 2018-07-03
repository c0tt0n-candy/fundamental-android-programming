package com.fundamental.c0tt0n.worldclock

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.add
import kotlinx.android.synthetic.main.activity_main.clockList
import java.util.TimeZone
import kotlinx.android.synthetic.main.activity_main.timeZone as timeZoneView

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val timeZone = TimeZone.getDefault()
    timeZoneView.text = timeZone.displayName

    add.setOnClickListener {
      startActivityForResult(TimeZoneSelectActivity.getIntent(this), REQUEST_CODE)
    }

    showWorldClock()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
      val timeZone = data.getStringExtra(TimeZoneSelectActivity.EXT_TIME_ZONE)

      getSharedPreferences(PREF, Context.MODE_PRIVATE).apply {
        val timeZones = getStringSet(KEY_TIME_ZONE, setOf()).toMutableSet().apply {
          add(timeZone)
        }
        edit().putStringSet(KEY_TIME_ZONE, timeZones).apply()
      }

      showWorldClock()
    }
  }

  private fun showWorldClock() {
    val timeZones = getSharedPreferences(PREF, Context.MODE_PRIVATE)
        .getStringSet(KEY_TIME_ZONE, setOf())
        .toTypedArray()
    clockList.adapter = TimeZoneAdapter(this, timeZones)
  }

  companion object {
    private const val PREF = "prefs"
    private const val KEY_TIME_ZONE = "key_time_zone"

    private const val REQUEST_CODE = 1
  }
}
