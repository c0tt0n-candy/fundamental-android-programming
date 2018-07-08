package com.fundamental.c0tt0n.memo

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
    val view = inflater.inflate(R.layout.fragment_input, container, false) ?: return null
    val content = view.findViewById<EditText>(R.id.content)
    val save = view.findViewById<Button>(R.id.save)

    save.setOnClickListener {
      currentFile = outputFiles(currentFile, content.text.toString())
      listener.onFileOutput()
    }
    return view
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    currentFile?.let {
      outState.putSerializable(KEY_FILE, it)
    }
  }

  fun show(file: File) {
    val memo = inputFile(file)
    val content = view?.findViewById<EditText>(R.id.content) ?: return

    content.setText(memo)
    currentFile = file
  }

  companion object {
    private const val KEY_FILE = "file"
  }
}