package com.fundamental.c0tt0n.rssreader

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fundamental.c0tt0n.rssreader.ArticlesAdapter.ArticleViewHolder

class ArticlesAdapter(
    private val context: Context,
    private val articles: List<Article>,
    private val onArticleClicked: (Article) -> Unit
) : RecyclerView.Adapter<ArticleViewHolder>() {

  private val inflater = LayoutInflater.from(context)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
    val view = inflater.inflate(R.layout.grid_article_cell, parent, false)
    val holder = ArticleViewHolder(view)

    view.setOnClickListener {
      val position = holder.adapterPosition
      val article = articles[position]
      onArticleClicked(article)
    }
    return holder
  }

  override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
    val article = articles[position]
    holder.apply {
      title.text = article.title
      pubDate.text = context.getString(R.string.publish_date, article.pubDate)
    }
  }

  override fun getItemCount() = articles.size

  inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.title)
    val pubDate = view.findViewById<TextView>(R.id.pubDate)
  }
}