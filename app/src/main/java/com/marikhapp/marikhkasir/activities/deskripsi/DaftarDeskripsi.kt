package com.marikhapp.marikhkasir.activities.deskripsi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.Toast
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.kategori.KategoriMenagement
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DeskripsiModels.Deskripsi
import kotlinx.android.synthetic.main.activity_daftar_deskripsi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarDeskripsi : AppCompatActivity() {

    lateinit var adapter: DeskripsiAdapter
    val context : Context = this
    val arrayDaftarDeskripsi = ArrayList<DeskripsiObject>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_deskripsi)

        val id_kat_produk = intent.getStringExtra("id_kat_produk")

        textViewDaftarDeskripsi.text = "< Daftar Deskripsi"
        textViewDaftarDeskripsi.setOnClickListener {
            val intent = Intent(this,KategoriMenagement::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        textViewTambahDeskripsi.setOnClickListener {
            val intent = Intent(this,TambahEditDeskripsi::class.java)
            intent.putExtra("id_kat_produk",id_kat_produk)
            intent.putExtra("mode","new")
            this.startActivity(intent)
        }

        val gridviewdaftardeskripsi = findViewById<View>(R.id.gridviewDaftarDeskripsi) as GridView
        adapter = DeskripsiAdapter(arrayDaftarDeskripsi,context)

        Client.instance.daftardeskripsiproduk(id_kat_produk!!.toInt()).enqueue(object : Callback<List<Deskripsi>>{
            override fun onFailure(call: Call<List<Deskripsi>>, t: Throwable) {
                Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Deskripsi>>, response: Response<List<Deskripsi>>) {
                val data = response.body()
                for(i in 1..data!!.size){
                    arrayDaftarDeskripsi.add(DeskripsiObject(data[i-1].id_deskripsi,data[i-1].nama_deskripsi,data[i-1].id_kat_produk))
                }
                gridviewdaftardeskripsi.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this,KategoriMenagement::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }
}
