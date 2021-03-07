package com.erastus.bwamov.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erastus.bwamov.R
import com.erastus.bwamov.sign.signin.User
import com.erastus.bwamov.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var sUsername : String
    lateinit var sPassword : String
    lateinit var sNama : String
    lateinit var sEmail : String

    lateinit var mFirebaseInstance : FirebaseDatabase
    lateinit var mDatabase: DatabaseReference
    lateinit var mDatabaseReference: DatabaseReference

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabaseReference = mFirebaseInstance.getReference("User")

        preferences = Preferences(this)

        btn_lanjutkan.setOnClickListener {
            sUsername = et_username.text.toString()
            sPassword = et_password.text.toString()
            sNama = et_nama.text.toString()
            sEmail = et_email.text.toString()

            if(sUsername.equals("")) {
                et_username.error = "Silahkan isi username anda"
                et_username.requestFocus()
            } else if(sPassword.equals("")) {
                et_password.error = "Silahkan isi password anda"
                et_password.requestFocus()
            } else if(sNama.equals("")) {
                et_nama.error = "Silahkan isi nama anda"
                et_nama.requestFocus()
            } else if(sEmail.equals("")) {
                et_email.error = "Silahkan isi email anda"
                et_email.requestFocus()
            } else {
                saveUsername(sUsername, sPassword, sNama, sEmail, "0", "https://www.cellmark.co.uk/media/1526/anonymous-avatar-icon-25.jpg")
            }
        }
    }

    private fun saveUsername(sUsername: String, sPassword: String, sNama: String, sEmail: String, saldo: String, url: String) {
        val user = User()
        user.username = sUsername
        user.password = sPassword
        user.nama = sNama
        user.email = sEmail
        user.saldo = saldo
        user.url = url

        if (sUsername != null) {
            checkingUsername(sUsername, user)
        }
    }

    private fun checkingUsername(iUsername: String, data: User) {
        mDatabaseReference.child(iUsername).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)

                if (user == null) {
                    mDatabaseReference.child(iUsername).setValue(data)

                    val goSignUpPhotoScreen = Intent(this@SignUpActivity,
                        SignUpPhotoScreenActivity::class.java).putExtra("nama", data.nama)
                    startActivity(goSignUpPhotoScreen)

                    Toast.makeText(this@SignUpActivity, "User berhasil dibuat", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@SignUpActivity, "User sudah digunakan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+databaseError.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}