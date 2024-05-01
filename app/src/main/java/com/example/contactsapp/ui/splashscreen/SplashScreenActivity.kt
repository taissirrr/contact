package com.example.contactsapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.contactsapp.MainActivity
import com.example.contactsapp.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_layout)
        initView()
    }

    private fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent=Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 3000 // 3 seconds
    }
}