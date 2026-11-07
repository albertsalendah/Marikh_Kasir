package com.marikhapp.marikhkasir.activities.member

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.PelangganModels.KategoriPelanggan
import com.marikhapp.marikhkasir.models.PelangganModels.Pelanggan
import kotlinx.android.synthetic.main.activity_member.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Member : AppCompatActivity() {
    var tglLahir = ""
    lateinit var adapterMember : MemberAdapter
    lateinit var adapterKategoriPelanggan : KategoriPelangganAdapter
    val arrayKatPelanggan = ArrayList<KategoriPelangganObject>()
    val context = this@Member
    val arrayMember = ArrayList<MemberObject>()
    var namaKategoriPelanggan = ""
    var updateM = false
    var updateKat = false
    var id_pelanggan = ""
    var id_kategori = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)
        textViewMember.text = "< Member"
        editTextTglLahirPelanggan.isEnabled = false
        textViewMember.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        progressBarMemberLayout.visibility = View.VISIBLE
        var jclick = 1
        buttonKategoriMember.setOnClickListener {
            jclick += 1
            if (jclick % 2 == 0){
                arrayKatPelanggan.clear()
                kategoriPelanggan()
                constraitListMember.visibility = View.GONE
                constraitKategoriPelanggan.visibility = View.VISIBLE
            }else{
                arrayMember.clear()
                daftarMember()
                constraitListMember.visibility = View.VISIBLE
                constraitKategoriPelanggan.visibility = View.GONE
                constraitTambahKategoriPelanggan.visibility = View.GONE
            }
        }

        kategoriPelanggan()
        adapterKategoriPelanggan = KategoriPelangganAdapter(arrayKatPelanggan,context)
        listKategoriPelanggan.adapter = adapterKategoriPelanggan

        listKategoriPelanggan.setOnItemLongClickListener { parent, view, position, id ->
            val katpelanggan = adapterKategoriPelanggan.getItem(position)
            if (katpelanggan.id_kat_pelanggan == "1"){
                updateKat = false
                Toast.makeText(applicationContext,"Entry ini tidak boleh diubah",Toast.LENGTH_LONG).show()
                editTextTambahKategoriPelanggan.setText("")
                buttonSimpanKategoriPelanggan.text = "Simpan"
                constraitTambahKategoriPelanggan.visibility = View.GONE
                return@setOnItemLongClickListener false
            }else{
                updateKat = true
                id_kategori = katpelanggan.id_kat_pelanggan
                editTextTambahKategoriPelanggan.setText(katpelanggan.nama_kat_pelanggan)
                buttonSimpanKategoriPelanggan.text = "Update"
                constraitTambahKategoriPelanggan.visibility = View.VISIBLE
            }
            return@setOnItemLongClickListener true
        }

        adapterMember = MemberAdapter(arrayMember,this)
        listMember.adapter = adapterMember
        daftarMember()

        var jclick1 = 1
        listMember.setOnItemClickListener { parent, view, position, id ->
            val namapelanggan = view.findViewById<TextView>(R.id.textViewNamaPelanggan)
            val nohpPelanggan = view.findViewById<TextView>(R.id.textViewNoHpPelanggan)
            val emailPelanggan = view.findViewById<TextView>(R.id.textViewEmailPelanggan)
            jclick1 += 1
            if (jclick1 % 2 == 0){
                namapelanggan.marqueeRepeatLimit = -1
                nohpPelanggan.marqueeRepeatLimit = -1
                emailPelanggan.marqueeRepeatLimit = -1
            }else{
                namapelanggan.marqueeRepeatLimit = 0
                nohpPelanggan.marqueeRepeatLimit = 0
                emailPelanggan.marqueeRepeatLimit = 0
            }
            namapelanggan.isSelected = jclick1 % 2 == 0
            nohpPelanggan.isSelected = jclick1 % 2 == 0
            emailPelanggan.isSelected = jclick1 % 2 == 0
        }

        listMember.setOnItemLongClickListener { parent, view, position, id ->
            val member  = adapterMember.getItem(position)
            if (member.id_pelanggan == "1"){
                updateM = false
                Toast.makeText(applicationContext,"Entry ini tidak boleh diubah",Toast.LENGTH_LONG).show()
                autoCompleteTextView.setText("")
                editTextTglLahirPelanggan.setText("")
                editTextNamaPelanggan.setText("")
                editTextNoHpPelanggan.setText("")
                editTextEmailPelnggan.setText("")
                buttonSimpanDataPelanggan.text = "Simpan"
                return@setOnItemLongClickListener false
            }else{
                autoCompleteTextView.setText(member.namaKategori)
                editTextTglLahirPelanggan.setText(member.tgl_lahir_pelanggan)
                editTextNamaPelanggan.setText(member.nama_pelanggan)
                editTextNoHpPelanggan.setText(member.hp_pelanggan)
                editTextEmailPelnggan.setText(member.email_pelanggan)
                buttonSimpanDataPelanggan.text = "Update"
                updateM = true
                id_pelanggan = member.id_pelanggan.toString()
            }
            return@setOnItemLongClickListener true
        }

        fabrefreshMember.setOnClickListener {
            progressBarMemberLayout.visibility = View.VISIBLE
            arrayMember.clear()
            daftarMember()
            autoCompleteTextView.setText("")
            editTextTglLahirPelanggan.setText("")
            editTextNamaPelanggan.setText("")
            editTextNoHpPelanggan.setText("")
            editTextEmailPelnggan.setText("")
            buttonSimpanDataPelanggan.text = "Simpan"
            updateM = false
            updateKat = false
            constraitTambahKategoriPelanggan.visibility = View.GONE
        }

        buttonTglLahir.setOnClickListener {
            showDatePickerDialog(editTextTglLahirPelanggan)
        }
        buttonSimpanDataPelanggan.setOnLongClickListener {
            return@setOnLongClickListener true
        }
        buttonSimpanDataPelanggan.setOnClickListener {
            progressBarMemberLayout.visibility = View.VISIBLE
            val namapelanggan = editTextNamaPelanggan.text.toString().toUpperCase()
            var nohpPelanggan = editTextNoHpPelanggan.text.toString()
            var emailPelanggan = editTextEmailPelnggan.text.toString()
            var id_kat_pelanggan = ""
            if(editTextNamaPelanggan.text.isEmpty()){
                editTextNamaPelanggan.error = "Nama Pelanggan Kosong"
                editTextNamaPelanggan.requestFocus()
                progressBarMemberLayout.visibility = View.GONE
            }
            if(autoCompleteTextView.text.isEmpty() || autoCompleteTextView.text.toString() == ""){
                autoCompleteTextView.error = "Kategori Pelanggan Kosong"
                autoCompleteTextView.requestFocus()
                progressBarMemberLayout.visibility = View.GONE
            }
            if (editTextTglLahirPelanggan.text.isEmpty()){
                tglLahir = ""
            }
            if (editTextNoHpPelanggan.text.isEmpty()){
                nohpPelanggan = ""
            }
            if (editTextEmailPelnggan.text.isEmpty()){
                emailPelanggan = ""
            }
            for (item in arrayKatPelanggan){
                if (autoCompleteTextView.text.toString() == item.nama_kat_pelanggan){
                    id_kat_pelanggan = item.id_kat_pelanggan
                    autoCompleteTextView.error = null
                }
            }
            if (!updateM){
                insertMember(namapelanggan,nohpPelanggan,emailPelanggan,tglLahir,id_kat_pelanggan)
            }else{
                if (id_pelanggan.isNotEmpty()){
                    if(autoCompleteTextView.text.isEmpty() || autoCompleteTextView.text.toString() == ""){
                        autoCompleteTextView.error = "Kategori Pelanggan Kosong"
                        autoCompleteTextView.requestFocus()
                        progressBarMemberLayout.visibility = View.GONE
                    }
                    if (editTextTglLahirPelanggan.text.isEmpty()){
                        tglLahir = ""
                    }
                    if (editTextNoHpPelanggan.text.isEmpty()){
                        nohpPelanggan = ""
                    }
                    if (editTextEmailPelnggan.text.isEmpty()){
                        emailPelanggan = ""
                    }
                    for (item in arrayKatPelanggan){
                        if (autoCompleteTextView.text.toString() == item.nama_kat_pelanggan){
                            id_kat_pelanggan = item.id_kat_pelanggan
                            autoCompleteTextView.error = null
                        }
                    }
                    updateMember(id_pelanggan,namapelanggan,nohpPelanggan,emailPelanggan,tglLahir,id_kat_pelanggan)
                }
            }
        }
    }

    private fun kategoriPelanggan(){

        val arrayKatPelangganS = ArrayList<String>()
        var jclick = 1
        textViewNewKategoriPelanggan.setOnClickListener {
            jclick += 1
            if (jclick % 2 == 0){
                constraitTambahKategoriPelanggan.visibility = View.VISIBLE
            }else{
                constraitTambahKategoriPelanggan.visibility = View.GONE
            }
        }

        Client.instance.daftarkategoripelanggan().enqueue(object : Callback<List<KategoriPelanggan>>{
            override fun onFailure(call: Call<List<KategoriPelanggan>>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                progressBarMemberLayout.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<KategoriPelanggan>>, response: Response<List<KategoriPelanggan>>) {
                if (response.isSuccessful){
                    progressBarMemberLayout.visibility = View.GONE
                    val data = response.body()
                    for (item in data!!){
                        arrayKatPelanggan.add(KategoriPelangganObject(item.id_kat_pelanggan,item.nama_kat_pelanggan))
                        arrayKatPelangganS.add(item.nama_kat_pelanggan)
                    }
                }else{
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                    progressBarMemberLayout.visibility = View.GONE
                }
                adapterKategoriPelanggan.notifyDataSetChanged()
            }
        })


        val adapterKategoriP: ArrayAdapter<String> = ArrayAdapter<String>(this@Member, android.R.layout.simple_spinner_dropdown_item, arrayKatPelangganS)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.setAdapter(adapterKategoriP)

        buttonSimpanKategoriPelanggan.setOnClickListener {
            progressBarMemberLayout.visibility = View.VISIBLE
            if (editTextTambahKategoriPelanggan.text.isEmpty()){
                editTextTambahKategoriPelanggan.error = "Nama Kategori Kosong"
                editTextTambahKategoriPelanggan.requestFocus()
                progressBarMemberLayout.visibility = View.GONE
            }
            val namaKategoriPelanggan = editTextTambahKategoriPelanggan.text.toString().toUpperCase()
            if (!updateKat){
                insertKatPelanggan(namaKategoriPelanggan)
            }else{
                if (id_kategori.isNotEmpty()){
                    updateKatPelanggan(id_kategori,namaKategoriPelanggan)
                }
            }
        }
    }

    private fun insertKatPelanggan(namaKat : String){
        Client.instance.insertkategoripelanggan(namaKat).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                updateKat = false
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                    arrayKatPelanggan.clear()
                    kategoriPelanggan()
                    editTextTambahKategoriPelanggan.setText("")
                    constraitTambahKategoriPelanggan.visibility = View.GONE
                    progressBarMemberLayout.visibility = View.GONE
                    updateKat = false
                }else{
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                    constraitTambahKategoriPelanggan.visibility = View.GONE
                    progressBarMemberLayout.visibility = View.GONE
                    updateKat = false
                }
            }
        })
    }

    private fun updateKatPelanggan(id_kat_pelanggan:String,namaKat_pelanggan : String){
        if (editTextTambahKategoriPelanggan.text.isEmpty()){
            editTextTambahKategoriPelanggan.error = "Nama Kategori Kosong"
            editTextTambahKategoriPelanggan.requestFocus()
        }else{
            Client.instance.updatekategoriPelanggan(id_kat_pelanggan,namaKat_pelanggan).enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                    progressBarMemberLayout.visibility = View.GONE
                    updateKat =false
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                        arrayKatPelanggan.clear()
                        kategoriPelanggan()
                        editTextTambahKategoriPelanggan.setText("")
                        buttonSimpanKategoriPelanggan.text = "Simpan"
                        constraitTambahKategoriPelanggan.visibility = View.GONE
                        progressBarMemberLayout.visibility = View.GONE
                        updateKat = false
                    }else{
                        editTextTambahKategoriPelanggan.setText("")
                        Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                        buttonSimpanKategoriPelanggan.text = "Simpan"
                        constraitTambahKategoriPelanggan.visibility = View.GONE
                        progressBarMemberLayout.visibility = View.GONE
                        updateKat = false
                    }
                }
            })
        }
    }

    private fun daftarMember(){
        Client.instance.daftarpelanggan().enqueue(object : Callback<List<Pelanggan>>{
            override fun onFailure(call: Call<List<Pelanggan>>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                progressBarMemberLayout.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<Pelanggan>>, response: Response<List<Pelanggan>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    for (item in data!!){
                        for (item2 in arrayKatPelanggan){
                            if (item2.id_kat_pelanggan == item.id_kat_pelanggan.toString()){
                                namaKategoriPelanggan = item2.nama_kat_pelanggan
                            }
                        }
                        arrayMember.add(MemberObject(item.id_pelanggan,item.nama_pelanggan,item.hp_pelanggan,item.email_pelanggan,item.tgl_lahir_pelanggan,item.id_kat_pelanggan,namaKategoriPelanggan))
                    }
                    progressBarMemberLayout.visibility = View.GONE
                }else{
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                    progressBarMemberLayout.visibility = View.GONE
                }
                adapterMember.notifyDataSetChanged()
            }
        })
    }

    private fun insertMember(nama_pelanggan : String,hp_pelanggan:String?,email_pelanggan:String?,tgl_lahir:String?,id_kat:String){
        Client.instance.insertpelanggan(nama_pelanggan,hp_pelanggan,email_pelanggan,tgl_lahir,id_kat).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                progressBarMemberLayout.visibility = View.GONE
                updateM = false
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                    arrayMember.clear()
                    daftarMember()
                    autoCompleteTextView.setText("")
                    editTextTglLahirPelanggan.setText("")
                    editTextNamaPelanggan.setText("")
                    editTextNoHpPelanggan.setText("")
                    editTextEmailPelnggan.setText("")
                    progressBarMemberLayout.visibility = View.GONE
                    constraitListMember.visibility = View.VISIBLE
                    constraitKategoriPelanggan.visibility = View.GONE
                    constraitTambahKategoriPelanggan.visibility = View.GONE
                    updateM = false
                }else{
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                    autoCompleteTextView.setText("")
                    editTextTglLahirPelanggan.setText("")
                    editTextNamaPelanggan.setText("")
                    editTextNoHpPelanggan.setText("")
                    editTextEmailPelnggan.setText("")
                    progressBarMemberLayout.visibility = View.GONE
                    constraitListMember.visibility = View.VISIBLE
                    constraitKategoriPelanggan.visibility = View.GONE
                    constraitTambahKategoriPelanggan.visibility = View.GONE
                    updateM = false
                }
            }
        })
    }

    private fun updateMember(id_pelanggan : String,nama_pelanggan : String,hp_pelanggan:String?,email_pelanggan:String?,tgl_lahir:String?,id_kat:String){
        if(editTextNamaPelanggan.text.isEmpty() || editTextNamaPelanggan.text.toString() == ""){
            editTextNamaPelanggan.error = "Nama Pelanggan Kosong"
            editTextNamaPelanggan.requestFocus()
            updateM = false
        }else{
            Client.instance.updatePelanggan(id_pelanggan,nama_pelanggan,hp_pelanggan,email_pelanggan,tgl_lahir,id_kat).enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                    progressBarMemberLayout.visibility = View.GONE
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        progressBarMemberLayout.visibility = View.GONE
                        Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                        arrayMember.clear()
                        daftarMember()
                        autoCompleteTextView.setText("")
                        editTextTglLahirPelanggan.setText("")
                        editTextNamaPelanggan.setText("")
                        editTextNoHpPelanggan.setText("")
                        editTextEmailPelnggan.setText("")
                        buttonSimpanDataPelanggan.text = "Simpan"
                        constraitListMember.visibility = View.VISIBLE
                        constraitKategoriPelanggan.visibility = View.GONE
                        constraitTambahKategoriPelanggan.visibility = View.GONE
                        updateM = false
                    }else{
                        Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat data",Toast.LENGTH_LONG).show()
                        autoCompleteTextView.setText("")
                        editTextTglLahirPelanggan.setText("")
                        editTextNamaPelanggan.setText("")
                        editTextNoHpPelanggan.setText("")
                        editTextEmailPelnggan.setText("")
                        buttonSimpanDataPelanggan.text = "Simpan"
                        progressBarMemberLayout.visibility = View.GONE
                        constraitListMember.visibility = View.VISIBLE
                        constraitKategoriPelanggan.visibility = View.GONE
                        constraitTambahKategoriPelanggan.visibility = View.GONE
                        updateM = false
                    }
                }
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePickerDialog(textView: EditText) {
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
                textView.setText(formatter.format(date.time).toString())
                val formatTanggal = SimpleDateFormat("yyyyMMdd")
                tglLahir = formatTanggal.format(date.time)
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
