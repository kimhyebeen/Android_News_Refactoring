package com.andpjt.news.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.nfc.Tag
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.andpjt.news.R
import com.andpjt.news.activity.NewsActivity
import com.andpjt.news.items.News
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

class ListAdapter : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
    private var newsItems: ArrayList<News> = ArrayList()
    val TAG = "ListAdapter"

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
        val cont = context

        fun onBind(title: String, link: String, image: String, contents: String, key1: String, key2: String, key3: String) {
            newsTitle.text = title
            /* 피카소 라이브러리 이용해서 newsImage에 url 이미지 붙이기 */
            Log.d("ListAdapter", "이미지 링크 : "+image)
            if (!image.equals("")) Picasso.with(cont).load(image).into(newsImage)
            newsContents.text = contents
            if (!key1.equals("null")) {
                keyword1.visibility = View.VISIBLE
                keyword1.text = key1
            } else keyword1.visibility = View.GONE
            if (!key2.equals("null")) {
                keyword2.visibility = View.VISIBLE
                keyword2.text = key2
            } else keyword2.visibility = View.GONE
            if (!key3.equals("null")) {
                keyword3.visibility = View.VISIBLE
                keyword3.text = key3
            } else keyword3.visibility = View.GONE

            /* item 누르면 NewsActivity로 이동 */
            itemView.setOnClickListener { v ->
                var intent = Intent(cont, NewsActivity::class.java)
                /*기 뉴스 제목, 키워드, 원문 링크 putExtra */
                intent.putExtra("title", title)
                intent.putExtra("link", link)
                intent.putExtra("key1", key1)
                intent.putExtra("key2", key2)
                intent.putExtra("key3", key3)
                cont.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(newsItems.get(position).title, newsItems.get(position).link, newsItems.get(position).image, newsItems.get(position).description, newsItems.get(position).keyword1, newsItems.get(position).keyword2, newsItems.get(position).keyword3)
    }

    fun additem(t: String, l: String, i: String, d: String, k1: String, k2: String, k3: String) {
        var news = News(t, l, i, d, k1, k2, k3)
        newsItems.add(news)
    }

    fun deleteItem() {
        while(!newsItems.isEmpty()) newsItems.removeAt(0)
    }
}