package com.marikhapp.marikhkasir.activities.purchasing

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.red
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.JOYFUL_COLORS
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.PurchasingModels.KategoriPurchasing
import com.marikhapp.marikhkasir.models.PurchasingModels.Purchasing
import kotlinx.android.synthetic.main.activity_purchasing_transaction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PurchasingTransaction : AppCompatActivity() {
    val arrayKategoriPurchasing = ArrayList<KategoriPurchasingObject>()
    var arrayPurchasing = ArrayList<PurchasingObject>()
    val context = this@PurchasingTransaction
    lateinit var adapterpurchasing : PurchasingAdapter
    lateinit var adapterkatpurchasing : KategoriPurchasingAdapter
    var title = arrayOf("Pembelian", "Kategori", "Jumlah", "Harga","Total")
    //var idkategori = ""
    val arrayKat = ArrayList<String>()
    val container = ArrayList<PurchasingObject>()
    var tanggalPurchasing = ""
    var selectedTgl = ""
    var namakategori = ""
    var updateKat = false
    var id_kat = ""
    var updateP = false
    var id_purchasing = ""
    var jumlah = ""
    var  totalpurchasing = 0
    var hrg = "0"
    var today = ""
    var chartData = ArrayList<Entry>()
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchasing_transaction)
        textViewTransaksi.text = "< Purchasing Transaction"
        textViewTransaksi.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        //ConstraitPurchasing.isScrollContainer = true
        progresBarPurchasing.visibility = View.VISIBLE
        var jclick1 = 1
        buttonOnOffPurchasing.setOnClickListener {
            jclick1 += 1
            if (jclick1%2 == 0){
                arrayKategoriPurchasing.clear()
                kategoripurchasing()
                constraitKategoriPurchasing.visibility = View.VISIBLE
                buttonfilterTgl.visibility = View.GONE
                textViewfilterTgl.visibility = View.GONE
                constraitPurchasing.visibility = View.GONE
            }else{
                arrayPurchasing.clear()
                daftarpurchasing()
                constraitPurchasing.visibility = View.VISIBLE
                buttonfilterTgl.visibility = View.VISIBLE
                textViewfilterTgl.visibility = View.VISIBLE
                constraitKategoriPurchasing.visibility = View.GONE
                constraitAddKatPurchasing.visibility = View.GONE
            }
        }
        kategoripurchasing()
        daftarpurchasing()
        //---------------------------------
        fabrefreshPurchasing.setOnClickListener {
            arrayPurchasing.clear()
            daftarpurchasing()
            editTextKatPurchasing.setText("")
            editTextNamaPurchasing.setText("")
            editTextJumlahPurchasing.setText("0")
            editTextHargaPurchasing.setText("0")
            editTextTotalPurchasing.setText("0")
            buttonSimpanPurchasing.text = "Simpan"
            constraitAddKatPurchasing.visibility = View.GONE
            updateKat = false
            updateP = false
            totalpurchasing = 0
            progresBarPurchasing.visibility = View.VISIBLE
        }
        //--------------------------------------
        adapterpurchasing = PurchasingAdapter(arrayPurchasing,context)
        listviewPurchasing.adapter = adapterpurchasing
        adapterpurchasing.notifyDataSetChanged()
        var jclick = 1
        listviewPurchasing.setOnItemClickListener { parent, view, position, id ->
            jclick += 1
            val namaPembelian = view.findViewById<TextView>(R.id.textviewNamaPembelian)
            val kategoriPembelian = view.findViewById<TextView>(R.id.textviewKategoriPembelian)
            if (jclick % 2 == 0){
                namaPembelian.isSelected = true
                namaPembelian.marqueeRepeatLimit = -1
                kategoriPembelian.isSelected = true
                kategoriPembelian.marqueeRepeatLimit = -1
            }else{
                namaPembelian.isSelected = false
                namaPembelian.marqueeRepeatLimit = 0
                kategoriPembelian.isSelected = false
                kategoriPembelian.marqueeRepeatLimit = 0
            }
        }

        listviewPurchasing.setOnItemLongClickListener { parent, view, position, id ->
            val purchasing = adapterpurchasing.getItem(position)
            updateP = true
            id_purchasing = purchasing.id_purchasing

            if (radioButtonKasHarian.text.toString().toUpperCase() == purchasing.jenis_post_pengeluaran.toUpperCase()){
                radioButtonKasHarian.isChecked = true
            }
            if (radioButtonKasUtama.text.toString().toUpperCase() == purchasing.jenis_post_pengeluaran.toUpperCase()){
                radioButtonKasUtama.isChecked = true
            }
            val dataTgl = purchasing.tgl_purchasing.take(10).replace("-","")
            val tgl = dataTgl.subSequence(6,8)
            val bln = dataTgl.subSequence(4,6)
            val thn = dataTgl.subSequence(0,4)
            val date = "$bln-$tgl-$thn"
            val date2 = "$thn$bln$tgl"
            val formatTanggal = SimpleDateFormat("yyyyMMdd")
            val ftgl = formatTanggal.parse(date2)
            tanggalPurchasing = formatTanggal.format(ftgl!!)
            editTextTglPurchasing.setText(date)
            editTextKatPurchasing.setText(purchasing.namaKategoriPembelian)
            editTextNamaPurchasing.setText(purchasing.nama_pembelian)
            editTextJumlahPurchasing.setText(purchasing.jumlah_pembelian.toString())
            editTextHargaPurchasing.setText(purchasing.harga_pembelian.toString())
            editTextTotalPurchasing.setText(purchasing.total_pembelian.toString())
            buttonSimpanPurchasing.text = "Update"
            return@setOnItemLongClickListener true
        }

        tambahKatPurchasing()
        adapterkatpurchasing = KategoriPurchasingAdapter(arrayKategoriPurchasing,context)
        listkategoriPurchasing.adapter = adapterkatpurchasing

        listkategoriPurchasing.setOnItemLongClickListener { parent, view, position, id ->
            val katPurchasing = adapterkatpurchasing.getItem(position)
            updateKat = true
            id_kat = katPurchasing.id_kat_pengeluaran
            buttonSaveKatPurchasing.text = "Update"
            constraitAddKatPurchasing.visibility = View.VISIBLE
            editTextInputKatPurchasing.setText(katPurchasing.nama_kat_pengeluaran)
            return@setOnItemLongClickListener true
        }

        //tambahPurchasing()
        editTextTglPurchasing.isEnabled = false
        editTextTotalPurchasing.isEnabled = false
        editTextHargaPurchasing.isEnabled = false

        buttonTglPurchasing.setOnClickListener {
            //showDatePickerDialog(editTextTglPurchasing)
        }

        val adapterKategoriPurchasing: ArrayAdapter<String> = ArrayAdapter<String>(this@PurchasingTransaction, android.R.layout.simple_spinner_dropdown_item, arrayKat)
        editTextKatPurchasing.threshold = 1
        editTextKatPurchasing.setAdapter(adapterKategoriPurchasing)

        editTextJumlahPurchasing.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                editTextHargaPurchasing.isEnabled = s.toString().isNotEmpty()
                editTextHargaPurchasing.isEnabled = s.toString() != "0"
                jumlah = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                editTextHargaPurchasing.isEnabled = s.toString().isNotEmpty()
                editTextHargaPurchasing.isEnabled = s.toString() != "0"
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextHargaPurchasing.isEnabled = s.toString().isNotEmpty()
                editTextHargaPurchasing.isEnabled = s.toString() != "0"
            }
        })

            editTextHargaPurchasing.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() == "" || s.toString().isEmpty() ){
                        hrg = "0"
                    }else{
                        hrg = s.toString()
                    }
                    if (jumlah != "0" || jumlah.isNotEmpty() || jumlah != "" || s.toString().isNotEmpty()){
                        totalpurchasing = jumlah.toInt() * hrg.toInt()
                    }else{
                        totalpurchasing = 0
                    }
                    editTextTotalPurchasing.setText(totalpurchasing.toString())
                    tambahPurchasing(totalpurchasing)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    if (s.toString() == "" || s.toString().isEmpty() ){
                        hrg = "0"
                    }else{
                        hrg = s.toString()
                    }
                    if (jumlah != "0" || jumlah.isNotEmpty() || jumlah != "" || s.toString().isNotEmpty()){
                        totalpurchasing = jumlah.toInt() * hrg.toInt()
                    }else{
                        totalpurchasing = 0
                    }
                    editTextTotalPurchasing.setText(totalpurchasing.toString())
                    tambahPurchasing(totalpurchasing)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() == "" || s.toString().isEmpty() ){
                        hrg = "0"
                    }else{
                        hrg = s.toString()
                    }
                    if (jumlah != "0" || jumlah.isNotEmpty() || jumlah != "" || s.toString().isNotEmpty()){
                        totalpurchasing = jumlah.toInt() * hrg.toInt()
                    }else{
                        totalpurchasing = 0
                    }
                    editTextTotalPurchasing.setText(totalpurchasing.toString())
                    tambahPurchasing(totalpurchasing)
                }
            })
       /* val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))

        val vl = LineDataSet(entries, "My Type")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.colorPrimary
        vl.fillAlpha = R.color.Bl
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.data = LineData(vl)
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = 1+0.1f
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")
        lineChart.animateX(1800, Easing.EaseInExpo)*/

    }

    @SuppressLint("SimpleDateFormat")
    private fun daftarpurchasing(){
        var namakategori = ""
        val date = Date()
        val formatter = SimpleDateFormat("MM-dd-yyyy")
        today = formatter.format(date.time)
        editTextTglPurchasing.setText(today)
        val formatTanggal = SimpleDateFormat("yyyyMMdd")
        tanggalPurchasing = formatTanggal.format(date.time)
        editTextTglPurchasing.setText(today)

        Client.instance.daftarpengeluaran().enqueue(object : Callback<List<Purchasing>>{
            override fun onFailure(call: Call<List<Purchasing>>, t: Throwable) {
                Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                progresBarPurchasing.visibility = View.GONE
            }

            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(call: Call<List<Purchasing>>, response: Response<List<Purchasing>>) {
                if (response.isSuccessful){
                    progresBarPurchasing.visibility = View.GONE
                    val data = response.body()
                    arrayPurchasing.clear()
                    container.clear()
                    for (item in data!!){
                        for (item2 in arrayKategoriPurchasing){
                            if (item2.id_kat_pengeluaran == item.id_kat_pengeluaran){
                                namakategori = item2.nama_kat_pengeluaran
                            }
                        }
                        arrayPurchasing.add(PurchasingObject(item.id_trx_pengeluaran,item.nama_pengeluaran,item.id_kat_pengeluaran,item.tgl_pengeluaran,item.qty_pengeluaran,item.nominal_pengeluaran,item.total_pengeluaran,item.jenis_post_pengeluaran,namakategori))
                        container.add(PurchasingObject(item.id_trx_pengeluaran,item.nama_pengeluaran,item.id_kat_pengeluaran,item.tgl_pengeluaran,item.qty_pengeluaran,item.nominal_pengeluaran,item.total_pengeluaran,item.jenis_post_pengeluaran,namakategori))
                    }
                }else{
                    Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                    progresBarPurchasing.visibility = View.GONE
                }
                var total = 0
                for (item in  arrayPurchasing){
                    total += item.total_pembelian
                }
                textViewTotalAllPurchasing.text = "Rp.$total"

                val date1 = Date()
                val formatter1 = SimpleDateFormat("dd-MM-yyyy")
                textViewfilterTgl.text = formatter1.format(date1.time)

                val formatTanggal1 = SimpleDateFormat("yyyyMMdd")
                selectedTgl = formatTanggal1.format(date1.time)
                filter()

                buttonfilterTgl.setOnClickListener {
                    filterDatePickerDialog()
                }
                buttonfilterTgl.setOnLongClickListener {
                    selectedTgl = ""
                    textViewfilterTgl.text = ""
                    arrayPurchasing.clear()
                    filter()
                    //arrayPurchasing.addAll(container)
                    adapterpurchasing.notifyDataSetChanged()
                    textViewTotalAllPurchasing.text = "Rp.$total"
                    return@setOnLongClickListener true
                }
                adapterpurchasing.notifyDataSetChanged()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun filter(){
        var tgl = ""
        if (selectedTgl.isNotEmpty()){
            arrayPurchasing.clear()
            for (item in container){
                tgl = item.tgl_purchasing.replace("-","").take(8)
                if (tgl == selectedTgl){
                    arrayPurchasing.add(item)
                }
            }
        }else{
            arrayPurchasing.addAll(container)
        }
        adapterpurchasing.notifyDataSetChanged()
        var total = 0
        for (item in  arrayPurchasing){
            total += item.total_pembelian
        }
        textViewTotalAllPurchasing.text = "Rp.$total"
    }

    @SuppressLint("SimpleDateFormat")
    private fun tambahPurchasing(hrg : Int){
        var kas = ""
        var id = ""
        val date = Date()
        val formatter = SimpleDateFormat("kkmmss")
        val jam: String = formatter.format(date)
        buttonSimpanPurchasing.isEnabled = hrg != 0
        buttonSimpanPurchasing.setOnClickListener {
            progresBarPurchasing.visibility = View.VISIBLE
            val radiogroupKas = radioGroupKas.checkedRadioButtonId
            val radiobuttonKas = findViewById<RadioButton>(radiogroupKas)
            if (radiobuttonKas.text.toString() == "Kas Harian"){
                kas = "kas harian"
            }else{
                kas = "kas operational"
            }
            if (editTextTglPurchasing.text.isEmpty()){
                buttonTglPurchasing.error = "Tanggal Kosong"
                buttonTglPurchasing.requestFocus()
                progresBarPurchasing.visibility = View.GONE
            }
            if (editTextKatPurchasing.text.isEmpty()){
                editTextKatPurchasing.error = "Kategori Kosong"
                editTextKatPurchasing.requestFocus()
                progresBarPurchasing.visibility = View.GONE
            }
            if (editTextNamaPurchasing.text.isEmpty()){
                editTextNamaPurchasing.error = "Nama Pembelian Kosong"
                editTextNamaPurchasing.requestFocus()
                progresBarPurchasing.visibility = View.GONE
            }
            if (editTextJumlahPurchasing.text.isEmpty() || editTextJumlahPurchasing.text.toString() == "0"){
                editTextJumlahPurchasing.error = "Jumlah Pembelian Kosong"
                editTextJumlahPurchasing.requestFocus()
                progresBarPurchasing.visibility = View.GONE
            }
            if (editTextHargaPurchasing.text.isEmpty() || editTextHargaPurchasing.text.toString() == "0"){
                editTextHargaPurchasing.error = "Harga Pembelian Kosong"
                editTextHargaPurchasing.requestFocus()
                progresBarPurchasing.visibility = View.GONE
            }

            val tglpurchasing = "$tanggalPurchasing$jam"
            val kategoripurchasing = editTextKatPurchasing.text.toString()
            val namapurchasing = editTextNamaPurchasing.text.toString().toUpperCase()
            val jumlahpurchasing = editTextJumlahPurchasing.text.toString()
            val hargapurchasing = editTextHargaPurchasing.text.toString()

            val  totalpurchasing = hrg
            for (item in arrayKategoriPurchasing){
                if (item.nama_kat_pengeluaran == kategoripurchasing){
                    id = item.id_kat_pengeluaran
                    editTextKatPurchasing.error = null
                }
            }
            if (!updateP){
                if (id.isNotEmpty() && namapurchasing.isNotEmpty() && tglpurchasing.isNotEmpty() && hargapurchasing.isNotEmpty() && jumlahpurchasing.isNotEmpty() && totalpurchasing != 0 && kas.isNotEmpty()){
                    insertPurchasing(id,namapurchasing,tglpurchasing,hargapurchasing.toInt(),jumlahpurchasing.toInt(),totalpurchasing,kas)
                }else{
                    for (item in arrayKategoriPurchasing){
                        if (item.nama_kat_pengeluaran != kategoripurchasing){
                            editTextKatPurchasing.error = "Kategori Tidak Terdaftar"
                            editTextKatPurchasing.requestFocus()
                        }else{
                            editTextKatPurchasing.error = null
                        }
                    }
                    Toast.makeText(applicationContext,"Isi Semua Field",Toast.LENGTH_LONG).show()
                    progresBarPurchasing.visibility = View.GONE
                }
            }else{
                if (id_purchasing.isNotEmpty() && id.isNotEmpty() && namapurchasing.isNotEmpty() && tglpurchasing.isNotEmpty() && hargapurchasing.isNotEmpty() && jumlahpurchasing.isNotEmpty() && totalpurchasing != 0 && kas.isNotEmpty()){
                    updatePurchasing(id_purchasing,id,namapurchasing,tglpurchasing,hargapurchasing.toInt(),jumlahpurchasing.toInt(),totalpurchasing,kas)
                }else{
                    for (item in arrayKategoriPurchasing){
                        if (item.nama_kat_pengeluaran != kategoripurchasing){
                            editTextKatPurchasing.error = "Kategori Tidak Terdaftar"
                            editTextKatPurchasing.requestFocus()
                        }else{
                            editTextKatPurchasing.error = null
                        }
                    }
                    Toast.makeText(applicationContext,"Isi Semua Field",Toast.LENGTH_LONG).show()
                    progresBarPurchasing.visibility = View.GONE
                }
            }
        }
    }

    private fun insertPurchasing(id_kat_pengeluaran:String,nama_pengeluaran : String,tgl_pengeluaran:String,harga_pengeluaran:Int,jumlah_pengeluaran:Int,total_pengeluaran:Int,post_pengeluaran:String){
        Client.instance.insertPengeluaran(id_kat_pengeluaran,nama_pengeluaran,tgl_pengeluaran,harga_pengeluaran,jumlah_pengeluaran,total_pengeluaran,post_pengeluaran).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                progresBarPurchasing.visibility = View.GONE
                updateP = false
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                    arrayPurchasing.clear()
                    daftarpurchasing()
                    editTextKatPurchasing.setText("")
                    editTextNamaPurchasing.setText("")
                    editTextJumlahPurchasing.setText("0")
                    editTextHargaPurchasing.setText("0")
                    editTextTotalPurchasing.setText("0")
                    progresBarPurchasing.visibility = View.GONE
                    constraitPurchasing.visibility = View.VISIBLE
                    buttonfilterTgl.visibility = View.VISIBLE
                    textViewfilterTgl.visibility = View.VISIBLE
                    constraitKategoriPurchasing.visibility = View.GONE
                    constraitAddKatPurchasing.visibility = View.GONE
                    updateP = false
                }else{
                    arrayPurchasing.clear()
                    daftarpurchasing()
                    Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                    editTextKatPurchasing.setText("")
                    editTextNamaPurchasing.setText("")
                    editTextJumlahPurchasing.setText("0")
                    editTextHargaPurchasing.setText("0")
                    editTextTotalPurchasing.setText("0")
                    progresBarPurchasing.visibility = View.GONE
                    constraitPurchasing.visibility = View.VISIBLE
                    buttonfilterTgl.visibility = View.VISIBLE
                    textViewfilterTgl.visibility = View.VISIBLE
                    constraitKategoriPurchasing.visibility = View.GONE
                    constraitAddKatPurchasing.visibility = View.GONE
                    updateP = false
                }
            }
        })
    }

    private fun updatePurchasing(id_purchasing:String,id_kat_pengeluaran:String,nama_pengeluaran : String,tgl_pengeluaran:String,harga_pengeluaran:Int,jumlah_pengeluaran:Int,total_pengeluaran:Int,post_pengeluaran:String){
        Client.instance.updatePengeluaran(id_purchasing,id_kat_pengeluaran,nama_pengeluaran,tgl_pengeluaran,harga_pengeluaran,jumlah_pengeluaran,total_pengeluaran,post_pengeluaran).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                progresBarPurchasing.visibility = View.GONE
                updateP = false
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                    arrayPurchasing.clear()
                    daftarpurchasing()
                    editTextKatPurchasing.setText("")
                    editTextNamaPurchasing.setText("")
                    editTextJumlahPurchasing.setText("0")
                    editTextHargaPurchasing.setText("0")
                    editTextTotalPurchasing.setText("0")
                    buttonSimpanPurchasing.text = "Simpan"
                    progresBarPurchasing.visibility = View.GONE
                    constraitPurchasing.visibility = View.VISIBLE
                    buttonfilterTgl.visibility = View.VISIBLE
                    textViewfilterTgl.visibility = View.VISIBLE
                    constraitKategoriPurchasing.visibility = View.GONE
                    constraitAddKatPurchasing.visibility = View.GONE
                    updateP = false
                }else{
                    arrayPurchasing.clear()
                    daftarpurchasing()
                    Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                    editTextKatPurchasing.setText("")
                    editTextNamaPurchasing.setText("")
                    editTextJumlahPurchasing.setText("0")
                    editTextHargaPurchasing.setText("0")
                    editTextTotalPurchasing.setText("0")
                    buttonSimpanPurchasing.text = "Simpan"
                    progresBarPurchasing.visibility = View.GONE
                    constraitPurchasing.visibility = View.VISIBLE
                    buttonfilterTgl.visibility = View.VISIBLE
                    textViewfilterTgl.visibility = View.VISIBLE
                    constraitKategoriPurchasing.visibility = View.GONE
                    constraitAddKatPurchasing.visibility = View.GONE
                    updateP = false
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun tambahKatPurchasing(){
        var jclick = 1
        textViewTambahKategoriPurchasing.setOnClickListener {
            jclick +=1
            if (jclick % 2 == 0){
                constraitAddKatPurchasing.visibility = View.VISIBLE
                editTextInputKatPurchasing.error = null
                editTextInputKatPurchasing.setText("")
                buttonSaveKatPurchasing.text = "Simpan"
            }else{
                constraitAddKatPurchasing.visibility = View.GONE
            }
        }

        buttonSaveKatPurchasing.setOnClickListener {
            progresBarPurchasing.visibility = View.VISIBLE
            if (editTextInputKatPurchasing.text.isEmpty()){
                editTextInputKatPurchasing.error = "Nama Kategori Kosong"
                editTextInputKatPurchasing.requestFocus()
                progresBarPurchasing.visibility = View.GONE
            }
            val namakatpurchasing = editTextInputKatPurchasing.text.toString().toUpperCase()
            if (namakatpurchasing.isNotEmpty() || namakatpurchasing != ""){
                if (!updateKat){
                    insertKatPurchasing(namakatpurchasing)
                }else{
                    if (id_kat.isNotEmpty()){
                        updatekategoripurchasing(id_kat,namakatpurchasing)
                    }
                }
            }
        }
    }

    private fun insertKatPurchasing(namakatpurchasing : String){
        Client.instance.insertKategoriPengeluaran(namakatpurchasing).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                updateKat = false
                progresBarPurchasing.visibility = View.GONE
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                    editTextInputKatPurchasing.error = null
                    constraitAddKatPurchasing.visibility = View.GONE
                    arrayKategoriPurchasing.clear()
                    kategoripurchasing()
                    editTextInputKatPurchasing.setText("")
                    updateKat = false
                    progresBarPurchasing.visibility = View.GONE
                }else{
                    Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                    constraitAddKatPurchasing.visibility = View.GONE
                    editTextInputKatPurchasing.setText("")
                    updateKat = false
                    progresBarPurchasing.visibility = View.GONE
                }
            }
        })
    }

    private fun updatekategoripurchasing(id_kat_pengeluaran: String , namakatpurchasing: String){
        if (editTextInputKatPurchasing.text.isEmpty()){
            editTextInputKatPurchasing.error = "Nama Kategori Kosong"
            editTextInputKatPurchasing.requestFocus()
        }else{
            Client.instance.updatekategoriPengeluaran(id_kat_pengeluaran,namakatpurchasing).enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                    updateKat = false
                    progresBarPurchasing.visibility = View.GONE
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                        editTextInputKatPurchasing.error = null
                        constraitAddKatPurchasing.visibility = View.GONE
                        arrayKategoriPurchasing.clear()
                        kategoripurchasing()
                        editTextInputKatPurchasing.setText("")
                        buttonSaveKatPurchasing.text = "Simpan"
                        progresBarPurchasing.visibility = View.GONE
                        updateKat = false
                    }else{
                        Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                        constraitAddKatPurchasing.visibility = View.GONE
                        editTextInputKatPurchasing.setText("")
                        buttonSaveKatPurchasing.text = "Simpan"
                        progresBarPurchasing.visibility = View.GONE
                        updateKat = false
                    }
                }
            })
        }
    }

    private fun kategoripurchasing(){
        Client.instance.daftarkategoripengeluaran().enqueue(object : Callback<List<KategoriPurchasing>>{
            override fun onFailure(call: Call<List<KategoriPurchasing>>, t: Throwable) {
                Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                progresBarPurchasing.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<KategoriPurchasing>>, response: Response<List<KategoriPurchasing>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    for (item in data!!){
                        arrayKategoriPurchasing.add(KategoriPurchasingObject(item.id_kat_pengeluaran,item.nama_kat_pengeluaran))
                        arrayKat.add(item.nama_kat_pengeluaran)
                        progresBarPurchasing.visibility = View.GONE
                    }
                }else{
                    Toast.makeText(applicationContext,"Gagal Memuat Data",Toast.LENGTH_LONG).show()
                    progresBarPurchasing.visibility = View.GONE
                }
                adapterkatpurchasing.notifyDataSetChanged()
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePickerDialog(textView: TextView) {
        //android.R.style.Theme_Material_Light_Dialog
        val now = Calendar.getInstance()
        val datepicker = DatePickerDialog(this,
            AlertDialog.THEME_TRADITIONAL,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)

                val date = GregorianCalendar(year, month, dayOfMonth)
                val formatter = SimpleDateFormat("MM-dd-yyyy")
                textView.text = formatter.format(date.time)
                val formatTanggal = SimpleDateFormat("yyyyMMdd")
                tanggalPurchasing = formatTanggal.format(date.time)
            },
            now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
        datepicker.show()
    }
    @SuppressLint("SimpleDateFormat")
    private fun filterDatePickerDialog() {
        val now = Calendar.getInstance()
        val datepicker = DatePickerDialog(this,
            AlertDialog.THEME_TRADITIONAL,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)

                val date = GregorianCalendar(year, month, dayOfMonth)
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                textViewfilterTgl.text = formatter.format(date.time)

                val formatTanggal = SimpleDateFormat("yyyyMMdd")
                selectedTgl = formatTanggal.format(date.time)
                filter()
            },
            now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
        datepicker.show()
    }
    override fun onBackPressed() {
        val intent = Intent(this, Main_Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }
}
