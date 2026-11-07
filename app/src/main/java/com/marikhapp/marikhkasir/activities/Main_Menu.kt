package com.marikhapp.marikhkasir.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.diskon.Diskon_Management
import com.marikhapp.marikhkasir.activities.kategori.KategoriMenagement
import com.marikhapp.marikhkasir.activities.member.Member
import com.marikhapp.marikhkasir.activities.methodbayar.MethodBayar
import com.marikhapp.marikhkasir.activities.purchasing.PurchasingTransaction
import com.marikhapp.marikhkasir.activities.settings.Settings
import com.marikhapp.marikhkasir.activities.transaksi.ModalHarian.ModalHarian
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.DetailPenjualan.DetailPenjualan
import com.marikhapp.marikhkasir.models.SaldoHarianModels.SaldoHarian
import com.marikhapp.marikhkasir.models.SettingModels.Setting
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import com.marikhapp.marikhkasir.models.userModels.User
import kotlinx.android.synthetic.main.activity_main__menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NAME_SHADOWING")
class Main_Menu : AppCompatActivity() {
    val arrayId_Trx_Penjualah = ArrayList<String>()
    val arrayId_Trx_PenjualahInDetailTRX = ArrayList<String>()
    val failed_id =  ArrayList<String>()
    var jenis_user = ""
    @SuppressLint("SimpleDateFormat", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__menu)

        val preferences = getSharedPreferences("USER_NAME", Context.MODE_PRIVATE)
        val user_name = preferences.getString("user_name", "").toString()

        loadingMainMenu.visibility = View.VISIBLE

