package com.marikhapp.marikhkasir.activities.transaksi.DeskripsiPesanan

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.deskripsi.DeskripsiObject
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DeskripsiModels.Deskripsi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeskripsiPesanan : AppCompatActivity() {
    val context : Context = this
    val arrayDaftarDeskripsi = ArrayList<DeskripsiObject>()
    lateinit var adapter : AdapterDeskripsiPesanan
    var jumlah : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deskripsi_pesanan)

        val id_kat_produk = intent.getStringExtra("id_kat_produk")

        val gridviewDaftarDeskripsiPesanan = findViewById<GridView>(R.id.gridviewDaftarDeskripsiPesanan)
        val buttonMin = findViewById<Button>(R.id.buttonMIN)
        val buttonPlus = findViewById<Button>(R.id.buttonPLUS)
        val editTextJumlahPesanan = findViewById<EditText>(R.id.editTextJumlahPesanan)


        buttonPlus.setOnClickListener {
            jumlah += 1
            editTextJumlahPesanan.setText(jumlah.toString())
        }
        buttonMin.setOnClickListener {
            if(jumlah < 0){
                jumlah = 0
            }else {
                jumlah -= 1
                editTextJumlahPesanan.setText(jumlah.toString())
            }
        }
        val qty = editTextJumlahPesanan.text.toString()
        Log.d("test123",qty)

        adapter = AdapterDeskripsiPesanan(arrayDaftarDeskripsi,context)

        Client.instance.daftardeskripsiproduk(id_kat_produk!!.toInt()).enqueue(object :
            Callback<List<Deskripsi>> {
            override fun onFailure(call: Call<List<Deskripsi>>, t: Throwable) {
                Toast.makeText(context,t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Deskripsi>>, response: Response<List<Deskripsi>>) {
                val data = response.body()
                for(i in 1..data!!.size){
                    arrayDaftarDeskripsi.add(DeskripsiObject(data[i-1].id_deskripsi,data[i-1].nama_deskripsi,data[i-1].id_kat_produk))
                }
                gridviewDaftarDeskripsiPesanan.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

    }
}
