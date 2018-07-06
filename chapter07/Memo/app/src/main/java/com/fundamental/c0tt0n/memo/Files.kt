package com.fundamental.c0tt0n.memo

import android.os.Environment
import android.text.format.DateFormat
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.Date

private const val DATE_TIME_FORMAT = "yyyy-MM-dd-hh-mm-ss"
private const val DIR_MEMO_FILES = "MemoFiles"

fun getFiles() = getFilesDir().listFiles().toList()

fun outputFiles(original: File?, content: String): File {
  val timeStamp = DateFormat.format(DATE_TIME_FORMAT, Date())
  val file = original ?: File(getFilesDir(), "memo-$timeStamp")

  BufferedWriter(FileWriter(file)).use {
    it.write(content)
    it.flush()
  }

  return file
}

fun inputFile(file: File): String = BufferedReader(FileReader(file)).readLines().joinToString("\n")

private fun getFilesDir(): File {
  val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

  return if (publicDir != null) {
    if (!publicDir.exists()) publicDir.mkdirs()
    publicDir
  } else {
    val dir = File(Environment.getExternalStorageDirectory(), DIR_MEMO_FILES)
    if (!dir.exists()) dir.mkdirs()
    dir
  }
}