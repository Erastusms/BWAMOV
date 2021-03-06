package com.erastus.bwamov.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.erastus.bwamov.R
import com.erastus.bwamov.model.Checkout
import com.erastus.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckOutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0
    private lateinit var preferences : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        preferences = Preferences(this)
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>

        for (a in dataList.indices) {
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(Checkout("Total harus dibayar", total.toString()))

        rv_checkout.layoutManager = LinearLayoutManager(this)
        rv_checkout.adapter = CheckoutAdapter(dataList) {

        }

        btn_tiket.setOnClickListener {
            val intent = Intent(this, CheckOutSuccessActivity::class.java)
            startActivity(intent)
        }

        btn_batalkan.setOnClickListener {
            finish()
        }
    }
}