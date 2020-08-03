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

class newsRecyclerAdapter : RecyclerView.Adapter<newsRecyclerAdapter.ItemViewHolder>() {
    private var newsItems: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)

        return ItemViewHolder(
            view,
            parent.context
        )
    }

    class ItemViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView!!) {
        var newsImage = itemView.findViewById<ImageView>(R.id.newsImage)
        var newsTitle = itemView.findViewById<TextView>(R.id.newsTitle)
        var newsContents = itemView.findViewById<TextView>(R.id.newsContents)
        var keyword1 = itemView.findViewById<TextView>(R.id.keyword1)
        var keyword2 = itemView.findViewById<TextView>(R.id.keyword2)
        var keyword3 = itemView.findViewById<TextView>(R.id.keyword3)
        val context = context

        fun onBind(item: News) {
            newsTitle.text = item.title
            /* 피카소 라이브러리 이용해서 newsImage에 url 이미지 붙이기 */
            if (!item.image.equals("")) Picasso.with(context).load(item.image).into(newsImage)
            newsContents.text = item.description
            if (!item.keyword1.equals("null")) {
                keyword1.visibility = View.VISIBLE
                keyword1.text = item.keyword1
            } else keyword1.visibility = View.GONE
            if (!item.keyword2.equals("null")) {
                keyword2.visibility = View.VISIBLE
                keyword2.text = item.keyword2
            } else keyword2.visibility = View.GONE
            if (!item.keyword3.equals("null")) {
                keyword3.visibility = View.VISIBLE
                keyword3.text = item.keyword3
            } else keyword3.visibility = View.GONE

            /* item 누르면 NewsActivity로 이동 */
            itemView.setOnClickListener { v ->
                var intent = Intent(context, NewsActivity::class.java)
                /*기 뉴스 제목, 키워드, 원문 링크 putExtra */
                intent.putExtra("title", item.title)
                intent.putExtra("link", item.link)
                intent.putExtra("key1", item.keyword1)
                intent.putExtra("key2", item.keyword2)
                intent.putExtra("key3", item.keyword3)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(newsItems.get(position))
    }

    fun additem(news: News) {
        newsItems.add(news)
    }

    fun deleteItem() {
        while(!newsItems.isEmpty()) newsItems.removeAt(0)
    }
}