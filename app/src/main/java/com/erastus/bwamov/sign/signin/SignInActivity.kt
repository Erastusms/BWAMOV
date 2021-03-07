package com.erastus.bwamov.sign.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erastus.bwamov.home.HomeActivity
import com.erastus.bwamov.R
import com.erastus.bwamov.sign.signup.SignUpActivity
import com.erastus.bwamov.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    lateinit var iUsername:String
    lateinit var iPassword:String

    lateinit var mDatabase : DatabaseReference
    lateinit var preferences : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Connect to firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        // inisialisasi preferences
        preferences = Preferences(this)

        // Cek preferences agar saat aplikasi dibuka kembali setelah pernah sekali ditutup
        preferences.setValue("onboarding", "1")
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val goHome = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(goHome)
        }
        btn_home.setOnClickListener {

            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if(iUsername.equals("")) {
                et_username.error = "Mohon tuliskan username anda"
                et_username.requestFocus()
            } else if(iPassword.equals("")) {
                et_password.error = "Mohon tuliskan password anda"
                et_password.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        btn_daftar.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {

        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if(user == null) {
                    Toast.makeText(this@SignInActivity, "Username tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {

                    if (user.password.equals(iPassword)) {
                        preferences.setValue("nama", user.nama.toString())
                        preferences.setValue("user", user.username.toString())
                        preferences.setValue("url", user.url.toString())
                        preferences.setValue("email", user.email.toString())
                        preferences.setValue("saldo", user.saldo.toString())
                        preferences.setValue("status", "1")

                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity, "Password anda salah", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignInActivity, databaseError.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}