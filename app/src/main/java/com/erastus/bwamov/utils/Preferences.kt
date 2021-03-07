package com.erastus.bwamov.utils

import android.content.Context
import android.content.SharedPreferences

// Kelas ini dibuat agar user sekali login saja saat buka aplikasi dan tidak ke logout saat aplikasi ditutup
class Preferences (val context: Context){
    companion object {
        const val  USER_PREFF = "USER_PREFF"
    }

    var sharedPreferences = context.getSharedPreferences(USER_PREFF, 0)

    fun setValue(key: String, value: String) {

        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getValues (key: String) : String? {
        return sharedPreferences.getString(key, "")
    }
}