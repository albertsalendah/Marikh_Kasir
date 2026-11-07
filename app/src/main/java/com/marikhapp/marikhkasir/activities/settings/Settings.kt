package com.marikhapp.marikhkasir.activities.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.ReportOperational.ReportOperational
import com.marikhapp.marikhkasir.models.SettingModels.Setting
import kotlinx.android.synthetic.main.activity_settings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Settings : AppCompatActivity() {
    val arrayId_Trx_Penjualah = ArrayList<String>()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        textViewSetting.text = "< Setting"

        textViewSetting.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val judulsetting = findViewById<TextView>(R.id.textViewJudulSetting)
        val modeSetting = findViewById<TextView>(R.id.textViewModeSetting)
        val editTextSetting = findViewById<EditText>(R.id.editTextSetting)
        val buttonSetSetting = findViewById<Button>(R.id.buttonSetSetting)
        val layoutSetting = findViewById<ConstraintLayout>(R.id.layoutSetting)
        val layoutSettingNota = findViewById<ConstraintLayout>(R.id.layoutSettingNota)
        buttonSetSetting.isEnabled = false
        Client.instance.listreportoperational().enqueue(object : Callback<List<ReportOperational>>{
            override fun onFailure(call: Call<List<ReportOperational>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<ReportOperational>>, response: Response<List<ReportOperational>>) {
               if (response.isSuccessful){
                   val data = response.body()
                   if (data != null){
                       for (item in data){
                           arrayId_Trx_Penjualah.add(item.id_report_all)
                       }
                   }
               }
            }
        })
        Client.instance.settings().enqueue(object : Callback<List<Setting>>{
            override fun onFailure(call: Call<List<Setting>>, t: Throwable) {
                Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Setting>>, response: Response<List<Setting>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        if (data.isEmpty()){
                            Log.d("test123","Data Kosong")
                        }else{
                            for (item in data){
                                val id = item.id
                                val modal_awal = item.modal_awal
                                val tax = item.tax
                                val gofoodph = item.gofoodph
                                val gofoodec = item.gofoodec
                                val gofoodkomisi = item.gofoodkomisi
                                val grabfoodph = item.grabfoodph
                                val grabfoodec = item.grabfoodec
                                val grabfoodkomisi = item.grabfoodkomisi

                                textViewSettingModalAwal.setOnClickListener {
                                    editTextSetting.setText("")
                                    layoutSetting.visibility = View.VISIBLE
                                    layoutSettingNota.visibility = View.GONE
                                    layoutSettingOlffod.visibility = View.GONE
                                    buttonSetSetting.isEnabled = true
                                    judulsetting.text = "Setting Modal Awal"
                                    modeSetting.text = "Modal Awal"
                                    editTextSetting.setText(modal_awal)

                                   /* if (buttonSetSetting.isEnabled){
                                        Toast.makeText(this@Settings,"Button Setting ON",Toast.LENGTH_LONG).show()
                                    }else{
                                        Toast.makeText(this@Settings,"Button Setting OFF",Toast.LENGTH_LONG).show()
                                    }*/
                                    editTextSetting.isEnabled = (modal_awal == "0")
                                    buttonSetSetting.isEnabled = (modal_awal == "0")
                                    buttonSetSetting.setOnClickListener {
                                        var modal = editTextSetting.text.toString()
                                        if (modal.isEmpty()){
                                            modal = "0"
                                        }
                                        settingModalAwal(id,modal)
                                    }
                                }

                                textViewSettingTax.setOnClickListener {
                                    editTextSetting.setText("")
                                    layoutSetting.visibility = View.VISIBLE
                                    layoutSettingNota.visibility = View.GONE
                                    layoutSettingOlffod.visibility = View.GONE
                                    buttonSetSetting.isEnabled = true
                                    judulsetting.text = "Setting Tax"
                                    modeSetting.text = "Besar Tax"
                                    if (tax.toInt() >= 0){
                                        editTextSetting.setText(tax)
                                    }else{
                                        editTextSetting.setText("")
                                    }

                                    buttonSetSetting.setOnClickListener {
                                        var taxx = editTextSetting.text.toString()
                                        if (taxx.isEmpty()){
                                            taxx = "0"
                                        }
                                        settingTax(id,taxx)
                                    }
                                }

                                textViewSettingOlfood.setOnClickListener {
                                    layoutSetting.visibility = View.GONE
                                    layoutSettingNota.visibility = View.GONE
                                    layoutSettingOlffod.visibility = View.VISIBLE
                                    var selected = ""
                                    radioButtonGoFood.setOnClickListener {
                                        selected = radioButtonGoFood.text.toString()
                                        editTextPenyesuaianHargaOlfood.setText(gofoodph)
                                        editTextExtraCharge.setText(gofoodec)
                                        editTextKomisi.setText(gofoodkomisi)
                                    }
                                    radioButtonGrabfood.setOnClickListener {
                                        selected = radioButtonGrabfood.text.toString()
                                        editTextPenyesuaianHargaOlfood.setText(grabfoodph)
                                        editTextExtraCharge.setText(grabfoodec)
                                        editTextKomisi.setText(grabfoodkomisi)
                                    }

                                    buttonSettingOlfood.setOnClickListener {
                                        loadingSetting.visibility = View.VISIBLE
                                        var harga = editTextPenyesuaianHargaOlfood.text.toString()
                                        var charge = editTextExtraCharge.text.toString()
                                        var komisi = editTextKomisi.text.toString()
                                        if (selected == "Gofood"){
                                            if (harga.isEmpty()){
                                                harga = "0"
                                            }
                                            if (charge.isEmpty()){
                                                charge = "0"
                                            }
                                            if (komisi.isEmpty()){
                                                komisi = "0"
                                            }
                                            updateGofood(id, harga, charge, komisi)
                                        }
                                        if(selected == "Grabfood"){
                                            if (harga.isEmpty()){
                                                harga = "0"
                                            }
                                            if (charge.isEmpty()){
                                                charge = "0"
                                            }
                                            if (komisi.isEmpty()){
                                                komisi = "0"
                                            }
                                            updateGrabfood(id, harga, charge, komisi)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })

        textViewSettingNota.setOnClickListener {
            layoutSetting.visibility = View.GONE
            layoutSettingNota.visibility = View.VISIBLE
        }

        val preferencesGoFood = this.getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
        //val PenyesuaianHargaGoFood = preferencesGoFood.getInt("PenyesuaianHargaGoFood", 0)
        val ExtraChargeGoFood = preferencesGoFood.getInt("ExtraChargeGoFood", 0)
        val KomisiGoFood = preferencesGoFood.getInt("KomisiGoFood", 0)

        val preferencesGrabfood = this.getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
        //val PenyesuaianHargaGrabFood = preferencesGrabfood.getInt("PenyesuaianHargaGrabFood", 0)
        val ExtraChargeGrabFood = preferencesGrabfood.getInt("ExtraChargeGrabFood", 0)
        //val KomisiGrabFood = preferencesGrabfood.getInt("KomisiGrabFood", 0)

    }

    private fun updateGofood(id:String,harga:String,charge:String,komisi:String){
        Client.instance.updateGofood(id,harga,charge,komisi).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                loadingSetting.visibility = View.GONE
                Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
               if (response.isSuccessful){
                   loadingSetting.visibility = View.GONE
                   Toast.makeText(this@Settings,response.message(),Toast.LENGTH_LONG).show()
                   val intent = Intent(this@Settings, Settings::class.java)
                   intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   startActivity(intent)
               }else{
                   loadingSetting.visibility = View.GONE
                   Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                   val intent = Intent(this@Settings, Settings::class.java)
                   intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   startActivity(intent)
               }
            }
        })
    }

    private fun updateGrabfood(id : String,harga:String,charge:String,komisi:String){
        Client.instance.updateGrabfood(id,harga,charge,komisi).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                loadingSetting.visibility = View.GONE
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    loadingSetting.visibility = View.GONE
                    Toast.makeText(this@Settings,response.message(),Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Settings, Settings::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }else{
                    loadingSetting.visibility = View.GONE
                    Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Settings, Settings::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        })
    }

    private fun settingOlfood(charge:Int){
        val preferences = getSharedPreferences("OlFoodCharge", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("olfoodcharge", charge)
        editor.apply()
        if (editor.commit()){
            Toast.makeText(this,"Perubahan Berhasil Diubah",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Perubahan Gagal Diubah",Toast.LENGTH_LONG).show()
        }
    }

    private fun settingTax(id : String,tax : String){
        Client.instance.updateTax(id,tax).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                loadingSetting.visibility = View.GONE
                Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    loadingSetting.visibility = View.GONE
                    Toast.makeText(this@Settings,response.message(),Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Settings, Settings::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }else{
                    loadingSetting.visibility = View.GONE
                    Toast.makeText(this@Settings,response.message(),Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Settings, Settings::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

            }
        })
    }

    private fun settingModalAwal(id: String,modal_awal : String){
        Client.instance.updateModalAwal(id,modal_awal).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                loadingSetting.visibility = View.GONE
                Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                   insertReport(modal_awal)
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun insertReport(modal_awal:String){
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMdd")
        val id = generateIDTrxPenjualan()
        val tgl_input = formatter.format(date)
        val keterangan = "Kas Operational"
        val kredit_op = modal_awal
        val debit_op = "0"
        if (id.isNotEmpty()){
            Client.instance.insertreportoperational(id,tgl_input,keterangan,kredit_op,debit_op).enqueue(object : Callback<DefaultResponse>{
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    loadingSetting.visibility = View.GONE
                    Toast.makeText(this@Settings,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful){
                        loadingSetting.visibility = View.GONE
                        Toast.makeText(this@Settings,response.message(),Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Settings, Settings::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@Settings,response.message(),Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Settings, Settings::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            })
        }else{
            Toast.makeText(this@Settings,"Gagal Generate ID Report",Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SimpleDateFormat", "DefaultLocale")
    private fun generateIDTrxPenjualan() : String{
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMdd")
        val tglhariini: Int = formatter.format(date).toInt()
        var generateId = ""
        val cut : Int
        var take = 0

        if (arrayId_Trx_Penjualah.isNotEmpty()) {
            cut = arrayId_Trx_Penjualah[arrayId_Trx_Penjualah.lastIndex].toString().take(6).toInt()
            take = arrayId_Trx_Penjualah[arrayId_Trx_Penjualah.lastIndex].toString().subSequence(6, 9).toString().toInt()
            if (cut != tglhariini){
                take = 0
                val id = take
                val num = java.lang.String.format("%03d", id)
                generateId = "OP$tglhariini$num"
            }else if(cut == tglhariini){
                val id = take
                val num = java.lang.String.format("%03d", id)
                generateId = "OP$tglhariini$num"
            }
        }else{
            val id = take
            val num = java.lang.String.format("%03d", id)
            generateId = "OP$tglhariini$num"
        }
        return generateId
    }

    private fun settingNota(){}

    override fun onBackPressed() {
        val intent = Intent(this,Main_Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }

}
