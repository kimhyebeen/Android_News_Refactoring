package com.andpjt.news.activity

import android.graphics.Color
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.andpjt.news.R
import kotlinx.android.synthetic.main.activity_news.*


class NewsActivity : AppCompatActivity() {
    val keyArray = arrayOf("key1", "key2", "key3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        var params = LinearLayout
            .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(20,0,20,0)

        /* 뉴스 제목, 키워드, webView에 보일 링크? 받아서 보이기. */
        titleText.text = intent.getStringExtra("title")

        for (i in 0..2) {
            if (!intent.getStringExtra(keyArray[i]).equals("null")) {
                TextView(applicationContext).apply {
                    setPadding(30, 0, 30, 0)
                    background = getDrawable(R.drawable.keyline)
                    layoutParams = params
                    setTextColor(Color.parseColor("#FFFFFF"))
                    text = intent.getStringExtra(keyArray[i])
                    keyLinear.addView(this)
                }
            }
        }

        webView.apply {
            loadUrl(intent.getStringExtra("link"))
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClientClass()
        }
    }

    private class WebViewClientClass : WebViewClient() {
        //페이지 이동
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
