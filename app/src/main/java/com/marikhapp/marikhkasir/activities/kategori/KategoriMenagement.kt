package com.marikhapp.marikhkasir.activities.kategori


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.kategoriModels.Kategori
import kotlinx.android.synthetic.main.activity_kategori_menagement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class KategoriMenagement : AppCompatActivity(){

    var listKategori = ArrayList<KategoriObject>()
    var container = ArrayList<KategoriObject>()
    lateinit var CustomAdapter : KategoriMenagementAdapter
    var context = this@KategoriMenagement

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_menagement)

        textView3.text = "< Product Menagent"
        textView3.setOnClickListener {
            val intent = Intent(this,Main_Menu::class.java)
            startActivity(intent)
        }

        tambahKategori.setOnClickListener {
            val intent = Intent(this@KategoriMenagement, TambahEditKategori::class.java)
            intent.putExtra("mode","new")
            startActivity(intent)
        }

        val gridviewkategori = findViewById<View>(R.id.gridviewkategori) as GridView

        CustomAdapter = KategoriMenagementAdapter(listKategori, context)
        Client.instance.listkategori()
            .enqueue(object : Callback<List<Kategori>>{
                override fun onFailure(call: Call<List<Kategori>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<Kategori>>, response: Response<List<Kategori>>) {
                    val arrayKategori = response.body()!!
                    for(i in 1..arrayKategori.size){
                        listKategori.add(KategoriObject(arrayKategori[i-1].id_kat_produk,arrayKategori[i-1].nama_kat_produk))
                        container.add(KategoriObject(arrayKategori[i-1].id_kat_produk,arrayKategori[i-1].nama_kat_produk))
                    }
                    gridviewkategori.adapter =  CustomAdapter
                    CustomAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onBackPressed() {
        val intent = Intent(this,Main_Menu::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

}
