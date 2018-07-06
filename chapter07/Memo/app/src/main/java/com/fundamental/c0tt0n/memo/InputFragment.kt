package com.fundamental.c0tt0n.memo

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_input.content
import kotlinx.android.synthetic.main.fragment_input.save
import java.io.File

class InputFragment : Fragment() {

  interface OnFileOutputListener {
    fun onFileOutput()
  }

  private lateinit var listener: OnFileOutputListener
  private var currentFile: File? = null

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context is OnFileOutputListener) {
      listener = context
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_FILE)) {
      currentFile = savedInstanceState.getSerializable(KEY_FILE) as File
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    save.setOnClickListener {
      currentFile = outputFiles(currentFile, content.text.toString())
      listener.onFileOutput()
    }
    return inflater.inflate(R.layout.fragment_input, container, false)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    currentFile?.let {
      outState.putSerializable(KEY_FILE, it)
    }
  }

  fun show(file: File) {
    val memo = inputFile(file)
    content.setText(memo)
    currentFile = file
  }

  companion object {
    private const val KEY_FILE = "file"
  }
}