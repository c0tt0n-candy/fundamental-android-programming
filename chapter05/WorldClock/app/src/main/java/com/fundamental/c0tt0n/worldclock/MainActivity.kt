package com.fundamental.c0tt0n.worldclock

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.add
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
  }

  companion object {
    private const val REQUEST_CODE = 1
  }
}