        Client.instance.getUser(user_name).enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    jenis_user = response.body()!!.jenis_user
                    if (jenis_user == "kasir"){
                        buttonprodukmenajement.isClickable = false
                        buttonSettings.isClickable = false
                        buttonPurchasing.isClickable = false
                        buttonMember.isClickable = false
                    }
                }else{
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })
        cekSetting()

        textViewMainMenu.setOnClickListener {
            this.finish()
        }
        buttonMember.setOnClickListener {
            val intent = Intent(this, Member::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        buttonprodukmenajement.setOnClickListener {
            val intent = Intent(this, KategoriMenagement::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonSettings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonPurchasing.setOnClickListener {
            val intent = Intent(this, PurchasingTransaction::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonTransaksi.setOnClickListener {
            checkTglInputSaldoHarian()
        }

        buttonDiskonManagement.setOnClickListener {
            val intent = Intent(this, Diskon_Management::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonPaymentMethod.setOnClickListener {
            val intent = Intent(this, MethodBayar::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonLogOut.setOnClickListener {
            val preferences = getSharedPreferences("USER_NAME", 0)
            preferences.edit().remove("user_name").apply()
            val intent = Intent(applicationContext,Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }



    @SuppressLint("SimpleDateFormat")
    private fun checkTglInputSaldoHarian(){
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val tglhariini: String = formatter.format(date)

        Client.instance.getReportSaldoHarian(tglhariini).enqueue(object : Callback<SaldoHarian>{
            override fun onFailure(call: Call<SaldoHarian>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<SaldoHarian>, response: Response<SaldoHarian>) {
                if(response.body()!!.tgl_report_sh != tglhariini){
                    val intent = Intent(applicationContext,ModalHarian::class.java)
                    intent.putExtra("comefrom","main_menu")
                    startActivity(intent)
                }else if(response.body()!!.tgl_report_sh == tglhariini){
                    val intent = Intent(this@Main_Menu,Transaksi::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    this@Main_Menu.startActivity(intent)
                }
            }
        })
    }

    private fun cekSetting(){
        Client.instance.settings().enqueue(object : Callback<List<Setting>>{
            override fun onFailure(call: Call<List<Setting>>, t: Throwable) {
                Toast.makeText(this@Main_Menu,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Setting>>, response: Response<List<Setting>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        if (data.isEmpty()){
                            Log.d("test123","Data Kosong")
                            val modal_awal = "0"
                            val tax = "0"
                            val gofoodph = "0"
                            val gofoodec = "0"
                            val gofoodkomisi = "0"
                            val grabfoodph = "0"
                            val grabfoodec = "0"
                            val grabfoodkomisi = "0"
                            Client.instance.insertSetting(modal_awal,tax,gofoodph,gofoodec,gofoodkomisi,grabfoodph,grabfoodec,grabfoodkomisi).enqueue(object : Callback<DefaultResponse>{
                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    Toast.makeText(this@Main_Menu,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                                }

                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                    if (response.isSuccessful){
                                        Toast.makeText(this@Main_Menu,"All Setting OK",Toast.LENGTH_SHORT).show()
                                        loadingMainMenu.visibility = View.GONE
                                        getListTRX()
                                    }else{
                                        Toast.makeText(this@Main_Menu,"Terjadi Kesalahan Saat Memuat Data Setting",Toast.LENGTH_LONG).show()
                                    }
                                }
                            })
                        }else{
                            loadingMainMenu.visibility = View.GONE
                            Toast.makeText(this@Main_Menu,"All Setting OK",Toast.LENGTH_SHORT).show()
                            Log.d("test123","Setting OK")
                            getListTRX()
                        }
                    }
                }
            }
        })
    }

    private fun getListTRX(){
        Client.instance.daftartrxpenjualan().enqueue(object : Callback<List<TrxPenjualan>>{
            override fun onFailure(call: Call<List<TrxPenjualan>>, t: Throwable) {
                Toast.makeText(this@Main_Menu,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<TrxPenjualan>>, response: Response<List<TrxPenjualan>>) {
                if (response.isSuccessful){
                    val data  = response.body()
                    if (data != null){
                        for (item in data){
                            arrayId_Trx_Penjualah.add(item.id_trx_penjualan)
                        }
                    }
                    getListDetailTRX()
                }
            }
        })
    }

    private fun getListDetailTRX(){
        Client.instance.daftarDetailPenjualan().enqueue(object : Callback<List<DetailPenjualan>>{
            override fun onFailure(call: Call<List<DetailPenjualan>>, t: Throwable) {
                Toast.makeText(this@Main_Menu,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<DetailPenjualan>>, response: Response<List<DetailPenjualan>>) {
               if (response.isSuccessful){
                   val data  = response.body()
                   if (data != null){
                       for (item in data){
                          arrayId_Trx_PenjualahInDetailTRX.add(item.id_trx_penjualan)
                       }
                   }
                   cekTransaksi()
               }
            }
        })
    }
    private fun cekTransaksi(){
        val container  = ArrayList<String>()
        for (item in arrayId_Trx_Penjualah){
            var duplicate  = false
            for (item2 in arrayId_Trx_PenjualahInDetailTRX){
                if (item == item2){
                    duplicate = true
                }
            }
            if (!duplicate){
                container.add(item)
            }
        }
        if (container.isNotEmpty()){
            loadingMainMenu.visibility = View.VISIBLE
            Toast.makeText(this@Main_Menu,"Transaksi Gagal Ditemukan ${container.size}",Toast.LENGTH_LONG).show()
            Toast.makeText(this@Main_Menu,"Mulai Proses Menghapus...",Toast.LENGTH_LONG).show()
            for (item in container){
                hupusTransaksiyangGagal(item)
            }
        }else{
            loadingMainMenu.visibility = View.GONE
            Toast.makeText(this@Main_Menu,"Tidak ada Transaksi yang Gagal",Toast.LENGTH_LONG).show()
        }
        Log.d("test123","Jumlah Transaksi Gagal Diniput = ${container.size}")
    }

    private fun hupusTransaksiyangGagal(id_trx_penjualan:String){
        Client.instance.hapusTrxPenjualan(id_trx_penjualan).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                loadingMainMenu.visibility = View.GONE
                Toast.makeText(this@Main_Menu,"Terjadi Kesalahan Saat Menghapus Transaksi yang Gagal",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    loadingMainMenu.visibility = View.GONE
                    Toast.makeText(this@Main_Menu,response.body()!!.message,Toast.LENGTH_LONG).show()
                }else{
                    loadingMainMenu.visibility = View.GONE
                    Toast.makeText(this@Main_Menu,"Terjadi Kesalahan Saat Menghapus Transaksi yang Gagal",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

}
