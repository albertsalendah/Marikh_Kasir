package com.marikhapp.marikhkasir.activities.deskripsi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import kotlinx.android.synthetic.main.activity_tambah_edit_deskripsi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahEditDeskripsi : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_edit_deskripsi)
        val id_kat_produk = intent.getStringExtra("id_kat_produk")
        val id_deskripsi = intent.getStringExtra("id_deskripsi")
        val namadeskripsi = intent.getStringExtra("nama_deskripsi")
        val mode = intent.getStringExtra("mode")

        modeDeskripsi.setOnClickListener {
            val intent = Intent(this,DaftarDeskripsi::class.java)
            intent.putExtra("id_kat_produk",id_kat_produk)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(intent)
        }

        if(mode == "new"){
            modeDeskripsi.text = "< New Deskripsi"
            buttonTambahEditDeskripsi.text = "Tambah Deskrpsi"
        }else if(mode == "edit"){
            modeDeskripsi.text = "< Edit Deskripsi"
            buttonTambahEditDeskripsi.text = "Edit Deskrpsi"
            editTextTambahEditDeskripsi.setText(namadeskripsi)
        }

        buttonTambahEditDeskripsi.setOnClickListener {
            val namadeskripsi = editTextTambahEditDeskripsi.text.toString()
            if(namadeskripsi.isEmpty()){
                editTextTambahEditDeskripsi.error = "Nama Deskripsi Kosong"
                editTextTambahEditDeskripsi.requestFocus()
                return@setOnClickListener
            }
            if(mode == "new"){
                tambahDeskripsi(namadeskripsi,id_kat_produk)
            }else if(mode == "edit"){
                editDeskripsi(namadeskripsi,id_deskripsi,id_kat_produk)
            }
        }

    }

    private fun tambahDeskripsi(namadeskripsi : String,id_kat_produk : String){
        Client.instance.tambahdeskripsiproduk(namadeskripsi,id_kat_produk.toInt()).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"Deskripsi Berhasil Ditambah",Toast.LENGTH_LONG).show()
                    val view = View(this@TambahEditDeskripsi)
                    val intent = Intent(view.context,DaftarDeskripsi::class.java)
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    view.context.startActivity(intent)
                }else{
                    Toast.makeText(applicationContext,"Gagal Menambahkan Data",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun editDeskripsi(namadeskripsi : String,id_deskripsi : String,id_kat_produk: String){
        Client.instance.updatedeskripsiproduk(id_deskripsi.toInt(),namadeskripsi).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"Deskripsi Berhasil Diedit",Toast.LENGTH_LONG).show()
                    val view = View(this@TambahEditDeskripsi)
                    val intent = Intent(view.context,DaftarDeskripsi::class.java)
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    view.context.startActivity(intent)
                }else{
                    Toast.makeText(applicationContext,"Gagal Mengubah Data",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onBackPressed() {
        val id_kat_produk = intent.getStringExtra("id_kat_produk")
        val intent = Intent(this,DaftarDeskripsi::class.java)
        intent.putExtra("id_kat_produk",id_kat_produk)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }
}
