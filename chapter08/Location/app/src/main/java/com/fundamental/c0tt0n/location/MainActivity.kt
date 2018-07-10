package com.fundamental.c0tt0n.location

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Calendar

class MainActivity : AppCompatActivity() {

  private var currentDate = Calendar.getInstance()
  private lateinit var googleMap: GoogleMap

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val mapFragment = supportFragmentManager.findFragmentById(R.id.map)
    if (mapFragment is SupportMapFragment) {
      mapFragment.getMapAsync {
        googleMap = it
        renderMap()
      }
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
}
