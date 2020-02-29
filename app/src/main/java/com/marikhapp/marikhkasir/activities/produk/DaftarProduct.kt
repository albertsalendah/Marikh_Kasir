package com.marikhapp.marikhkasir.activities.produk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.kategori.KategoriMenagement
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.ProdukModels.Produk
import kotlinx.android.synthetic.main.activity_daftar_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DaftarProduct : AppCompatActivity() {

    lateinit var customadapterdaftarproduk : ProdukAdapter

    var context : Context = this
    val arrayProduk = ArrayList<ProdukObject>()
    val container = ArrayList<ProdukObject>()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_product)

        val kategoriproduct = intent.getStringExtra("kategoriproduct")
        val id_kat_produk = intent.getStringExtra("id_kat_produk")
        textView5.text = "< Daftar Product $kategoriproduct"

        textView5.setOnClickListener {
            val intent = Intent(this,KategoriMenagement::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        edittextTambahProduct.setOnClickListener {
            val intent = Intent(applicationContext, TambahEditProduct::class.java)
            intent.putExtra("kategoriproduct",kategoriproduct)
            intent.putExtra("id_kat_produk",id_kat_produk)
            intent.putExtra("mode","new")
            startActivity(intent)
        }

        val gridviewproduk = findViewById<View>(R.id.gridviewDaftarProduct) as GridView
        val stats = "daftarproduk"
        customadapterdaftarproduk = ProdukAdapter(arrayProduk,context,id_kat_produk!!,kategoriproduct!!,stats)

        Client.instance.daftarproduk(id_kat_produk.toInt())
            .enqueue(object : Callback<List<Produk>>{
                override fun onFailure(call: Call<List<Produk>>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<Produk>>, response: Response<List<Produk>>) {
                    val jumlahdata = response.body()
                    for(i in 1..jumlahdata!!.size){

                        customadapterdaftarproduk.notifyDataSetChanged()
                        arrayProduk.add(ProdukObject(jumlahdata[i-1].id_produk,jumlahdata[i-1].nama_produk,jumlahdata[i-1].harga_produk,jumlahdata[i-1].gbr_produk))
                        container.add(ProdukObject(jumlahdata[i-1].id_produk,jumlahdata[i-1].nama_produk,jumlahdata[i-1].harga_produk,jumlahdata[i-1].gbr_produk))
                    }
                    gridviewproduk.adapter = customadapterdaftarproduk
                }

            })

        searchMakanan.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s!!)
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s!!)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                filter(s!!)
            }

        })

    }

    private fun filter(text: CharSequence) {
        arrayProduk.clear()
        if(text.isNotEmpty()){
            for (item in container) {
                if (item.nama_produk.toLowerCase().contains(text)) {
                    arrayProduk.add(item)
                }
            }
        }else{
            arrayProduk.addAll(container)
        }
        customadapterdaftarproduk.notifyDataSetChanged()
        return
    }

    override fun onBackPressed() {
        val intent = Intent(this,KategoriMenagement::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }

}
