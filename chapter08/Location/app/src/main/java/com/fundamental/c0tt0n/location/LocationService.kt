package com.fundamental.c0tt0n.location

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.LocationResult

class LocationService : IntentService("LocationService") {

  override fun onHandleIntent(intent: Intent?) {
    LocationResult.extractResult(intent)?.let {
      insertLocations(this, it.locations)
    }
  }
}