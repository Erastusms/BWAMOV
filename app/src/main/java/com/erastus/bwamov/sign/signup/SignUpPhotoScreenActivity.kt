package com.erastus.bwamov.sign.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.erastus.bwamov.home.HomeActivity
import com.erastus.bwamov.R
import com.erastus.bwamov.sign.signin.User
import com.erastus.bwamov.utils.Preferences
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*

import java.util.*

class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {

    var statusAdd:Boolean = false
    lateinit var filePath: Uri

    lateinit var storage : FirebaseStorage
    lateinit var storageReference : StorageReference
    lateinit var preferences: Preferences

    lateinit var user : User
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("User")

        user = intent.getParcelableExtra("data")
        tv_hello.text = "Selamat Datang\n"+ user.nama

        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_upload)
                iv_profile.setImageResource(R.drawable.circle_pic)
            } else {
                ImagePicker.with(this)
                        .cameraOnly()
                        .start()
            }
        }

        btn_home.setOnClickListener {
            finishAffinity()

            val goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        btn_save.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading.....")
                progressDialog.show()

                val ref = storageReference.child("images/"+ UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            saveToFirebase(it.toString())
                        }
                        finishAffinity()

                        val goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
                        startActivity(goHome)
                    }
                    .addOnFailureListener{
                        progressDialog.dismiss()
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
                        taskSnapshot -> var progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload "+ progress.toInt()+"%")
                    }

            } else {

            }
        }
    }

    private fun saveToFirebase(url: String) {


        mFirebaseDatabase.child(user.username!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user.url = url
                mFirebaseDatabase.child(user.username!!).setValue(user)

                preferences.setValue("nama", user.nama.toString())
                preferences.setValue("user", user.username.toString())
                preferences.setValue("url", url)
                preferences.setValue("saldo", user.saldo.toString())
                preferences.setValue("email", user.email.toString())
                preferences.setValue("status", "1")



                finishAffinity()
                val intent = Intent(this@SignUpPhotoScreenActivity,
                        HomeActivity::class.java)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpPhotoScreenActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
 //       Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
 //           takePictureIntent ->
 //           takePictureIntent.resolveActivity(packageManager)?.also {
 //               startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
 //           }
 //       }

        ImagePicker.with(this)
            .cameraOnly()	//User can only capture image using Camera
            .start()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan foto profil", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? Klik tombol upload nanti aja", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            filePath = data?.data!!
            statusAdd = true

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            btn_save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}