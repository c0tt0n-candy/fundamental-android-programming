package com.fundamental.c0tt0n.memo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.File

class FilesAdapter(
    private val context: Context,
    private val files: List<File>,
    private val onFileClicked: (File) -> Unit
) : RecyclerView.Adapter<FilesAdapter.FileViewHolder>() {

  override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
    val file = files[position]
    holder.apply {
      title.text = file.name
      updatedTime.text = context.getString(R.string.last_modified, file.lastModified())
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
    val view = LayoutInflater.from(context).inflate(R.layout.list_item_row, parent, false)
    val holder = FileViewHolder(view)

    view.setOnClickListener {
      onFileClicked(files[holder.adapterPosition])
    }

    return holder
  }

  override fun getItemCount() = files.size

  inner class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.title)
    val updatedTime = view.findViewById<TextView>(R.id.lastModified)
  }
}