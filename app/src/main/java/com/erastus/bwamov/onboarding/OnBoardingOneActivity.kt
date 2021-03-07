package com.erastus.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.erastus.bwamov.R
import com.erastus.bwamov.sign.signin.SignInActivity
import com.erastus.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_on_boarding_one.*


/*
    ini adalah halaman yang pertama kali dijalankan
 */
class OnBoardingOneActivity : AppCompatActivity() {

    lateinit var preferences : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_one)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")) {
            finishAffinity()

            val intent = Intent(this@OnBoardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        btn_home.setOnClickListener {
            val intent = Intent(this@OnBoardingOneActivity, OnBoardingTwoActivity::class.java)
            startActivity(intent)
        }

        btn_daftar.setOnClickListener {
            preferences.setValue("onboarding", "1")
            finishAffinity()

            val intent = Intent(this@OnBoardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}