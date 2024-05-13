package com.example.landmarkremark.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.landmarkremark.R
import com.example.landmarkremark.util.authenticate.UserManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSplashScreen()
    }

    private fun setSplashScreen() {
        val loginFlag = UserManager.getInstance(this).isLogged()

        Handler(Looper.getMainLooper()).postDelayed({
            if (loginFlag) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, NotLoginActivity::class.java))
            }
            finish()
        }, 2000L)
    }
}