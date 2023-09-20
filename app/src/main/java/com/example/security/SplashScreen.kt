package com.example.security

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isUserLoggedIn=SharedPref.getBoolean(PrefConstants.IS_USER_LOGGED_IN, false)
        if (isUserLoggedIn){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }else{
           startActivity(Intent(this,LoginActivity::class.java))
           finish()

        }


    }
}