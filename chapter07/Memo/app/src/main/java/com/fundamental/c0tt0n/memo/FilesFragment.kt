package com.fundamental.c0tt0n.memo

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.File

class FilesFragment : Fragment() {

  interface OnFileSelectListener {
    fun onFileSelected(file: File)
  }

  private lateinit var listener: OnFileSelectListener
  private lateinit var recyclerView: RecyclerView

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context is OnFileSelectListener) {
      listener = context
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_list, container, false)
    recyclerView = view.findViewById<RecyclerView>(R.id.filesList).apply {
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
    show()
    return view
  }

  fun show() {
    context?.let {
      recyclerView.adapter = FilesAdapter(it, getFiles(), { file ->
        listener.onFileSelected(file)
      })
    }
  }
}