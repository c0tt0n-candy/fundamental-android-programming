package com.fundamental.c0tt0n.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import java.util.concurrent.TimeUnit

class SwitchFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val isRequesting = context?.getSharedPreferences("", Context.MODE_PRIVATE)?.getBoolean("", false) ?: false

    val view = inflater.inflate(R.layout.fragment_switch, container, false)
    view.findViewById<Switch>(R.id.locationSwitch).apply {
      isChecked = isRequesting
      setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
          checkLocationAvailable()
        } else {
          stopLocationRequest()
        }
      }
    }

    return view
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      checkLocationPermission()
    } else {
      showErrorMessage()
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      checkLocationPermission()
    } else {
      showErrorMessage()
    }
  }

  private fun checkLocationAvailable() {
    activity?.let {
      val checkRequest = LocationSettingsRequest.Builder()
          .addLocationRequest(getLocationRequest())
          .build()
      val checkTask = LocationServices.getSettingsClient(it).checkLocationSettings(checkRequest)
      checkTask.addOnCompleteListener { response ->
        try {
          response.getResult(ApiException::class.java)
          checkLocationPermission()
        } catch (exception: ApiException) {
          if (exception.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
            val resolvable = exception as ResolvableApiException
            resolvable.startResolutionForResult(activity, 1)
          } else {
            showErrorMessage()
          }
        }
      }
    } ?: return
  }

  private fun checkLocationPermission() {
    context?.let {
      if (ContextCompat.checkSelfPermission(it, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val intent = Intent(it, LocationService::class.java)
        val service = PendingIntent.getService(it, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        LocationServices.getFusedLocationProviderClient(it).requestLocationUpdates(getLocationRequest(), service)

        it.getSharedPreferences(PREF_LOCATION, Context.MODE_PRIVATE).edit().putBoolean(KEY_REQUEST, true).apply()
      } else {
        requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 1)
      }
    } ?: return
  }

  private fun getLocationRequest() = LocationRequest()
      .setInterval(TimeUnit.MINUTES.toMillis(5))
      .setFastestInterval(TimeUnit.MINUTES.toMillis(1))
      .setMaxWaitTime(TimeUnit.MINUTES.toMillis(20))
      .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

  private fun stopLocationRequest() {
    context?.let {
      val intent = Intent(it, LocationService::class.java)
      val service = PendingIntent.getService(it, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
      LocationServices.getFusedLocationProviderClient(it).removeLocationUpdates(service)

      it.getSharedPreferences(PREF_LOCATION, Context.MODE_PRIVATE).edit().putBoolean(KEY_REQUEST, true).apply()
    } ?: return
  }

  private fun showErrorMessage() {
    Toast.makeText(context, getString(R.string.location_error_message), Toast.LENGTH_SHORT).show()
    activity?.finish()
  }

  companion object {
    private const val PREF_LOCATION = "LocationRequest"
    private const val KEY_REQUEST = "isRequesting"
  }
}