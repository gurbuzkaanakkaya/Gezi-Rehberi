package com.example.projectpazarama.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.projectpazarama.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this,GezeceklerimActivity::class.java)
            startActivity(intent)
            finish()
        },2500)
    }
}