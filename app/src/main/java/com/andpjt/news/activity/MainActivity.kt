package com.andpjt.news.activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.andpjt.news.R
import com.andpjt.news.adapter.ListAdapter
import com.andpjt.news.items.News
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var recyclerView: RecyclerView
    lateinit var swipeLayout: SwipeRefreshLayout
    lateinit var linearLayoutManager1: LinearLayoutManager
    val listAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        swipeLayout = findViewById(R.id.swipeLayout)
        linearLayoutManager1 = LinearLayoutManager(applicationContext)

        /* recyclerView 세팅 */
        recyclerView.layoutManager = linearLayoutManager1
        recyclerView.adapter = listAdapter

        /* RSS를 읽어서 newsList에 저장 */
        MyAsyncTask().execute()

        /* 리스트를 당겨서 새로고침 */
        swipeLayout.setOnRefreshListener {
            /* 새로고침 코드 (RSS에서 받아온 내용 저장하고 다시 list에 나타내기..??) */
            val num = listAdapter.itemCount-1
            listAdapter.deleteItem()
            for (i in 0..num) listAdapter.notifyItemRemoved(0)
            MyAsyncTask().execute()
            swipeLayout.isRefreshing = false
        }
    }

    inner class MyAsyncTask: AsyncTask<String, String, ArrayList<News>>() {
        var newsList: ArrayList<News> = ArrayList()
        val url = "https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko"

        val thisActivity = findViewById<ConstraintLayout>(R.id.mainConstraint)
        val progressbar = ProgressBar(this@MainActivity)
        var params = ConstraintLayout.LayoutParams(150, 150);

        override fun onPreExecute() {
            /* document 다 불러올 때까지 로딩표시 띄우기 */
            params.topToTop = R.id.mainConstraint
            params.bottomToBottom = R.id.mainConstraint
            params.leftToLeft = R.id.mainConstraint
            params.rightToRight = R.id.mainConstraint
            thisActivity.addView(progressbar, params)
            progressbar.visibility = View.VISIBLE
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: String?): ArrayList<News> {
            val doc: Document = Jsoup.connect(url).get()
            val size = doc.select("item").size
            val titles = doc.select("item").select("title")
            val links = doc.select("item").select("link")

            // 인증서 오류 처리
            permitSSL()

            for (i in 0 until size) {
                val metadoc: Document = Jsoup.connect(links[i].text().toString()).get()
                var img = ""
                var description = ""
                var keyword1 = "null"
                var keyword2 = "null"
                var keyword3 = "null"
                if (!metadoc.select("meta[property=og:image]").isEmpty()) img = metadoc.select("meta[property=og:image]")[0].attr("content")
                if (!metadoc.select("meta[property=og:description]").isEmpty()) description = metadoc.select("meta[property=og:description]")[0].attr("content")
                Log.d(TAG, "i : "+i)
                /* keyword 분석 (본문이 간혹 없는 경우엔 어떻게 하지?????? -> 없애면 되지) */
                if (!description.equals("")) {
                    val words = getKeyword(description)
                    if (words[0]!=null) keyword1 = words[0].toString()
                    if (words[1]!=null) keyword2 = words[1].toString()
                    if (words[2]!=null) keyword3 = words[2].toString()
//                    Log.d(TAG, "words : "+words[0]+" "+words[1]+" "+words[2])
                }
                var mNews = News(titles[i].text(), links[i].text(), img, description, keyword1, keyword2, keyword3)
                newsList.add(mNews)
            }
            return newsList
        }

        override fun onPostExecute(result: ArrayList<News>) {
            super.onPostExecute(result)
            if (result.size==0) Toast.makeText(applicationContext, "데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            else {
                for (i in 0 until result.size) {
                    listAdapter.additem(result[i].title, result[i].link, result[i].image, result[i].description, result[i].keyword1, result[i].keyword2, result[i].keyword3)
                }
                listAdapter.notifyDataSetChanged()
            }
            progressbar.visibility = View.GONE
        }
    }
    fun permitSSL() {
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
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        val allHostsValid: HostnameVerifier = object : HostnameVerifier {
            override fun verify(hostname: String?, session: SSLSession?): Boolean { return true }
        }
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }

    fun getKeyword(desc: String): Array<String?> {
        var keyword = arrayOfNulls<String>(3)
        val des = desc.split(" ")
        var map = HashMap<String, Int>()
        for (word in des) {
            if (map.containsKey(word)) {
                val n = map.get(word)
                map.remove(word)
                map.put(word, n!!+1)
            }
            else if (word.length >= 2) map.put(word, 1)
        }

        val list: List<Map.Entry<String, Int>> =
            LinkedList(map.entries)
        Collections.sort(
            list,
            object : Comparator<Map.Entry<String?, Int?>?> {
                override fun compare(
                    o1: Map.Entry<String?, Int?>?,
                    o2: Map.Entry<String?, Int?>?
                ): Int {
                    val comparision = (o1!!.value!! - o2!!.value!!) * -1
                    return if (comparision == 0) o1.key!!.compareTo(o2.key!!) else comparision
                }
            })
        var num = 0
        for ((key) in list) {
            keyword[num++] = key
            if (num == 3) break
        }
        return keyword
    }
}
