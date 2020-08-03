package com.andpjt.news.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import com.andpjt.news.R


class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val intent = intent
        var titleText = findViewById<TextView>(R.id.titleText)
        var keyLinear = findViewById<LinearLayout>(R.id.keyLinear)
        var text = TextView(applicationContext)
        var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(20,0,20,0)
        text.setPadding(30, 0, 30, 0)
        text.background = getDrawable(R.drawable.keyline)
        text.layoutParams = params

        var webView = findViewById<WebView>(R.id.webView)

        /* 뉴스 제목, 키워드, webView에 보일 링크? 받아서 보이기. */
        titleText.text = intent.getStringExtra("title")
        if (!intent.getStringExtra("key1").equals("null")) {
            text.text = intent.getStringExtra("key1")
            keyLinear.addView(text)
        }
        if (!intent.getStringExtra("key2").equals("null")) {
            text = TextView(applicationContext)
            params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(20,0,20,0)
            text.setPadding(30, 0, 30, 0)
            text.background = getDrawable(R.drawable.keyline)
            text.layoutParams = params
            text.text = intent.getStringExtra("key2")
            keyLinear.addView(text)
        }
        if (!intent.getStringExtra("key3").equals("null")) {
            text = TextView(applicationContext)
            params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(20,0,20,0)
            text.setPadding(30, 0, 30, 0)
            text.background = getDrawable(R.drawable.keyline)
            text.layoutParams = params
            text.text = intent.getStringExtra("key3")
            keyLinear.addView(text)
        }

        webView.loadUrl(intent.getStringExtra("link"))
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClientClass()
    }

    private class WebViewClientClass : WebViewClient() {
        //페이지 이동
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            Log.d("check URL", url)
            view.loadUrl(url)
            return true
        }
    }
}
