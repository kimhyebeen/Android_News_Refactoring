package com.andpjt.news.activity

import android.os.Bundle
import android.transition.TransitionInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.andpjt.news.R
import com.andpjt.news.adapter.NewsRecyclerAdapter
import com.andpjt.news.items.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private val listAdapter = NewsRecyclerAdapter()
    private lateinit var url: String
    lateinit var getNewsScope: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.run {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.custom_transition)
            sharedElementExitTransition = TransitionInflater.from(context).inflateTransition(R.transition.custom_transition)
        }

        url = getString(R.string.url)
        recyclerView = findViewById(R.id.recyclerView)
        swipeLayout = findViewById(R.id.swipeLayout)

        /* recyclerView 세팅 */
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = listAdapter

        /* RSS를 읽어서 newsList에 저장 */
        permitSSL()

        getNewsData()

        /* 리스트를 당겨서 새로고침 */
        swipeLayout.setOnRefreshListener {
            /* 새로고침 코드 (RSS에서 받아온 내용 저장하고 다시 list에 나타내기..??) */
            getNewsScope.cancel()
            val num = listAdapter.itemCount-1
            listAdapter.deleteItem()
            for (i in 0..num) listAdapter.notifyItemRemoved(0)
            getNewsData()
            swipeLayout.isRefreshing = false
        }
    }

    private fun getNewsData() {
        getNewsScope = GlobalScope.launch {
            val doc: Document = Jsoup.connect(url).get()
            val size = doc.select("item").size
            val titles = doc.select("item").select("title")
            val links = doc.select("item").select("link")

            // 인증서 오류 처리
            permitSSL()

            for (i in 0 until size) {
                try {
                    var news = News(titles[i].text().toString(), links[i].text().toString(), "", "", "null", "null", "null")

                    Jsoup.connect(links[i].text().toString())
                        .userAgent("Mozilla")
                        .header("Accept", "text/html")
                        .header("Accept-Encoding", "gzip,deflate")
                        .header("Accept-Language", "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
                        .header("Connection", "keep-alive")
                        .ignoreContentType(true)
                        .get().let { metadoc ->
                            if (!metadoc.select("meta[property=og:image]").isEmpty()) news.image = metadoc.select("meta[property=og:image]")[0].attr("content")
                            if (!metadoc.select("meta[property=og:description]").isEmpty()) news.description = metadoc.select("meta[property=og:description]")[0].attr("content")
                        }

                    /* keyword 분석 */
                    if (!news.description.equals("")) {
                        val words = getKeyword(news.description)
                        if (words[0]!=null) news.keyword1 = words[0].toString()
                        if (words[1]!=null) news.keyword2 = words[1].toString()
                        if (words[2]!=null) news.keyword3 = words[2].toString()
                    }
                    listAdapter.addItem(news)

                    launch(Dispatchers.Main) { listAdapter.notifyDataSetChanged() }
                } catch (e: Exception) {
                    println("${i}번째에서 오류 발생")
                }
            }
            launch(Dispatchers.Main) { Toast.makeText(applicationContext, "finish", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun permitSSL() {
        val trustAllCerts =
            arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate?>? { return null }
                override fun checkClientTrusted(
                    certs: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    certs: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }
            })

        SSLContext.getInstance("SSL").let {
            it.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(it.socketFactory)
        }
        HostnameVerifier { _, _ -> true }.let {
            HttpsURLConnection.setDefaultHostnameVerifier(it)
        }
    }

    private fun getKeyword(desc: String): Array<String?> {
        var keyword = arrayOfNulls<String>(3)
        val des = desc.split(" ")
        var map = HashMap<String, Int>()
        for (word in des) {
            if (map.containsKey(word)) {
                val n = map[word]
                map.remove(word)
                map[word] = n!!+1
            }
            else if (word.length >= 2) map[word] = 1
        }

        val list: List<Map.Entry<String, Int>> =
            LinkedList(map.entries)
        Collections.sort(
            list
        ) { o1, o2 ->
            val comparision = (o1!!.value - o2!!.value) * -1
            if (comparision == 0) o1.key.compareTo(o2.key) else comparision
        }
        var num = 0
        for ((key) in list) {
            keyword[num++] = key
            if (num == 3) break
        }
        return keyword
    }
}
