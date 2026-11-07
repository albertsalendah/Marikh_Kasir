@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.marikhapp.marikhkasir.activities.transaksi.deskripsiPesanan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.deskripsi.DeskripsiObject
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DeskripsiModels.Deskripsi
import kotlinx.android.synthetic.main.activity_deskripsi_pesanan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeskripsiPesanan : AppCompatActivity() {
    val context : Context = this@DeskripsiPesanan
    var activity: Activity = this
    val arrayDaftarDeskripsi = ArrayList<DeskripsiObject>()
    lateinit var adapter : AdapterDeskripsiPesanan
    var jumlah = 1
    var deskripsi = ArrayList<Int>()
    var indexpesanan = ""
    var diskon_produk : Int? = 0
    var tgl_awal_diskon : String? = ""
    var tgl_akhir_diskon : String? = ""
    var diskon_olfood : Int? = 0
    var tgl_awal_diskon_olfood : String? = ""
    var tgl_akhir_diskon_olfood : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deskripsi_pesanan)
        val comefrom = intent.getStringExtra("comefrom")
        val id_produk = intent.getStringExtra("id_produk")
        val nama_produk = intent.getStringExtra("nama_produk")
        val harga_produk = intent.getStringExtra("harga_produk")
        val id_kat_produk = intent.getStringExtra("id_kat_produk")

        val gridviewDaftarDeskripsiPesanan = findViewById<GridView>(R.id.gridviewDaftarDeskripsiPesanan)
        val buttonMin = findViewById<Button>(R.id.buttonMIN)
        val buttonPlus = findViewById<Button>(R.id.buttonPLUS)
        val editTextJumlahPesanan = findViewById<EditText>(R.id.editTextJumlahPesanan)
        val button = findViewById<Button>(R.id.buttonInputDeskripsiPesanan)

        if (comefrom == "daftarproduk"){
            editTextJumlahPesanan.setText("1")
            diskon_produk = intent.getIntExtra("diskon_produk",0)
            tgl_awal_diskon = intent.getStringExtra("tgl_awal_diskon")
            tgl_akhir_diskon = intent.getStringExtra("tgl_akhir_diskon")
            diskon_olfood = intent.getIntExtra("diskon_olfood",0)
            tgl_awal_diskon_olfood = intent.getStringExtra("tgl_awal_diskon_olfood")
            tgl_akhir_diskon_olfood = intent.getStringExtra("tgl_akhir_diskon_olfood")
        }else{
            val jumlah_pesanan = intent.getStringExtra("jumlah_pesanan")
             deskripsi = intent.getIntegerArrayListExtra("idDeskripsi")!!
            indexpesanan = intent.getStringExtra("indexpesanan").toString()
            jumlah = jumlah_pesanan!!.toInt()
            editTextJumlahPesanan.setText(jumlah.toString())
        }

        adapter = AdapterDeskripsiPesanan(arrayDaftarDeskripsi,context,comefrom,deskripsi)

        Client.instance.daftardeskripsiproduk(id_kat_produk!!.toInt()).enqueue(object : Callback<List<Deskripsi>> {
            override fun onFailure(call: Call<List<Deskripsi>>, t: Throwable) {
                Toast.makeText(context,t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Deskripsi>>, response: Response<List<Deskripsi>>) {
                val data = response.body()!!
                for(item in data){
                        arrayDaftarDeskripsi.add(DeskripsiObject(item.id_deskripsi,item.nama_deskripsi,item.id_kat_produk))
                }
                gridviewDaftarDeskripsiPesanan.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

        buttonPlus.setOnClickListener{
            jumlah += 1
            editTextJumlahPesanan.setText(jumlah.toString())
        }
        buttonMin.setOnClickListener {
            if(jumlah < 2){
                jumlah = 1
                editTextJumlahPesanan.setText(jumlah.toString())
            }else {
                jumlah -= 1
                editTextJumlahPesanan.setText(jumlah.toString())
            }
        }

        button.setOnClickListener{
                val addnotes : String? = editTextAddNotes.text.toString()
                 val intent = Intent(activity, Transaksi::class.java)
                intent.putExtra("id_produk",id_produk)
                intent.putExtra("nama_produk",nama_produk)
                intent.putExtra("harga_produk",harga_produk)
                intent.putExtra("id_kat_produk",id_kat_produk)
                intent.putExtra("jumlah_pesanan",jumlah.toString())
                intent.putExtra("diskon_produk",diskon_produk)
                intent.putExtra("tgl_awal_diskon",tgl_awal_diskon)
                intent.putExtra("tgl_akhir_diskon",tgl_akhir_diskon)
            intent.putExtra("diskon_olfood",diskon_olfood)
            intent.putExtra("tgl_awal_diskon_olfood",tgl_awal_diskon_olfood)
            intent.putExtra("tgl_akhir_diskon_olfood",tgl_akhir_diskon_olfood)
                intent.putStringArrayListExtra("daftardeskripsi",adapter.getDeskripsi())
                intent.putIntegerArrayListExtra("daftarIdDeskripsi",adapter.getIdDeskripsi())
                intent.putExtra("indexpesanan",indexpesanan)
                intent.putExtra("addnotes",addnotes)
                activity.setResult(Activity.RESULT_OK,intent)
                activity.finish()

       }
    }
}
