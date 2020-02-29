package com.marikhapp.marikhkasir.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.kategori.KategoriMenagement
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import kotlinx.android.synthetic.main.activity_main__menu.*

class Main_Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__menu)
        textViewMainMenu.setOnClickListener {
            this.finish()
        }
        button8.setOnClickListener {
            val intent = Intent(this, KategoriMenagement::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        button5.setOnClickListener {
            val intent = Intent(this,Transaksi::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

}
