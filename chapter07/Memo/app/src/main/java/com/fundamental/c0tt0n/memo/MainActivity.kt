package com.fundamental.c0tt0n.memo

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.fundamental.c0tt0n.memo.FilesFragment.OnFileSelectListener
import com.fundamental.c0tt0n.memo.InputFragment.OnFileOutputListener
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import java.io.File

class MainActivity : AppCompatActivity(), OnFileSelectListener, OnFileOutputListener {

  private var drawerToggle: ActionBarDrawerToggle? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (hasPermission()) setViews()
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    drawerToggle?.syncState()
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    super.onConfigurationChanged(newConfig)
    drawerToggle?.onConfigurationChanged(newConfig)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return if (drawerToggle?.onOptionsItemSelected(item) == true) {
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      setViews()
      drawerToggle?.syncState()
    } else {
      finish()
    }
  }

  // [OnFileSelectListener]
  override fun onFileSelected(file: File) {
    val fragment = supportFragmentManager.findFragmentById(R.id.input)
    if (fragment is InputFragment) {
      fragment.show(file)
    }
  }

  // [OnFileOutputListener]
  override fun onFileOutput() {
    val fragment = supportFragmentManager.findFragmentById(R.id.list)
    if (fragment is FilesFragment) {
      fragment.show()
    }
  }

  private fun setViews() {
    setContentView(R.layout.activity_main)
    if (drawerLayout != null) setupDrawer(drawerLayout)
  }

  private fun setupDrawer(drawer: DrawerLayout) {
    val toggle = ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name).apply {
      isDrawerIndicatorEnabled = true
    }
    drawer.addDrawerListener(toggle)
    drawerToggle = toggle

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
    }
  }

  private fun hasPermission(): Boolean {
    return if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 1)
      false
    } else {
      true
    }
  }
}
