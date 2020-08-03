package com.andpjt.news.activity

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.andpjt.news.R

class SplashActivity : AppCompatActivity() {
    val handler = Handler()

    override fun onResume() {
        super.onResume()
        /* 1.3초 동안 splash 화면 띄우기 */
        handler.postDelayed({
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1300)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val iconImageView = findViewById<ImageView>(R.id.iconImageView)
        iconImageView.background = ShapeDrawable(OvalShape())
        iconImageView.clipToOutline = true
    }
}