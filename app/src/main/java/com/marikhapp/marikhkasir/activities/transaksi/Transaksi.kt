package com.marikhapp.marikhkasir.activities.transaksi

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.activities.transaksi.ui.main.SectionsPagerAdapter
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.kategoriModels.Kategori
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Transaksi : AppCompatActivity() {
    var namaKat = ArrayList<String>()
    lateinit var sectionsPagerAdapter : SectionsPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)


        val viewPager: ViewPager = this.findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        //sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager,3)


        Client.instance.listkategori().enqueue(object : Callback<List<Kategori>>{
            override fun onFailure(call: Call<List<Kategori>>, t: Throwable) {
                Toast.makeText(this@Transaksi,"Gagal Memuat Data",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Kategori>>, response: Response<List<Kategori>>) {
                if (response.isSuccessful){
                    for (item in 1..response.body()!!.size){
                        namaKat.add(response.body()!![item-1].nama_kat_produk)
                        sectionsPagerAdapter = SectionsPagerAdapter(this@Transaksi, supportFragmentManager,namaKat)
                    }
                    viewPager.adapter = sectionsPagerAdapter
                }
            }
        })



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,Main_Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }
}