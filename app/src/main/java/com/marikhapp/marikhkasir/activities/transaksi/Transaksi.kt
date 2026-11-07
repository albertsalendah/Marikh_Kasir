@file:Suppress("LocalVariableName")

package com.marikhapp.marikhkasir.activities.transaksi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.activities.produk.ProdukObject
import com.marikhapp.marikhkasir.activities.transaksi.Bayar.Bayar
import com.marikhapp.marikhkasir.activities.transaksi.ModalHarian.ModalHarian
import com.marikhapp.marikhkasir.activities.transaksi.deskripsiPesanan.DeskripsiPesanan
import com.marikhapp.marikhkasir.activities.transaksi.diskon.Diskon
import com.marikhapp.marikhkasir.activities.transaksi.openBill.OpenBill
import com.marikhapp.marikhkasir.activities.transaksi.ui.main.PlaceholderFragment
import com.marikhapp.marikhkasir.activities.transaksi.ui.main.SectionsPagerAdapter
import com.marikhapp.marikhkasir.api.AdminDatabase
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.PelangganModels.Pelanggan
import com.marikhapp.marikhkasir.models.SaldoHarianModels.SaldoHarian
import com.marikhapp.marikhkasir.models.SettingModels.Setting
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualanRespon
import com.marikhapp.marikhkasir.models.kategoriModels.Kategori
import com.marikhapp.marikhkasir.models.userModels.UserToken
import kotlinx.android.synthetic.main.activity_transaksi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NAME_SHADOWING", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class Transaksi : AppCompatActivity(), PlaceholderFragment.OnDataPass {
    var namaKat = ArrayList<String>()
    lateinit var sectionsPagerAdapter : SectionsPagerAdapter
    private val context : Context = this@Transaksi
    private var activity: Activity = this
    private var arrayPesanan = ArrayList<PesananObject>()
    var produkClickStatus:Boolean = false
    lateinit var adapter : PesananAdapter
    var subTotal = 0
    var dis = 0
    var arrayNamaPelanggan = ArrayList<String?>()
    val arrayPelanggan = ArrayList<PelangganObject>()
    var id_pelanggan : String = ""
    var namapelangggan : String = ""
    val arrayId_Trx_Penjualah = ArrayList<Int>()
    var jclick = 1
    var update_pesanan = false
    var upadateTagihanPesanan = 0
    var jumalahpesanan = 0
    var saved_diskon = 0
    var tax = 0
    var tax_openbill = 0
    var selected_jenisTRansaksi = ""
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        newtransaksi2.text = "< New Transaksi"
        prpgresBarTransaksi.visibility = View.VISIBLE

        newtransaksi.setOnClickListener {
            prpgresBarTransaksi.visibility = View.VISIBLE
            cekSetting()
            val kode_vocher = getSharedPreferences("KODE_VOUCHER", 0)
            val id_pelanggan = getSharedPreferences("ID_PELANGGAN",0)
            if(kode_vocher.edit().commit() && id_pelanggan.edit().commit()){
                cekSetting()
            }
            val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
            sharedPreferencesID.edit().remove("id_pelanggan").apply()
        }
        val preferences = this.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
        preferences.edit().remove("saved_diskon").apply()

        val viewPager: ViewPager = this.findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val textViewSub = findViewById<TextView>(R.id.textViewSub)

        //Set Adapter Tab Kategori
        sectionsPagerAdapter = SectionsPagerAdapter(this@Transaksi, supportFragmentManager,namaKat)
        viewPager.adapter = sectionsPagerAdapter

        daftarKategori(viewPager)

        //Set Adapter Pesanan
        val LayoutManager = LinearLayoutManager(this)
        adapter = PesananAdapter(arrayPesanan, context,activity,textViewSub,update_pesanan,upadateTagihanPesanan)
        listViewPesanan.layoutManager = LayoutManager
        listViewPesanan.adapter = adapter
    }

    private fun cekSetting(){
        Client.instance.settings().enqueue(object : Callback<List<Setting>>{
            override fun onFailure(call: Call<List<Setting>>, t: Throwable) {
                Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Setting>>, response: Response<List<Setting>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        if (data.isEmpty()){
                            Toast.makeText(context,"Anda Belum Melakukan Pengaturan Awal Di Menu Setting",Toast.LENGTH_LONG).show()
                            val intent = Intent(this@Transaksi,Main_Menu::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else{
                            for (item in data) {
                                val id = item.id
                                val modal_awal = item.modal_awal
                                val tax = item.tax
                                val gofoodph = item.gofoodph
                                val gofoodec = item.gofoodec
                                val gofoodkomisi = item.gofoodkomisi
                                val grabfoodph = item.grabfoodph
                                val grabfoodec = item.grabfoodec
                                val grabfoodkomisi = item.grabfoodkomisi

                                val preferencesTax = getSharedPreferences("TAX",Context.MODE_PRIVATE)
                                val editorTax = preferencesTax.edit()
                                editorTax.putInt("tax",tax.toInt())

                                val preferencesModalAwal = getSharedPreferences("MODAL_AWAL",Context.MODE_PRIVATE)
                                val editorModalAwal = preferencesModalAwal.edit()
                                editorModalAwal.putInt("modal_awal",modal_awal.toInt())

                                val preferencesGoFood = getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
                                val editorGoFood = preferencesGoFood.edit()
                                editorGoFood.putInt("PenyesuaianHargaGoFood", gofoodph.toInt())
                                editorGoFood.putInt("ExtraChargeGoFood",gofoodec.toInt())
                                editorGoFood.putInt("KomisiGoFood",gofoodkomisi.toInt())

                                val preferencesGrabfood = getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
                                val editorGrabFood = preferencesGrabfood.edit()
                                editorGrabFood.putInt("PenyesuaianHargaGrabFood", grabfoodph.toInt())
                                editorGrabFood.putInt("ExtraChargeGrabFood",grabfoodec.toInt())
                                editorGrabFood.putInt("KomisiGrabFood",grabfoodkomisi.toInt())

                                if (editorTax.commit() && editorModalAwal.commit() && editorGoFood.commit() && editorGrabFood.commit()){
                                    /*Log.d("test123","Setting GoFood OK")
                                    Log.d("test123","Setting Modal Awal OK")
                                    Log.d("test123","Setting TAX OK")
                                    Log.d("test123","Setting GrabFood OK")*/
                                    startTransaksi()
                                }else{
                                    val intent = Intent(this@Transaksi,Main_Menu::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }else{
                    Toast.makeText(context,"Anda Belum Melakukan Pengaturan Awal Di Menu Setting",Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Transaksi,Main_Menu::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun startTransaksi(){
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val tglhariini: String = formatter.format(date)
        Client.instance.getReportSaldoHarian(tglhariini)
            .enqueue(object : Callback<SaldoHarian> {
                override fun onFailure(call: Call<SaldoHarian>, t: Throwable) {
                    Toast.makeText(applicationContext, "Terjadi Kesalahan Saat Memuat Data", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<SaldoHarian>, response: Response<SaldoHarian>) {
                    if (response.isSuccessful){
                        prpgresBarTransaksi.visibility = View.GONE
                        if (response.body()!!.saldo_awal == 0) {
                            produkClickStatus = false
                            newtransaksi2.visibility = View.GONE
                            newtransaksi2.isClickable = false
                            buttonDiskon.isClickable = false
                            buttonOpenBill.isClickable = false
                            buttonListOpenBill.isClickable = false
                            textViewTotalHarga.isClickable = false
                            checkSaldoHarian(tglhariini)
                        } else if (response.body()!!.saldo_awal != 0) {
                            newtransaksi.visibility = View.VISIBLE
                            radioButton.isEnabled = true
                            radioButton2.isEnabled = true
                            editTextNoMeja.isEnabled = true
                            buttonOpenBill.isEnabled = true
                            buttonListOpenBill.isEnabled = true
                            checkSaldoHarian(tglhariini)
                        }
                    }else{
                        prpgresBarTransaksi.visibility = View.GONE
                        Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkSaldoHarian(tglhariini: String){
        prpgresBarTransaksi.visibility = View.VISIBLE
        Client.instance.getReportSaldoHarian(tglhariini).enqueue(object : Callback<SaldoHarian>{
            override fun onFailure(call: Call<SaldoHarian>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                prpgresBarTransaksi.visibility = View.GONE
            }
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<SaldoHarian>, response: Response<SaldoHarian>) {
                    if (response.isSuccessful){
                        prpgresBarTransaksi.visibility = View.GONE
                        if(response.body()!!.saldo_awal == 0){
                            val intent = Intent(applicationContext,ModalHarian::class.java)
                            intent.putExtra("comefrom","transaksi")
                            startActivityForResult(intent,3)
                        }else if(response.body()!!.saldo_awal != 0){
                            produkClickStatus = true
                            newtransaksi.visibility = View.INVISIBLE
                            newtransaksi2.visibility = View.VISIBLE
                            newtransaksi2.isClickable = true
                            buttonDiskon.isClickable = true
                            buttonOpenBill.isClickable = true
                            buttonListOpenBill.isClickable = true
                            textViewTotalHarga.isClickable = true
                            switchOlfood.isEnabled = true
                            editTextNamaPelanggan.isEnabled = true
                            buttonDiskon.isEnabled = true
                            switchOlfood.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked){
                                    if (arrayPesanan.size > 0){
                                        arrayPesanan.clear()
                                        adapter.notifyDataSetChanged()
                                    }
                                    radioButton.isEnabled = false
                                    radioButton2.isEnabled = false
                                    //editTextNoMeja.isEnabled = false
                                    buttonOpenBill.isEnabled = false
                                    buttonListOpenBill.isEnabled = false
                                    radioButton.isSelected = false
                                    radioButton2.isSelected = false
                                    textViewSub.text = "0"
                                    textViewTotalHarga.text = "0"
                                    textViewDiskon.text = "0"
                                    textViewTax.text = "0"
                                    editTextNamaPelanggan.setText("")
                                    radioGroupPesanan.visibility = View.GONE
                                    radioGroupOlfood.visibility = View.VISIBLE
                                    textView25.text = "No.Pesanan"
                                    editTextNoMeja.setText("0")
                                    val preferences = getSharedPreferences("DISKON",Context.MODE_PRIVATE)
                                    preferences.edit().remove("saved_diskon").apply()
                                }else{
                                    if (arrayPesanan.size > 0){
                                        arrayPesanan.clear()
                                        adapter.notifyDataSetChanged()
                                    }
                                    textView25.text = "Table"
                                    radioGroupOlfood.visibility = View.GONE
                                    radioGroupPesanan.visibility = View.VISIBLE
                                    radioButton.isEnabled = true
                                    radioButton2.isEnabled = true
                                    editTextNoMeja.isEnabled = true
                                    buttonOpenBill.isEnabled = true
                                    buttonListOpenBill.isEnabled = true
                                    radioButton.isSelected = true
                                    textViewSub.text = "0"
                                    textViewTotalHarga.text = "0"
                                    textViewDiskon.text = "0"
                                    textViewTax.text = "0"
                                    editTextNamaPelanggan.setText("")
                                    editTextNoMeja.setText("1")
                                    val preferences = getSharedPreferences("DISKON",Context.MODE_PRIVATE)
                                    preferences.edit().remove("saved_diskon").apply()
                                }
                            }
                            newtransaksi2.setOnClickListener {
                                val intent = Intent(this@Transaksi,Main_Menu::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            textViewMemberGuest.setOnClickListener {
                                jclick += 1
                                if (jclick % 2 == 0){
                                    textViewMemberGuest.text = "Member-Name : "
                                    prpgresBarTransaksi.visibility = View.VISIBLE
                                    getMember()
                                }else{
                                    textViewMemberGuest.text = "Guest-Name : "
                                    editTextNamaPelanggan.setAdapter(null)
                                }
                            }

                            buttonListOpenBill.setOnClickListener {
                                val intent = Intent(activity,OpenBill::class.java)
                                activity.startActivityForResult(intent,6)
                            }
                            //------------------------------------------------------------------------------
                            prpgresBarTransaksi.visibility = View.VISIBLE
                            Client.instance.daftartrxpenjualan().enqueue(object : Callback<List<TrxPenjualan>>{
                                override fun onFailure(call: Call<List<TrxPenjualan>>, t: Throwable) {
                                    prpgresBarTransaksi.visibility = View.GONE
                                }

                                override fun onResponse(call: Call<List<TrxPenjualan>>, response: Response<List<TrxPenjualan>>) {
                                    Log.d("test123",response.message())
                                    if (response.isSuccessful){
                                        prpgresBarTransaksi.visibility = View.GONE
                                        val listtransaksi = response.body()
                                        for (item in listtransaksi!!){
                                            arrayId_Trx_Penjualah.add(item.id_trx_penjualan.toInt())
                                        }
                                    }else{
                                        Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data ${arrayId_Trx_Penjualah.size}",Toast.LENGTH_LONG).show()
                                        return
                                    }
                                }
                            })
                            //------------------------------------------------------------------------------
                        }
                    }else{
                        prpgresBarTransaksi.visibility = View.GONE
                        Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                 }
            }
        })
    }

    private fun getMember(){
        Client.instance.daftarpelanggan().enqueue(object : Callback<List<Pelanggan>>{
            override fun onFailure(call: Call<List<Pelanggan>>, t: Throwable) {
                Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                prpgresBarTransaksi.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<Pelanggan>>, response: Response<List<Pelanggan>>) {
                if (response.isSuccessful){
                    prpgresBarTransaksi.visibility = View.GONE
                    val pelanggan = response.body()
                    for (item in pelanggan!!){
                        arrayNamaPelanggan.add(item.nama_pelanggan)
                        arrayPelanggan.add(PelangganObject(item.id_pelanggan,item.nama_pelanggan,item.hp_pelanggan,item.email_pelanggan,item.tgl_lahir_pelanggan,item.id_kat_pelanggan))
                    }
                }else{
                    Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                    prpgresBarTransaksi.visibility = View.GONE
                    val intent = Intent(this@Transaksi,Main_Menu::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        })
        val adapterNamaPelanggan: ArrayAdapter<String?> = ArrayAdapter<String?>(this@Transaksi, android.R.layout.simple_spinner_dropdown_item, arrayNamaPelanggan)
        editTextNamaPelanggan.threshold = 1
        editTextNamaPelanggan.setAdapter(adapterNamaPelanggan)
    }

    private fun daftarKategori(viewPager: ViewPager){
        Client.instance.listkategori().enqueue(object : Callback<List<Kategori>>{
            override fun onFailure(call: Call<List<Kategori>>, t: Throwable) {
                Toast.makeText(this@Transaksi,"Gagal Memuat Data",Toast.LENGTH_SHORT).show()
                prpgresBarTransaksi.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<Kategori>>, response: Response<List<Kategori>>) {
                if (response.isSuccessful){
                    prpgresBarTransaksi.visibility = View.GONE
                    for (item in 1..response.body()!!.size){
                        namaKat.add(response.body()!![item-1].nama_kat_produk)
                    }
                    sectionsPagerAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    @SuppressLint("DefaultLocale", "SimpleDateFormat", "SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (intent != null){
            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val tglhariini: String = formatter.format(date)
            val id_produk = intent.getStringExtra("id_produk")
            val nama_produk = intent.getStringExtra("nama_produk")
            val harga_produk = intent.getStringExtra("harga_produk")
            val id_kat_produk = intent.getStringExtra("id_kat_produk")
            val jumlah_pesanan = intent.getStringExtra("jumlah_pesanan")
            val deskripsi = intent.getStringArrayListExtra("daftardeskripsi")
            val idDeskripsi = intent.getIntegerArrayListExtra("daftarIdDeskripsi")
            val indexpesanan = intent.getStringExtra("indexpesanan")
            val diskon_produk = intent.getIntExtra("diskon_produk",0)
            val tgl_awal_diskon = intent.getStringExtra("tgl_awal_diskon")
            val tgl_akhir_diskon = intent.getStringExtra("tgl_akhir_diskon")
            val diskon_olfood = intent.getIntExtra("diskon_olfood",0)
            val tgl_awal_diskon_olfood = intent.getStringExtra("tgl_awal_diskon_olfood")
            val tgl_akhir_diskon_olfood = intent.getStringExtra("tgl_akhir_diskon_olfood")
            val addnotes : String? = intent.getStringExtra("addnotes")

            val idRadioButton = radioGroupPesanan.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(idRadioButton)
            var jenis_transaksi: String = ""

            val rgOlfood = radioGroupOlfood.checkedRadioButtonId
            val selectedOlfood = findViewById<RadioButton>(rgOlfood)

            val LayoutManager = LinearLayoutManager(this)
            adapter = PesananAdapter(arrayPesanan, context,activity,textViewSub,update_pesanan,upadateTagihanPesanan)
            listViewPesanan.layoutManager = LayoutManager
            listViewPesanan.adapter = adapter
            adapter.notifyDataSetChanged()

            if (update_pesanan){
                editTextNamaPelanggan.isEnabled = false
            }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (switchOlfood.isChecked){
                jenis_transaksi = selectedOlfood.text.toString()//switchOlfood.text.toString()
                arrayPesanan.add(PesananObject(id_produk!!.toInt(), nama_produk!!, harga_produk!!.toInt(), id_kat_produk!!.toInt(), jumlah_pesanan!!.toInt(), deskripsi!!,jenis_transaksi,idDeskripsi!!,diskon_olfood,tgl_awal_diskon_olfood?.toString(),tgl_akhir_diskon_olfood?.toString(),addnotes))
            }else{
                jenis_transaksi = selectedRadioButton.text.toString()
                arrayPesanan.add(PesananObject(id_produk!!.toInt(), nama_produk!!, harga_produk!!.toInt(), id_kat_produk!!.toInt(), jumlah_pesanan!!.toInt(), deskripsi!!,jenis_transaksi,idDeskripsi!!,diskon_produk,tgl_awal_diskon?.toString(),tgl_akhir_diskon?.toString(),addnotes))
            }
            swipedelete(jenis_transaksi)

            checkSaldoHarian(tglhariini)
            /*buttonDiskon.setOnClickListener {
                val intent = Intent(this@Transaksi,Diskon::class.java)
                startActivityForResult(intent,2)
                val preferences = this@Transaksi.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
                preferences.edit().remove("saved_diskon").apply()
            }*/

        }else if (requestCode == 3 && resultCode == RESULT_OK){
            checkSaldoHarian(tglhariini)
        }else if (requestCode == 4 && resultCode == RESULT_OK){
            if (switchOlfood.isChecked){
                jenis_transaksi = switchOlfood.text.toString()
                arrayPesanan[indexpesanan!!.toInt()].jenis_transaksi = jenis_transaksi
                if (deskripsi != null) {
                    arrayPesanan[indexpesanan.toInt()].listDeskripsi = deskripsi
                }
                if (idDeskripsi != null) {
                    arrayPesanan[indexpesanan.toInt()].listIdDeskripsi = idDeskripsi
                }
                if (jumlah_pesanan != null) {
                    arrayPesanan[indexpesanan.toInt()].jumlah_pesanan = jumlah_pesanan.toInt()
                }
                arrayPesanan[indexpesanan.toInt()].addnotes = addnotes
            } else{
                jenis_transaksi = selectedRadioButton.text.toString()
                arrayPesanan[indexpesanan!!.toInt()].jenis_transaksi = jenis_transaksi
                if (deskripsi != null) {
                    arrayPesanan[indexpesanan.toInt()].listDeskripsi = deskripsi
                }
                if (idDeskripsi != null) {
                    arrayPesanan[indexpesanan.toInt()].listIdDeskripsi = idDeskripsi
                }
                if (jumlah_pesanan != null) {
                    arrayPesanan[indexpesanan.toInt()].jumlah_pesanan = jumlah_pesanan.toInt()
                }
                arrayPesanan[indexpesanan.toInt()].addnotes = addnotes
            }

            adapter.notifyItemChanged(indexpesanan.toInt())
            adapter.notifyDataSetChanged()
        }else if (requestCode == 5 && resultCode == RESULT_OK){
            val idmethodbayar = intent.getStringExtra("idmethodbayar")
            val terbayar = intent.getStringExtra("terbayar")
            val total_diskon = intent.getIntExtra("total_diskon",0)
            val tagihan_awal = intent.getIntExtra("tagihan_awal",0)
            val komisi = intent.getIntExtra("komisi",0)
            prpgresBarTransaksi.visibility = View.VISIBLE
            if (idmethodbayar != null) {
                if (terbayar != null) {
                    bayar(idmethodbayar,terbayar,total_diskon,tagihan_awal,komisi)
                }
            }

        }else if(requestCode == 6 && resultCode == RESULT_OK){
            //val tgl_trx_input = intent.getStringExtra("tgl_trx_input")
            //val id_user = intent.getStringExtra("id_user")
            val id_trx_penjualan = intent.getStringExtra("id_trx_penjualan")
            val tagihan_akhir = intent.getIntExtra("tagihan_akhir",0)
            val tagihan_awal = intent.getIntExtra("tagihan_awal",0)
            val total_diskon = intent.getIntExtra("total_diskon",0)
            val tax_lama = intent.getIntExtra("tax",0)
            val id_pelanggan = intent.getIntExtra("id_pelanggan",0)
            val nama = intent.getStringExtra("nama")
            val no_meja = intent.getStringExtra("no_meja")
            jumalahpesanan = intent.getIntExtra("jumalahpesanan",0)

            update_pesanan = true
            upadateTagihanPesanan = tagihan_akhir
            tax_openbill = tax_lama
            val jenis_transaksi = "open bill"
            if (id_trx_penjualan != null) {
                if (nama != null) {
                    updatePesananOpenBill(id_trx_penjualan,tagihan_akhir,tagihan_awal,total_diskon,nama,id_pelanggan,no_meja.toString(),jenis_transaksi)
                }
            }
        }

            buttonDiskon.setOnLongClickListener {
                val preferences = this.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
                preferences.edit().remove("saved_diskon").apply()
                if (preferences.edit().remove("saved_diskon").commit()){
                    val sharedPreferences: SharedPreferences = this.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
                    saved_diskon = sharedPreferences.getInt("saved_diskon",0)
                    textViewDiskon.text = "$saved_diskon%"
                    if (saved_diskon == 0){
                        textViewTotalHarga.text = textViewSub.text.toString()
                        finalTotalHarga(saved_diskon,tax,jenis_transaksi)
                    }
                }
                return@setOnLongClickListener true
            }
            buttonDiskon.setOnClickListener {
                val intent = Intent(this@Transaksi,Diskon::class.java)
                startActivityForResult(intent,2)
                val preferences = this@Transaksi.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
                preferences.edit().remove("saved_diskon").apply()
            }

            val sharedPreferencesTax: SharedPreferences = this.getSharedPreferences("TAX",Context.MODE_PRIVATE)
            tax = sharedPreferencesTax.getInt("tax",0)
            val sharedPreferences: SharedPreferences = this.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
            saved_diskon = sharedPreferences.getInt("saved_diskon",0)

            val disLength = saved_diskon.toString().count()
            if (disLength < 4){
                textViewDiskon.text = "$saved_diskon%"
            }else if(disLength > 3){
                textViewDiskon.text = "$saved_diskon"
            }

           if (!update_pesanan){

               if (switchOlfood.isChecked){
                   jenis_transaksi = selectedOlfood.text.toString()//switchOlfood.text.toString()
                   adapter.getTotal(jenis_transaksi,false,0)
               }else{
                   jenis_transaksi = selectedRadioButton.text.toString()
                   adapter.getTotal(jenis_transaksi,false,0)
               }

               finalTotalHarga(saved_diskon,tax,jenis_transaksi)

               textViewSub.addTextChangedListener(object : TextWatcher{
                   override fun afterTextChanged(s: Editable?) {
                       subTotal = textViewSub.text.toString().toInt()
                       dis = subTotal - (subTotal * saved_diskon / 100)
                       finalTotalHarga(saved_diskon,tax,jenis_transaksi)
                   }
                   override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                       subTotal = textViewSub.text.toString().toInt()
                       dis = subTotal - (subTotal * saved_diskon / 100)
                       finalTotalHarga(saved_diskon,tax,jenis_transaksi)
                   }
                   override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                       finalTotalHarga(saved_diskon,tax,jenis_transaksi)
                   }
               })
               ////////////////////////////////////////////////////////////////////////////////////////
               val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
               val saved_id_pelanggan = sharedPreferencesID.getString("id_pelanggan","")
               if (saved_id_pelanggan.toString().isNotEmpty()){
                   for (item in arrayPelanggan){
                       if (item.id_pelanggan == saved_id_pelanggan){
                           editTextNamaPelanggan.setText("${item.nama_pelanggan}")
                           editTextNamaPelanggan.error = null
                       }
                   }
               }
                /////////////////////////////////////////////////////////////////////////////////
               buttonOpenBill.setOnClickListener {
                   prpgresBarTransaksi.visibility = View.VISIBLE
                   if(editTextNamaPelanggan.text.isNullOrEmpty()){
                       editTextNamaPelanggan.error = "Nama Pelanggan Kosong"
                       editTextNamaPelanggan.requestFocus()
                       return@setOnClickListener
                   }
                   if (editTextNoMeja.text.isNullOrEmpty()){
                       editTextNoMeja.error = "No Meja Kosong"
                       editTextNoMeja.requestFocus()
                       return@setOnClickListener
                   }
                   if (jclick%2 == 0){
                       var checknama = false
                       for (item in arrayPelanggan) {
                           if (editTextNamaPelanggan.text.toString().toLowerCase() == item.nama_pelanggan.toString().toLowerCase()){
                               checknama = true
                           }
                       }
                       if (checknama){
                           openbill(getTotal_diskon(jenis_transaksi))
                       }else{
                           editTextNamaPelanggan.error = "Nama Tidak Terdaftar"
                           editTextNamaPelanggan.requestFocus()
                       }
                   }else{
                       openbill(getTotal_diskon(jenis_transaksi))
                   }
               }
               textViewTotalHarga.setOnClickListener {
                   //prpgresBarTransaksi.visibility = View.VISIBLE
                   val total_tagihan = textViewTotalHarga.text.toString()
                   if(editTextNamaPelanggan.text.isNullOrEmpty()){
                       editTextNamaPelanggan.error = "Nama Pelanggan Kosong"
                       editTextNamaPelanggan.requestFocus()
                       return@setOnClickListener
                   }
                   if (switchOlfood.isChecked){
                       if (editTextNoMeja.text.isNullOrEmpty()){
                           editTextNoMeja.error = "Isi ID Transaksi Olfood disini"
                           editTextNoMeja.requestFocus()
                           return@setOnClickListener
                       }
                   }else{
                       if (editTextNoMeja.text.isNullOrEmpty()){
                           editTextNoMeja.error = "No Meja Kosong"
                           editTextNoMeja.requestFocus()
                           return@setOnClickListener
                       }
                   }
                   if (jclick%2 == 0){
                       var checknama = false
                       for (item in arrayPelanggan) {
                           if (editTextNamaPelanggan.text.toString().toLowerCase() == item.nama_pelanggan.toString().toLowerCase()){
                               checknama = true
                           }
                       }
                       if (checknama){
                           val intent = Intent(this, Bayar::class.java)
                           intent.putExtra("jenistransaksi",jenis_transaksi)
                           intent.putExtra("total_tagihan",total_tagihan)
                           intent.putExtra("total_diskon",getTotal_diskon(jenis_transaksi))
                           intent.putExtra("tagihan_awal",getTagihan_Awal())
                           startActivityForResult(intent,5)
                       }else{
                           editTextNamaPelanggan.error = "Nama Tidak Terdaftar"
                           editTextNamaPelanggan.requestFocus()
                       }
                   }else{
                       val intent = Intent(this, Bayar::class.java)
                       intent.putExtra("jenistransaksi",jenis_transaksi)
                       intent.putExtra("total_tagihan",total_tagihan)
                       intent.putExtra("total_diskon",getTotal_diskon(jenis_transaksi))
                       intent.putExtra("tagihan_awal",getTagihan_Awal())
                       startActivityForResult(intent,5)
                   }
               }
           }else{
               textViewTax.text = tax_openbill.toString()
               editTextNamaPelanggan.isEnabled = false
               if (switchOlfood.isChecked){
                   jenis_transaksi = selectedOlfood.text.toString()//switchOlfood.text.toString()
                   adapter.getTotal(jenis_transaksi,update_pesanan,upadateTagihanPesanan)
               }else{
                   jenis_transaksi = selectedRadioButton.text.toString()
                   adapter.getTotal(jenis_transaksi,update_pesanan,upadateTagihanPesanan)
               }

               finalTotalHarga(saved_diskon,tax,jenis_transaksi)
               textViewSub.addTextChangedListener(object : TextWatcher{
                   override fun afterTextChanged(s: Editable?) {
                       subTotal = textViewSub.text.toString().toInt()
                       dis = subTotal - (subTotal * saved_diskon / 100)
                       finalTotalHarga(saved_diskon,tax,jenis_transaksi)
                   }
                   override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                       subTotal = textViewSub.text.toString().toInt()
                       dis = subTotal - (subTotal * saved_diskon / 100)
                       finalTotalHarga(saved_diskon,tax,jenis_transaksi)
                   }
                   override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                       finalTotalHarga(saved_diskon,tax,jenis_transaksi)
                   }
               })
           }
        }
    }

    private fun finalTotalHarga(saved_diskon:Int,tax:Int,jenis_transaksi: String){
        val textViewTotalHarga = findViewById<TextView>(R.id.textViewTotalHarga)
        val subTotal: Int
        val setTax : Int
        val disLength = saved_diskon.toString().count()
        if (update_pesanan){
            subTotal = textViewSub.text.toString().toInt()-upadateTagihanPesanan
            //dis = subTotal - (subTotal * saved_diskon / 100)
            if (disLength < 4){
                dis = subTotal - (subTotal * saved_diskon / 100)
            }else if(disLength > 3){
                dis = subTotal - saved_diskon
            }
            if (saved_diskon != 0){
                setTax = (dis * tax/100)
                textViewTax.text = (setTax + tax_openbill).toString()
                //val tgh_akhir = pembulatanTagihan(dis+setTax)
                val tgh_akhir = dis+setTax
                textViewTotalHarga.text = (tgh_akhir+upadateTagihanPesanan).toString()
            }else{
                setTax = (subTotal * tax/100)
                textViewTax.text = (setTax + tax_openbill).toString()
                //val tgh_akhir = pembulatanTagihan(subTotal+setTax)
                val tgh_akhir = subTotal+setTax
                textViewTotalHarga.text = (tgh_akhir+upadateTagihanPesanan).toString()
            }
        }else{
            subTotal = textViewSub.text.toString().toInt()
            //dis = subTotal - (subTotal * saved_diskon / 100)
            if (disLength < 4){
                dis = subTotal - (subTotal * saved_diskon / 100)
            }else if(disLength > 3){
                dis = subTotal - saved_diskon
            }
            if (saved_diskon != 0){
                if (jenis_transaksi == "Gofood" || jenis_transaksi == "Grabfood"){
                    setTax = 0
                    textViewTax.text = "0"
                    val tgh_akhir = pembulatanTagihan(dis+setTax)
                    textViewTotalHarga.text = tgh_akhir.toString()
                }else{
                    setTax = (dis * tax/100)
                    textViewTax.text = setTax.toString()
                    val tgh_akhir = dis+setTax
                    textViewTotalHarga.text = tgh_akhir.toString()
                }
                /*val tgh_akhir = pembulatanTagihan(dis+setTax)
                textViewTotalHarga.text = tgh_akhir.toString()*/
            }else{
                if (jenis_transaksi == "Gofood" || jenis_transaksi == "Grabfood"){
                    setTax = 0
                    textViewTax.text = "0"
                    val tgh_akhir = pembulatanTagihan(subTotal+setTax)
                    textViewTotalHarga.text = tgh_akhir.toString()
                }else{
                    setTax = (subTotal * tax/100)
                    textViewTax.text = setTax.toString()
                    val tgh_akhir = subTotal+setTax
                    textViewTotalHarga.text = tgh_akhir.toString()
                }
               /* val tgh_akhir = pembulatanTagihan(subTotal+setTax)
                textViewTotalHarga.text = tgh_akhir.toString()*/
            }
        }
    }

    private fun pembulatanTagihan(tgh_akhir : Int) : Int {
        val hasil : Int
        if (tgh_akhir % 100 != 0){
            val sisa = tgh_akhir % 100
            hasil = (tgh_akhir-sisa)+100
           /* if (sisa >= 50){
                hasil = (tgh_akhir-sisa)+100
            }else{
                hasil = (tgh_akhir-sisa)//+100
            }*/
        }else{
            hasil = tgh_akhir
        }
        return hasil
    }

    private fun getTax(tgh_akhir : Int) : Int{
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("TAX",Context.MODE_PRIVATE)
        val tax = sharedPreferences.getInt("tax",0)
        val setTax = (tgh_akhir * tax/100)
        textViewTax.text = setTax.toString()
        /*if (switchOlfood.isChecked){
            setTax = tax
        }else{
            for (item in arrayPesanan){
                if (item.jenis_transaksi == "Takeaway"){
                    setTax = tax
                }
            }
        }*/
        //Log.d("test123",setTax.toString())
        return setTax
    }

    private fun getTotal_diskon(jenis_transaksi: String) : Int {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
        val saved_diskon = sharedPreferences.getInt("saved_diskon",0)
        val disLength = saved_diskon.toString().count()
        var TT_disc : Int = 0
        val detail = adapter.getDetail()
        val subTotal : Int
        var disc = 0
        var total_disc = 0
        if (update_pesanan){
            subTotal = textViewSub.text.toString().toInt()-upadateTagihanPesanan
        }else{
            subTotal = textViewSub.text.toString().toInt()
        }
            for (i in 1..arrayPesanan.size){
                disc +=(detail[i-1].diskon * detail[i-1].jumlah_pesanan)
            }
        if (disLength < 4){
            TT_disc = disc+(subTotal * saved_diskon / 100)
        }else if(disLength > 3){
            TT_disc = disc+saved_diskon
        }
        if (jenis_transaksi == "Gofood" || jenis_transaksi == "Grabfood"){
            total_disc = pembulatanTagihan(TT_disc)
        }else{
            total_disc = TT_disc
        }
        return total_disc
    }

    fun getTagihan_Awal() : Int {
        var tagihan_awal = 0
        /*val sharedPreferences: SharedPreferences = this.getSharedPreferences("OlFoodCharge",Context.MODE_PRIVATE)
        val charge = sharedPreferences.getInt("olfoodcharge",0)*/
        for (item in arrayPesanan){
            var charge = 0
            var extraCharge = 0
            val preferencesGoFood = context.getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
            val preferencesGrabfood = context.getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
            if (item.jenis_transaksi == "Gofood"){
                charge = preferencesGoFood.getInt("PenyesuaianHargaGoFood", 0)
                extraCharge = preferencesGoFood.getInt("ExtraChargeGoFood", 0)
            }else if(item.jenis_transaksi == "Grabfood"){
                charge = preferencesGrabfood.getInt("PenyesuaianHargaGrabFood", 0)
                extraCharge = preferencesGrabfood.getInt("ExtraChargeGrabFood", 0)
            }
            val sharedPreferencesTax: SharedPreferences = context.getSharedPreferences("TAX",Context.MODE_PRIVATE)
            val tax2 = sharedPreferencesTax.getInt("tax",0)
            val olfoodCharge = item.harga_produk * charge/100

            val EXC = ((item.harga_produk+extraCharge)+((item.harga_produk+extraCharge)*tax2/100))*charge/100
            if (item.jenis_transaksi == "Gofood" || item.jenis_transaksi == "Grabfood"){
                tagihan_awal += (item.jumlah_pesanan * (item.harga_produk+EXC))
            }else{
                tagihan_awal += (item.jumlah_pesanan * item.harga_produk)
            }
        }
        return tagihan_awal
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun updatePesananOpenBill(id_trx_penjualan : String, tagihan_akhir : Int, tagihan_awal : Int,total_diskon : Int?, nama:String,id_pelanggan : Int, no_meja : String,jenis_transaksi: String){

        val textViewMemberGuest = findViewById<TextView>(R.id.textViewMemberGuest)
        if (id_pelanggan == 1){
            textViewMemberGuest.text = "Guest-Name : "
        }else{
            textViewMemberGuest.text = "Member-Name : "
        }

        if (adapter.itemCount != 0){
            textViewSub.text = (tagihan_akhir + getTagihan_Awal()).toString()
        }else{
            textViewSub.text = tagihan_akhir.toString()
        }

        editTextNamaPelanggan.setText(nama)
        editTextNamaPelanggan.isEnabled = false
        editTextNoMeja.setText(no_meja)
        editTextNoMeja.isEnabled = false
        buttonUpdatePesanan.visibility = View.VISIBLE
        buttonOpenBill.isEnabled = false
        buttonOpenBill.visibility = View.GONE

        textViewTotalHarga.isClickable = false

        //buttonListOpenBill.isEnabled = buttonUpdatePesanan.visibility != View.VISIBLE
        if (buttonUpdatePesanan.visibility == View.VISIBLE){
            buttonListOpenBill.text = "Batal"
            //textView20.visibility = View.GONE
            //textViewTotalHarga.visibility = View.GONE
        }else{
            buttonListOpenBill.text = "List Open Bill"
            //textView20.visibility = View.VISIBLE
           // textViewTotalHarga.visibility = View.VISIBLE
        }

        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMddkkmmss")

        buttonListOpenBill.setOnClickListener {
            val intent = Intent(this,Transaksi::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonUpdatePesanan.setOnClickListener {
            prpgresBarTransaksi.visibility = View.VISIBLE
            if(editTextNamaPelanggan.text.isNullOrEmpty()){
                editTextNamaPelanggan.error = "Nama Pelanggan Kosong"
                editTextNamaPelanggan.requestFocus()
                return@setOnClickListener
            }
            if (adapter.itemCount != 0){
                val id_trx_penjualan = id_trx_penjualan
                val tgl_trx_input = formatter.format(date)
                val tagihan_akhir = textViewTotalHarga.text.toString().toInt()
                val tagihan_awal = tagihan_awal+getTagihan_Awal()
                val tax_openbill = textViewTax.text.toString().toInt()
                //--------------------------------------------------------------------------
                val total_diskon = total_diskon!! + getTotal_diskon(jenis_transaksi)
                tambahpesanan(id_trx_penjualan,tgl_trx_input,tagihan_akhir,tagihan_awal,total_diskon,tax_openbill)
            }else{
                Toast.makeText(this,"Pesanan Kosong",Toast.LENGTH_SHORT).show()
                prpgresBarTransaksi.visibility = View.GONE
            }
        }
    }

    private fun tambahpesanan(id_trx_penjualan : String, tgl_trx_input : String, tagihan_akhir : Int, tagihan_awal : Int,total_diskon : Int?,tax_openbill:Int){
        Client.instance.tambahpesanan(id_trx_penjualan,tgl_trx_input,tagihan_akhir,tagihan_awal,total_diskon,tax_openbill).enqueue(object : Callback<TrxPenjualanRespon>{
            override fun onFailure(call: Call<TrxPenjualanRespon>, t: Throwable) {
                Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<TrxPenjualanRespon>, response: Response<TrxPenjualanRespon>) {
                if (response.isSuccessful){
                    //Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                    insertDetailPenjualan(id_trx_penjualan)
                }else{
                    Toast.makeText(applicationContext,response.errorBody().toString(),Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat","DefaultLocale")
    private fun openbill(total_diskon : Int){
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("USER_ID",Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("user_id","0")
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMddkkmmss")
        if (jclick%2 == 0){
            for (item in arrayPelanggan) {
                if (editTextNamaPelanggan.text.toString().toLowerCase() == item.nama_pelanggan.toString().toLowerCase()) {
                    namapelangggan = item.nama_pelanggan!!
                    id_pelanggan = item.id_pelanggan
                }
            }
        }else{
            namapelangggan = editTextNamaPelanggan.text.toString()
            id_pelanggan = "1"
        }

        val id_trx_penjualan = generateIDTrxPenjualan().toString()
        val tgl_trx_input = formatter.format(date)
        val trx_tgl_bayar : String? = null
        val tagihan_akhir = textViewTotalHarga.text.toString().toInt()
        val tagihan_awal = getTagihan_Awal()
        val tax = textViewTax.text.toString().toInt()
        val terbayar : Int? = null
        val id_pelanggan = id_pelanggan
        val nama = namapelangggan.toUpperCase()
        val no_meja = editTextNoMeja.text.toString()
        val id_method_bayar : Int? = null
        val jenis_tagihan = "Open bill"
        val id_user : Int? = id?.toInt()
        val komisi = 0

        Client.instance.inserttrxpenjualan(id_trx_penjualan,tgl_trx_input,trx_tgl_bayar,tagihan_akhir,tagihan_awal,total_diskon,tax,terbayar,komisi,id_pelanggan,nama,no_meja,id_method_bayar,jenis_tagihan,id_user)
            .enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@Transaksi,t.message,Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        //Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                        insertDetailPenjualan(id_trx_penjualan)
                    }else{
                        Toast.makeText(applicationContext,response.errorBody().toString(),Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat", "DefaultLocale")
    private fun bayar(idmethodbayar : String,terbayar:String,total_diskon:Int,tagihan_awal : Int,komisi:Int){

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("USER_ID",Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("user_id","0")
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMddkkmmss")
        if (jclick%2 == 0){
            val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
            val saved_id_pelanggan = sharedPreferencesID.getString("id_pelanggan","")
            if (saved_id_pelanggan.toString().isNotEmpty()){
               for (item in arrayPelanggan){
                   if (item.id_pelanggan == saved_id_pelanggan){
                       namapelangggan = item.nama_pelanggan!!
                       id_pelanggan = item.id_pelanggan
                   }
               }
            }else{
                for (item in arrayPelanggan) {
                    if (editTextNamaPelanggan.text.toString().toLowerCase() == item.nama_pelanggan.toString().toLowerCase()) {
                        namapelangggan = item.nama_pelanggan!!
                        id_pelanggan = item.id_pelanggan
                    }
                }
            }
        }else{
            namapelangggan = editTextNamaPelanggan.text.toString()
            id_pelanggan = "1"
        }

        val id_trx_penjualan = generateIDTrxPenjualan().toString()
        val tgl_trx_input = formatter.format(date)
        val trx_tgl_bayar : String? = formatter.format(date)
        val tagihan_akhir = textViewTotalHarga.text.toString().toInt()
        val tagihan_awal = tagihan_awal
        val tax = textViewTax.text.toString().toInt()
        val terbayar : Int? = terbayar.toInt()
        //val id_pelanggan = id_pelanggan
        val nama = namapelangggan.toUpperCase()
        val no_meja = editTextNoMeja.text.toString()
        val id_method_bayar : Int? = idmethodbayar.toInt()
        val jenis_tagihan = "Lunas"
        val id_user : Int? = id?.toInt()
        val komisi = komisi
        var id_pelanggan1 = ""

        val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
        val saved_id_pelanggan = sharedPreferencesID.getString("id_pelanggan","")
        if (saved_id_pelanggan == null || saved_id_pelanggan.isEmpty()){
            id_pelanggan1 = id_pelanggan
        }else {
            id_pelanggan1 = saved_id_pelanggan
        }

        val total_point : Int = tagihan_akhir / 1000
       Log.d("test123","Point Pelanggan : $total_point")
        Client.instance.inserttrxpenjualan(id_trx_penjualan,tgl_trx_input,trx_tgl_bayar,tagihan_akhir,tagihan_awal,total_diskon,tax,terbayar,komisi,id_pelanggan1,nama,no_meja,id_method_bayar,jenis_tagihan,id_user)
            .enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@Transaksi,t.message,Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        updateVoucherPelanggan()
                        updatePoinPelanggan(total_point.toString())
                        insertDetailPenjualan(id_trx_penjualan)
                    }else{
                        Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    @SuppressLint("DefaultLocale")
    private fun insertDetailPenjualan(id_trx_penjualan : String){
        val detail = adapter.getDetail()

        for (i in 1..arrayPesanan.size){
            val id_trx_penjualan = id_trx_penjualan
            var id_trx_detpenjualan : String
            if (!update_pesanan){
                val num = java.lang.String.format("%03d", i)
                id_trx_detpenjualan = "$id_trx_penjualan$num"
            }else{
                val num = java.lang.String.format("%03d", i+jumalahpesanan)
                id_trx_detpenjualan = "$id_trx_penjualan$num"
            }
            val qty_terjual = detail[i-1].jumlah_pesanan
            val harga_terjual = detail[i-1].harga_produk_pesanan
            var diskon_terjual : Int?
            val status_pesanan = "dalam proses"
            val id_produk = detail[i-1].id_produk_pesanan
            val jenis_transaksi = detail[i-1].jenis_transaksi
            val diskon = detail[i-1].diskon
            val arraydeskripsi = detail[i-1].arraydeskripsi
            var deskripsi: String?
            val addnotes : String? = detail[i-1].addnotes
            if (arraydeskripsi.isNullOrEmpty()){
                deskripsi = ""
            }else{
                deskripsi = arraydeskripsi.toString().replace("[","").replace("]","")
            }
            if (diskon > 0){
                diskon_terjual = detail[i-1].diskon * qty_terjual
            }else{
                diskon_terjual = 0
            }
            Client.instance.insertdetailpenjualan(id_trx_detpenjualan,id_trx_penjualan,qty_terjual,harga_terjual,diskon_terjual,status_pesanan,id_produk,jenis_transaksi,deskripsi,addnotes)
                .enqueue(object : Callback<DefaultResponse>{
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(this@Transaksi,t.message,Toast.LENGTH_LONG).show()
                        prpgresBarTransaksi.visibility = View.GONE
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if (response.isSuccessful){
                            val kode_vocher = getSharedPreferences("KODE_VOUCHER", 0)
                            val id_pelanggan = getSharedPreferences("ID_PELANGGAN",0)
                            if(kode_vocher.edit().commit() && id_pelanggan.edit().commit()){
                                Toast.makeText(this@Transaksi,"Transaksi Berhasil",Toast.LENGTH_LONG).show()
                                val intent = Intent(this@Transaksi,Transaksi::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                prpgresBarTransaksi.visibility = View.GONE
                                startActivity(intent)
                            }
                        }else{
                            Toast.makeText(this@Transaksi,response.body()?.message,Toast.LENGTH_LONG).show()
                            prpgresBarTransaksi.visibility = View.GONE
                        }
                    }

                })
            //Log.d("test123","deskripsi : $id_trx_detpenjualan")
            /*//convert string to array
            val arr : List<String> = listString.split(",")
            for (item in arr){
                Log.d("test123",item)
            }*/
        }
    }

    @SuppressLint("SimpleDateFormat", "DefaultLocale")
    private fun generateIDTrxPenjualan() : Int{
        val date = Date()
        val formatter = SimpleDateFormat("yyMMdd")
        val tglhariini: Int = formatter.format(date).toInt()
        var generateId = ""
        val cut : Int
        var take = 0

        if (arrayId_Trx_Penjualah.isNotEmpty()) {
            cut = arrayId_Trx_Penjualah[arrayId_Trx_Penjualah.lastIndex].toString().take(6).toInt()
            take = arrayId_Trx_Penjualah[arrayId_Trx_Penjualah.lastIndex].toString().subSequence(6, 9).toString().toInt()
            if (cut != tglhariini){
                take = 0
                val id = take + 1
                val num = java.lang.String.format("%03d", id)
                generateId = "$tglhariini$num"
            }else if(cut == tglhariini){
                val id = take + 1
                val num = java.lang.String.format("%03d", id)
                generateId = "$tglhariini$num"
            }
        }else{
            val id = take + 1
            val num = java.lang.String.format("%03d", id)
            generateId = "$tglhariini$num"
        }
        return generateId.toInt()
    }

    override fun onDataPass(gridview: GridView, arrayProduk:ArrayList<ProdukObject>, id_kat_produk:Int) {
        gridview.setOnItemClickListener{ parent, view, position, id ->
                if (produkClickStatus){
               // if (editTextNamaPelanggan.text.isNotEmpty()){
                    val rgOlfood = radioGroupOlfood.checkedRadioButtonId
                    val selectedOlfood = findViewById<RadioButton>(rgOlfood)
                    selected_jenisTRansaksi = selectedOlfood.text.toString()
                    if (switchOlfood.isChecked){
                        val data = arrayPesanan
                        if (data.isEmpty()){
                            dataTopass(arrayProduk,position)
                        }else{
                            var check = false
                            for (item in data){
                                check = item.jenis_transaksi != selected_jenisTRansaksi
                            }
                            if (check){
                                Toast.makeText(context,"Pesanan Tidak Bisa Ditambahkan (Jenis Transaksi Berbeda)",Toast.LENGTH_LONG).show()
                            }else{
                                dataTopass(arrayProduk,position)
                            }
                        }
                    }else{
                        dataTopass(arrayProduk,position)
                    }
               /* }else{
                    editTextNamaPelanggan.error = "Nama Pelanggan Kosong"
                    editTextNamaPelanggan.requestFocus()
                }*/
                }else{
                    Toast.makeText(this,"Tap (New Transaks +) Dulu",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun dataTopass(arrayProduk:ArrayList<ProdukObject>,position:Int){
        val id_kat_produk = arrayProduk[position].id_kat_produk
        val id_produk = arrayProduk[position].id_produk
        val nama_produk = arrayProduk[position].nama_produk
        val harga_produk = arrayProduk[position].harga_produk
        val diskon_produk = arrayProduk[position].diskon_produk
        val tgl_awal_diskon = arrayProduk[position].tgl_awal_diskon
        val tgl_akhir_diskon = arrayProduk[position].tgl_akhir_diskon
        val diskon_olfood : Int? = arrayProduk[position].diskon_olfood
        val tgl_awal_diskon_olfood : String? = arrayProduk[position].tgl_awal_diskon_olfood
        val tgl_akhir_diskon_olfood : String? = arrayProduk[position].tgl_akhir_diskon_olfood
        val intent = Intent(this, DeskripsiPesanan::class.java)
        intent.putExtra("id_kat_produk", id_kat_produk.toString())
        intent.putExtra("id_produk", id_produk.toString())
        intent.putExtra("nama_produk", nama_produk)
        intent.putExtra("harga_produk", harga_produk.toString())
        intent.putExtra("diskon_produk",diskon_produk)
        intent.putExtra("tgl_awal_diskon",tgl_awal_diskon)
        intent.putExtra("tgl_akhir_diskon",tgl_akhir_diskon)
        intent.putExtra("diskon_olfood",diskon_olfood)
        intent.putExtra("tgl_awal_diskon_olfood",tgl_awal_diskon_olfood)
        intent.putExtra("tgl_akhir_diskon_olfood",tgl_akhir_diskon_olfood)
        intent.putExtra("comefrom", "daftarproduk")
        startActivityForResult(intent,1)
    }

    private fun swipedelete(jenis_transaksi:String){

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val item: PesananObject = adapter.getData()[position]
                val detail : DetailTransaksi = adapter.getDetail()[position]
                adapter.removeAt(position)
                if (adapter.itemCount >= 0){
                    if (!update_pesanan){
                        adapter.getTotal(jenis_transaksi,update_pesanan,0)
                    }else{
                        adapter.getTotal(jenis_transaksi,update_pesanan,upadateTagihanPesanan)
                    }
                }
                val snackbar: Snackbar = Snackbar.make(root_view_transaksi, item.nama_produk+"dihapus ", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO ?") {
                    adapter.restoreItem(item,detail,position)
                    listViewPesanan.scrollToPosition(position)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(listViewPesanan)
    }

    private fun updateVoucherPelanggan(){
        val nama_database = AdminDatabase().nama_database
        val sharedPreferencesKV: SharedPreferences = this.getSharedPreferences("KODE_VOUCHER",Context.MODE_PRIVATE)
        val kode_voucher = sharedPreferencesKV.getString("kode_voucher","")
        val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
        val id_pelanggan = sharedPreferencesID.getString("id_pelanggan","")
        if (kode_voucher != null && id_pelanggan != null){
            Client.instance.updateStatusVoucherPelanggan("USED",kode_voucher,id_pelanggan,nama_database).enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@Transaksi,"Voucher Berhasil Digunakan",Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    private fun updatePoinPelanggan(total_point:String){
        val nama_database = AdminDatabase().nama_database
        val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
        val id_pelanggan = sharedPreferencesID.getString("id_pelanggan","")
        if (id_pelanggan != null){
            Client.instance.updatePoinPelanggan(total_point,id_pelanggan,nama_database).enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@Transaksi,"Poin Pelanggan Berhasil Ditambah",Toast.LENGTH_LONG).show()
                        sharedPreferencesID.edit().remove("id_pelanggan").apply()
                        //getTokenNotifikasiPelanggan()
                    }
                }
            })
        }
    }

   /* private fun getTokenNotifikasiPelanggan(){
        val nama_database = AdminDatabase().nama_database
        val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
        val id_pelanggan = sharedPreferencesID.getString("id_pelanggan","")
        if (id_pelanggan != null){
            Client.instance.getTokenPelanggan(id_pelanggan,nama_database).enqueue(object : Callback<UserToken>{
                override fun onFailure(call: Call<UserToken>, t: Throwable) {
                    Toast.makeText(this@Transaksi,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<UserToken>, response: Response<UserToken>) {
                    if (response.isSuccessful){
                        val data = response.body()
                        if (data != null){
                            Log.d("test123","${data.nama_pelanggan}")
                            Log.d("test123","${data.token_notifikasi}")
                        }
                        sharedPreferencesID.edit().remove("id_pelanggan").apply()
                    }
                }
            })
        }
    }*/

    override fun onBackPressed() {
        val intent = Intent(this,Main_Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val sharedPreferencesID: SharedPreferences = this.getSharedPreferences("ID_PELANGGAN",Context.MODE_PRIVATE)
        sharedPreferencesID.edit().remove("id_pelanggan").apply()
        startActivity(intent)
        super.onBackPressed()
    }
}