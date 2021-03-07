package com.erastus.bwamov.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.erastus.bwamov.EditProfileActivity
import com.erastus.bwamov.MyWalletActivity
import com.erastus.bwamov.R
import com.erastus.bwamov.sign.signin.SignInActivity
import com.erastus.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    lateinit var preferences : Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(context!!)

        tv_nama.text = preferences.getValues("nama")
        tv_email.text = preferences.getValues("email")

        Glide.with(this)
                .load(preferences.getValues("url"))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

        tv_my_wallet.setOnClickListener {
            val intent = Intent(context, MyWalletActivity::class.java)
            startActivity(intent)
        }
        tv_edit_profile.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }
        tv_sign_out.setOnClickListener {
            preferences.setValue("status","0")

            val intent = Intent(context, SignInActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, "Akun anda telah keluar", Toast.LENGTH_LONG).show()
        }
    }


}