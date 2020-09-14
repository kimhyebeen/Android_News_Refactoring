package com.andpjt.news.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andpjt.news.R
import com.andpjt.news.activity.NewsActivity
import com.andpjt.news.items.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*

class NewsRecyclerAdapter : RecyclerView.Adapter<NewsRecyclerAdapter.ItemViewHolder>() {
    private var newsItems: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)

        return ItemViewHolder(
            view,
            parent.context
        )
    }

    class ItemViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView!!) {
        private val newsImage: ImageView = itemView.newsImage
        private val newsTitle: TextView = itemView.newsTitle
        private val newsContents: TextView = itemView.newsContents
        private val keyword1: TextView = itemView.keyword1
        private val keyword2: TextView = itemView.keyword2
        private val keyword3: TextView = itemView.keyword3

        fun onBind(item: News) {
            newsTitle.text = item.title
            /* 피카소 라이브러리 이용해서 newsImage에 url 이미지 붙이기 */
            if (item.image != "") Picasso.with(context).load(item.image).into(newsImage)
            newsContents.text = item.description

            if (item.keyword1 != "null") {
                keyword1.visibility = View.VISIBLE
                keyword1.text = item.keyword1
            } else keyword1.visibility = View.GONE
            if (item.keyword2 != "null") {
                keyword2.visibility = View.VISIBLE
                keyword2.text = item.keyword2
            } else keyword2.visibility = View.GONE
            if (item.keyword3 != "null") {
                keyword3.visibility = View.VISIBLE
                keyword3.text = item.keyword3
            } else keyword3.visibility = View.GONE

            /* item 누르면 NewsActivity로 이동 */
            itemView.setOnClickListener {
                Intent(context, NewsActivity::class.java).apply {
                    /* 뉴스 제목, 키워드, 원문 링크 putExtra */
                    putExtra("title", item.title)
                    putExtra("link", item.link)
                    putExtra("key1", item.keyword1)
                    putExtra("key2", item.keyword2)
                    putExtra("key3", item.keyword3)
                    context.startActivity(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(newsItems.get(position))
    }

    fun addItem(news: News) {
        newsItems.add(news)
    }

    fun deleteItem() {
        while(!newsItems.isEmpty()) newsItems.removeAt(0)
    }
}