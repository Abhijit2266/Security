package com.example.security

import android.app.Application

class MySecurityApplication :Application(){

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }

}