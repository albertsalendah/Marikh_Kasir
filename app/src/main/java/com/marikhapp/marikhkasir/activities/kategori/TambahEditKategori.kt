package com.marikhapp.marikhkasir.activities.kategori

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.kategoriModels.UpdateKategoriResponse
import kotlinx.android.synthetic.main.activity_tambah_edit_kategori.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TambahEditKategori : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_edit_kategori)
        val mode = intent.getStringExtra("mode")
        val id_kat_produk = intent.getStringExtra("id_kat_produk")
        val nama_kat_produk = intent.getStringExtra("nama_kat_produk")
        if(mode == "new"){
            modeKategori.text = "< $mode Category"
            tambahKategori()
        }else if(mode == "edit"){
            modeKategori.text = "< $mode Category $nama_kat_produk"
            editKategori(id_kat_produk!!.toInt(),nama_kat_produk!!)
        }
        modeKategori.setOnClickListener {
            val intent = Intent(this,KategoriMenagement::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun tambahKategori(){
        buttonTambahEditKategori.text = "Tambah Kategori"
        buttonTambahEditKategori.setOnClickListener {
            val namakategori = editTextTambahEditKategori.text.toString().toUpperCase()

            if (namakategori.isEmpty()){
                editTextTambahEditKategori.error = "Nama Kategori Kosong"
                editTextTambahEditKategori.requestFocus()
                return@setOnClickListener
            }

            Client.instance.tambahkategori(namakategori)
                .enqueue(object : Callback<UpdateKategoriResponse> {
                    override fun onFailure(call: Call<UpdateKategoriResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                        Log.d("test123",t.message.toString())
                    }

                    override fun onResponse(call: Call<UpdateKategoriResponse>, response: Response<UpdateKategoriResponse>) {
                        if(response.isSuccessful){
                            response.body()
                            val message = response.body()!!.message
                            Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, KategoriMenagement::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else  {
                            Toast.makeText(applicationContext,response.errorBody()!!.string(),Toast.LENGTH_SHORT).show()
                        }
                    }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun editKategori(id_kat_produk:Int,nama_kat_produk:String){

        buttonTambahEditKategori.text = "Edit Kategori"
        editTextTambahEditKategori.setText(nama_kat_produk)
        buttonTambahEditKategori.setOnClickListener {
            val nama_kat_produk = editTextTambahEditKategori.text.toString().trim()

            if (nama_kat_produk.isEmpty()){
                editTextTambahEditKategori.error = "Nama Kategori Kosong"
                editTextTambahEditKategori.requestFocus()
                return@setOnClickListener
            }

            Client.instance.updatekategori(id_kat_produk,nama_kat_produk)
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, KategoriMenagement::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                })
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,KategoriMenagement::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }
}
