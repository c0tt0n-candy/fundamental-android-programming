package com.fundamental.c0tt0n.location

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Calendar

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

  private var currentDate = Calendar.getInstance()
  private lateinit var googleMap: GoogleMap

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT)) {
      currentDate = savedInstanceState.getSerializable(KEY_CURRENT) as Calendar
    }

    val mapFragment = supportFragmentManager.findFragmentById(R.id.map)
    if (mapFragment is SupportMapFragment) {
      mapFragment.getMapAsync {
        googleMap = it
        renderMap()
      }
      showCurrentDate()
    }
  }

  override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    super.onSaveInstanceState(outState, outPersistentState)
    outState?.putSerializable(KEY_CURRENT, currentDate)
  }

  // [DatePickerDialog.OnDateSetListener]
  override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
    currentDate.set(year, month, day)
    renderMap()
    showCurrentDate()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == SwitchFragment.REQUEST_CODE) {
      val fragment = supportFragmentManager.findFragmentById(R.id.switch_fragment)
      fragment?.onActivityResult(requestCode, resultCode, data)
    }
  }

  private fun renderMap() {
    val locations = selectInDay(this, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE])
    zoomTo(googleMap, locations)
    putMarkers(googleMap, locations)
  }

  private fun zoomTo(map: GoogleMap, locations: List<LocationRecord>) {
    val move = when {
      locations.size == 1 -> {
        val latLng = LatLng(locations[0].latitude, locations[0].longitude)
        CameraUpdateFactory.newLatLngZoom(latLng, 15f)
      }
      locations.size > 1 -> {
        val bounds = LatLngBounds.Builder()
        locations.forEach { location ->
          bounds.include(LatLng(location.latitude, location.longitude))
        }

        CameraUpdateFactory.newLatLngBounds(
            bounds.build(),
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels,
            (50 * resources.displayMetrics.density).toInt()
        )
      }
      else -> return
    }

    map.moveCamera(move)
  }

  private fun putMarkers(map: GoogleMap, locations: List<LocationRecord>) {
    map.clear()

    val lineOptions = PolylineOptions().apply {
      locations.forEach { location ->
        val latLng = LatLng(location.latitude, location.longitude)
        add(latLng)

        val marker = MarkerOptions().position(latLng).draggable(false).apply {
          icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }
        map.addMarker(marker)
      }
    }

    map.addPolyline(lineOptions)
  }

  private fun showCurrentDate() {
    findViewById<TextView>(R.id.date).apply {
      setOnClickListener {
        DatePickerFragment.getDialog(currentDate)
            .show(supportFragmentManager, TAG_CALENDAR)
      }
      text = DateFormat.format(DATE_FORMAT, currentDate.time)
    }
  }

  companion object {
    private const val KEY_CURRENT = "currentDate"
    private const val TAG_CALENDAR = "calendar"
    private const val DATE_FORMAT = "mm月 dd日"
  }
}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

  private lateinit var calendar: Calendar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    calendar = arguments?.getSerializable(ARG_CURRENT) as? Calendar ?: Calendar.getInstance()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return DatePickerDialog(context, this, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DATE])
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
    if (context is DatePickerDialog.OnDateSetListener) {
      (context as DatePickerDialog.OnDateSetListener).onDateSet(view, year, month, day)
    }
  }

  companion object {
    private const val ARG_CURRENT = "current"

    fun getDialog(current: Calendar) = DatePickerFragment().apply {
      arguments = Bundle().apply {
        putSerializable(ARG_CURRENT, current)
      }
    }
  }
}