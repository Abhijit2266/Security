package com.example.security

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            // This code will be executed after the timer (3 seconds) is over
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Close this activity
            finish()
        }, 3000)
    }
}