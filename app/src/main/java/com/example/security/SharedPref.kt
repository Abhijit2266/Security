package com.example.security

import android.content.Context
import android.content.SharedPreferences

object SharedPref {

    private const val NAME="SecuritySharedPreferences"
    private const val MODE=Context.MODE_PRIVATE
    lateinit var preferences: SharedPreferences

    fun init(context: Context){
        preferences = context.getSharedPreferences(NAME,MODE)
    }

    fun putBoolean(key:String,value:Boolean){
        preferences.edit().putBoolean(key, value).commit()
    }
    fun getBoolean(key: String, b: Boolean):Boolean{
        return preferences.getBoolean(key,false)
    }

}