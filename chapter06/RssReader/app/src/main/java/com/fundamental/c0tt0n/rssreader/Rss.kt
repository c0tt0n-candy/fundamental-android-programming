package com.fundamental.c0tt0n.rssreader

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import org.w3c.dom.NodeList
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

data class Rss(
    val title: String,
    val pubDate: Date,
    val articles: List<Article>
)

data class Article(
    val title: String,
    val link: String,
    val pubDate: Date
)

// RSS parser
private const val DATE_TIME_FORMAT_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss z"
private const val ITEMS_PATH = "/rss/channel//item"
private const val ARTICLE_TITLE_PATH = "./title/text()"
private const val ARTICLE_LINK_PATH = "./link/text()"
private const val ARTICLE_PUB_DATE_PATH = "./pubDate/text()"
private const val RSS_TITLE_PATH = "/rss/channel/title/text()"
private const val RSS_PUB_DATE_PATH = "/rss/channel/pubDate/text()"

fun parseRss(stream: InputStream): Rss {
  val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream)

  stream.close()

  val xPath = XPathFactory.newInstance().newXPath()
  val formatter = SimpleDateFormat(DATE_TIME_FORMAT_RFC1123, Locale.US)

  val articles = arrayListOf<Article>().apply {
    val items = xPath.evaluate(ITEMS_PATH, doc, XPathConstants.NODESET) as NodeList

    for (i in 0 until items.length) {
      val item = items.item(i)
      add(Article(
          title = xPath.evaluate(ARTICLE_TITLE_PATH, item),
          link = xPath.evaluate(ARTICLE_LINK_PATH, item),
          pubDate = formatter.parse(xPath.evaluate(ARTICLE_PUB_DATE_PATH, item))
      ))
    }
  }

  return Rss(
      title = xPath.evaluate(RSS_TITLE_PATH, doc),
      pubDate = formatter.parse(xPath.evaluate(RSS_PUB_DATE_PATH, doc)),
      articles = articles
  )
}

class RssLoader(context: Context) : AsyncTaskLoader<Rss>(context) {

  private var cache: Rss? = null

  override fun onStartLoading() {
    when {
      cache != null -> deliverResult(cache)
      takeContentChanged() || cache == null -> forceLoad()
    }
  }

  override fun loadInBackground(): Rss? {
    val response = httpGet(HOT_TOPICS_URL)

    return if (response != null) {
      parseRss(response)
    } else null
  }

  override fun deliverResult(data: Rss?) {
    if (isReset || data == null) return

    cache = data
    super.deliverResult(data)
  }

  override fun onStopLoading() {
    cancelLoad()
  }

  override fun onReset() {
    super.onReset()
    onStopLoading()
    cache = null
  }

  companion object {
    private const val HOT_TOPICS_URL = "https://www.sbbit.jp/rss/HotTopics.rss"
  }
}