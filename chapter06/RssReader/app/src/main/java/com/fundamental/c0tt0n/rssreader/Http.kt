package com.fundamental.c0tt0n.rssreader

import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun httpGet(url: String): InputStream? {

  val con = (URL(url).openConnection() as HttpsURLConnection).apply {
    requestMethod = "GET"
    connectTimeout = 3000
    readTimeout = 5000
    instanceFollowRedirects = true
  }

  con.connect()

  return if (con.responseCode in 200..299) {
    BufferedInputStream(con.inputStream)
  } else null
}