package com.fundamental.c0tt0n.worldclock

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextClock
import android.widget.TextView
import java.util.TimeZone

class TimeZoneAdapter(
    private val context: Context,
    private val timeZones: Array<String> = TimeZone.getAvailableIDs()
) : BaseAdapter() {

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view = convertView ?: createView(parent)

    (view.tag as ViewHolder).apply {
      val timeZone = TimeZone.getTimeZone(getItem(position))

      @SuppressLint("SetTextI18n")
      name.text = "${timeZone.displayName}(${timeZone.id})"
      clock.timeZone = timeZone.id
    }

    return view
  }

  override fun getItem(position: Int) = timeZones[position]

  override fun getItemId(position: Int) = position.toLong()

  override fun getCount() = timeZones.size

  private fun createView(parent: ViewGroup?): View {
    return LayoutInflater.from(context).inflate(R.layout.list_time_zone_row, parent, false).apply {
      tag = ViewHolder(this)
    }
  }

  inner class ViewHolder(view: View) {
    val name = view.findViewById<TextView>(R.id.timeZone)
    val clock = view.findViewById<TextClock>(R.id.clock)
  }
}