package com.fundamental.c0tt0n.location

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import java.util.Calendar

private const val DB_NAME = "LocationDatabase"
private const val DB_VERSION = 1
private const val TABLE_NAME = "Locations"
private const val CLM_ID = "_id"
private const val CLM_LATITUDE = "latitude"
private const val CLM_LONGITUDE = "longitude"
private const val CLM_TIME = "time"

class LocationDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

  override fun onCreate(db: SQLiteDatabase?) {
    db?.execSQL("""
      CREATE TABLE $TABLE_NAME(
      $CLM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
      $CLM_LATITUDE REAL NOT NULL,
      $CLM_LONGITUDE REAL NOT NULL,
      $CLM_TIME INTEGER NOT NULL);
      """)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    // need not implement
  }

}

class LocationRecord(val id: Long, val latitude: Double, val longitude: Double, val time: Long)

fun selectInDay(context: Context, year: Int, month: Int, day: Int): List<LocationRecord> {
  val calendar = Calendar.getInstance().apply {
    set(year, month, day, 0, 0, 0)
  }
  val from = calendar.time.time.toString()
  calendar.add(Calendar.DATE, 1)
  val to = calendar.time.time.toString()

  val locations = mutableListOf<LocationRecord>()
  LocationDatabase(context).readableDatabase.use { db ->
    db.query(
        TABLE_NAME,
        null,
        "$CLM_TIME >= ? AND $CLM_TIME < ?",
        arrayOf(from, to),
        null,
        null,
        "$CLM_TIME DESC"
    ).use { c ->
      while (c.moveToNext()) {
        val place = LocationRecord(
            c.getLong(c.getColumnIndex(CLM_ID)),
            c.getDouble(c.getColumnIndex(CLM_LATITUDE)),
            c.getDouble(c.getColumnIndex(CLM_LONGITUDE)),
            c.getLong(c.getColumnIndex(CLM_TIME))
        )
        locations.add(place)
      }
    }
  }
  return locations
}

fun insertLocations(context: Context, locations: List<Location>) {
  LocationDatabase(context).writableDatabase.use { db ->
    locations.filter { !it.isFromMockProvider }
        .forEach { locations ->
          val record = ContentValues().apply {
            put(CLM_LATITUDE, locations.latitude)
            put(CLM_LONGITUDE, locations.longitude)
            put(CLM_TIME, locations.time)
          }
          db.insert(TABLE_NAME, null, record)
        }
  }
}