package com.marikhapp.marikhkasir.activities.transaksi.diskon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.diskon.DiskonObject
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import com.marikhapp.marikhkasir.activities.transaksi.qrscanner.QRscanner
import com.marikhapp.marikhkasir.api.AdminDatabase
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DiskonModels.Diskon
import kotlinx.android.synthetic.main.activity_diskon.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Diskon : AppCompatActivity() {

    val context : Context = this@Diskon
    val activity : Activity = this
    lateinit var adapter : DiskonAdapter
    val listDiskon = ArrayList<DiskonObject>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diskon)

        val gridviewDiskon = findViewById<GridView>(R.id.gridviewDiskon)

        buttonScanner.setOnClickListener {
            val qrScan =  IntentIntegrator(this)
            qrScan.setBeepEnabled(false)
            qrScan.captureActivity = QRscanner::class.java
            qrScan.setPrompt("")
            qrScan.initiateScan()
        }

        adapter = DiskonAdapter(listDiskon,context,activity)
        gridviewDiskon.adapter = adapter
        adapter.notifyDataSetChanged()
        listDiskon()
    }

    private fun listDiskon(){
        Client.instance.daftardiskon().enqueue(object : Callback<List<Diskon>> {
            override fun onFailure(call: Call<List<Diskon>>, t: Throwable) {
                Toast.makeText(this@Diskon,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Diskon>>, response: Response<List<Diskon>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data!!.isNotEmpty()){
                        for (item in data){
                             listDiskon.add(DiskonObject(item.id_disc,item.deskripsi_disc,item.nominal_disc))
                        }
                        adapter.notifyDataSetChanged()
                    }else{
                        Toast.makeText(this@Diskon,"Daftar Diskon Kosong",Toast.LENGTH_LONG).show()
                    }
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@Diskon,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //if qrcode has nothing in it
            if (result.contents == null) {
                Toast.makeText(this, "Scann QR dibatalkan", Toast.LENGTH_LONG).show()
            } else {
                //if qr contains data
                //convert string to array
                val scanResult = result.contents.toString()
                val arrayStringResult : List<String> = scanResult.split("-")
                var userID = ""
                var kode_voucher = ""
                for (i in 0..arrayStringResult.size){
                    userID = arrayStringResult[0]
                    kode_voucher = arrayStringResult[1]
                }
                Log.d("test123","User ID : $userID")
                Log.d("test123","Kode Voucher : $kode_voucher")
                if (userID.isNotEmpty() && kode_voucher.isNotEmpty()){
                    getNominalVoucher(kode_voucher,userID)
                }else if (userID.isNotEmpty() && kode_voucher.isEmpty()){
                    val sharedPreferencesID: SharedPreferences = activity.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
                    val editorID: SharedPreferences.Editor =  sharedPreferencesID.edit()
                    editorID.putString("id_pelanggan",userID)
                    if (editorID.commit()){
                        val intent = Intent(context, Transaksi::class.java)
                        intent.putExtra("Diskon",0)
                        activity.setResult(Activity.RESULT_OK,intent)
                        activity.finish()
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getNominalVoucher(kode_voucher:String,id_pelanggan:String){
        val nama_database = AdminDatabase().nama_database
        Client.instance.getVoucherByKodeVoucher(kode_voucher,nama_database).enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@Diskon,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        Toast.makeText(this@Diskon,"Scann QR-Code Berhasil",Toast.LENGTH_LONG).show()
                        val diskon = data
                        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
                        editor.putInt("saved_diskon",diskon)

                        val sharedPreferencesKV: SharedPreferences = activity.getSharedPreferences("KODE_VOUCHER",Context.MODE_PRIVATE)
                        val editorKV: SharedPreferences.Editor =  sharedPreferencesKV.edit()
                        editorKV.putString("kode_voucher",kode_voucher)

                        val sharedPreferencesID: SharedPreferences = activity.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
                        val editorID: SharedPreferences.Editor =  sharedPreferencesID.edit()
                        editorID.putString("id_pelanggan",id_pelanggan)

                        if (editor.commit() && editorKV.commit() && editorID.commit()){
                            val intent = Intent(context, Transaksi::class.java)
                            intent.putExtra("Diskon",diskon)
                            activity.setResult(Activity.RESULT_OK,intent)
                            activity.finish()
                        }
                    }
                }else{
                    Toast.makeText(this@Diskon,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}
