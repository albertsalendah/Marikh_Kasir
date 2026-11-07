package com.marikhapp.marikhkasir.activities.transaksi.Bayar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.methodbayar.MethodBayarObject
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import com.marikhapp.marikhkasir.activities.transaksi.openBill.OpenBill
import com.marikhapp.marikhkasir.activities.transaksi.openBill.OpenBillObject
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.MethodBayarModels.MethodBayarModel
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualanRespon
import kotlinx.android.synthetic.main.activity_bayar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING","SimpleDateFormat", "DefaultLocale",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class Bayar : AppCompatActivity() {
    val arrayId_Trx_Penjualah = ArrayList<Int>()
    val context = this@Bayar
    var arrayListMethodBayar = ArrayList<MethodBayarObject>()
    lateinit var adapter : AdapterListMethodBayar
    var komisi = 0
    private var selectedPosition = -1
    var total_tagihan : String? = ""
    var total_diskon : Int? = 0
    var tagihan_awal : Int? = 0
    var checkedTRX = ArrayList<OpenBillObject>()
    var id_trans = ""
    var tax = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
        setContentView(R.layout.activity_bayar)

        val jenis_transaksi : String? = intent.getStringExtra("jenistransaksi")
        /*val total_tagihan : String? = intent.getStringExtra("total_tagihan")
        val total_diskon : Int? = intent.getIntExtra("total_diskon",0)
        val tagihan_awal : Int? = intent.getIntExtra("tagihan_awal",0)*/
        //textViewTotalBayar.text = total_tagihan

        if (jenis_transaksi != null) {
            Log.d("test123",jenis_transaksi)
        }

        if (jenis_transaksi == "Gofood" || jenis_transaksi == "Grabfood"){
            buttonBayar.isEnabled = true
            editTextBayar.isEnabled = false
            editTextKembali.isEnabled = false
             total_tagihan = intent.getStringExtra("total_tagihan")
             total_diskon  = intent.getIntExtra("total_diskon",0)
             tagihan_awal  = intent.getIntExtra("tagihan_awal",0)
            textViewTotalBayar.text = total_tagihan
        }else if (jenis_transaksi == "Dine In" || jenis_transaksi == "Takeaway"){
            editTextBayar.isEnabled = true
            editTextKembali.isEnabled = true
            total_tagihan = intent.getStringExtra("total_tagihan")
            total_diskon  = intent.getIntExtra("total_diskon",0)
            tagihan_awal  = intent.getIntExtra("tagihan_awal",0)
            textViewTotalBayar.text = total_tagihan
        }else if (jenis_transaksi == "openbill"){
            checkedTRX = (intent.getSerializableExtra("array_trx_openbill") as ArrayList<OpenBillObject>)
            total_tagihan = intent.getStringExtra("total_tagihan")
            textViewTotalBayar.text = total_tagihan
            if (checkedTRX.isNotEmpty()){
                Log.d("test123",checkedTRX.size.toString())
            }
        }else{
            total_tagihan = intent.getStringExtra("total_tagihan")
            id_trans = intent.getStringExtra("id_trans").toString()
            tax = intent.getIntExtra("tax",0)
            textViewTotalBayar.text = total_tagihan
        }

        var kembali : Int = 0
        editTextBayar.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
               if (s!!.isNotEmpty()){
                   kembali = s.toString().toInt() - total_tagihan.toString().toInt()
                   buttonBayar.isEnabled = !(editTextBayar.text.isEmpty() || kembali<0)
                   if (kembali >= 0){
                       editTextKembali.setText(kembali.toString())
                   }else{
                       editTextBayar.error = "Jumlah uang yang diinput kurang"
                   }
               }else{
                   editTextKembali.setText("0")
               }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()){
                    kembali = s.toString().toInt() - total_tagihan.toString().toInt()
                    buttonBayar.isEnabled = !(editTextBayar.text.isEmpty() || kembali<0)
                    if (kembali >= 0){
                        editTextKembali.setText(kembali.toString())
                    }else{
                        editTextBayar.error = "Jumlah uang yang diinput kurang"
                    }
                }else{
                    editTextKembali.setText("0")
                }
            }
        })

        /*buttonBayar.setOnClickListener {
            val idRadioButton = radioGroupMethodBayar.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(idRadioButton)
            val methodbayar = selectedRadioButton.text.toString()
            var idmethodbayar : String = ""
            var terbayar : String = ""
            if (methodbayar == "CASH"){
                idmethodbayar = "1"
            }else if(methodbayar == "OVO"){
                idmethodbayar = "2"
            }else if(methodbayar == "GoPay"){
                idmethodbayar = "3"
            }else if(methodbayar == "Gofood"){
                idmethodbayar = "4"
            }else if(methodbayar == "Grabfood"){
                idmethodbayar = "5"
            }
            if (jenis_transaksi == "Olfood"){
                terbayar = total_tagihan.toString()
            }else{
                terbayar = editTextBayar.text.toString()
            }
            val intent = Intent(this,Transaksi::class.java)
            intent.putExtra("idmethodbayar",idmethodbayar)
            intent.putExtra("terbayar",terbayar)
            intent.putExtra("total_diskon",total_diskon)
            intent.putExtra("tagihan_awal",tagihan_awal)
            this.setResult(Activity.RESULT_OK,intent)
            this.finish()
        }

        Client.instance.daftartrxpenjualan().enqueue(object : Callback<List<TrxPenjualan>>{
            override fun onFailure(call: Call<List<TrxPenjualan>>, t: Throwable) {}

            override fun onResponse(call: Call<List<TrxPenjualan>>, response: Response<List<TrxPenjualan>>) {
                if (response.isSuccessful){
                    buttonBayar.isEnabled = true
                    val listtransaksi : List<TrxPenjualan>? = response.body()
                    for (item in listtransaksi!!){
                        arrayId_Trx_Penjualah.add(item.id_trx_penjualan.toInt())
                    }
                    Toast.makeText(context,"Status OK",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                }
            }
        })*/

        adapter = AdapterListMethodBayar(arrayListMethodBayar,context)
        gridViewMethodBayar.adapter = adapter
        adapter.notifyDataSetChanged()

        Client.instance.daftarmethodbayar().enqueue(object : Callback<List<MethodBayarModel>>{
            override fun onFailure(call: Call<List<MethodBayarModel>>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<MethodBayarModel>>, response: Response<List<MethodBayarModel>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        for (item in data){
                            if (jenis_transaksi == "Gofood" || jenis_transaksi == "Grabfood"){
                                if (item.olfood == "1"){
                                    arrayListMethodBayar.add(MethodBayarObject(item.id_method_bayar,item.nama_method.toString(),item.reguler,item.olfood))
                                }
                            }else{
                                if (item.reguler == "1"){
                                    arrayListMethodBayar.add(MethodBayarObject(item.id_method_bayar,item.nama_method.toString(),item.reguler,item.olfood))
                                }
                            }
                        }
                        adapter.notifyDataSetChanged()
                        buttonBayar.setOnClickListener {
                            val date = Date()
                            val formatter = SimpleDateFormat("yyyyMMddkkmmss")
                            val methodbayar = adapter.getSelectedItem()?.toUpperCase()
                            var idmethodbayar  = ""
                            var terbayar  = ""
                            if (methodbayar.isNullOrEmpty()){
                                Toast.makeText(context,"Pilih Method Bayar",Toast.LENGTH_SHORT).show()
                            }else{
                                for (item in data){
                                    if (methodbayar == item.nama_method?.toUpperCase()){
                                        idmethodbayar = item.id_method_bayar
                                    }
                                }

                                if (jenis_transaksi == "Gofood" || jenis_transaksi == "Grabfood"){
                                    terbayar = total_tagihan.toString()
                                    if (jenis_transaksi == "Gofood"){
                                        val preferencesGoFood = context.getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
                                        val komisiGoFood = preferencesGoFood.getInt("KomisiGoFood", 0)
                                        komisi = total_tagihan.toString().toInt()*komisiGoFood/100
                                    }else if (jenis_transaksi == "Grabfood"){
                                        val preferencesGrabfood = context.getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
                                        val komisiGrabFood = preferencesGrabfood.getInt("KomisiGrabFood", 0)
                                        komisi = total_tagihan.toString().toInt()*komisiGrabFood/100
                                    }
                                    bayar(idmethodbayar,terbayar,total_diskon,tagihan_awal,komisi)
                                }else if (jenis_transaksi == "Dine In" || jenis_transaksi == "Takeaway"){
                                    terbayar = editTextBayar.text.toString()
                                    komisi = 0
                                    if (terbayar.isNullOrEmpty()){
                                        editTextBayar.error = "Isi Kolom Bayar"
                                        editTextBayar.requestFocus()
                                    }else{
                                        bayar(idmethodbayar,terbayar,total_diskon,tagihan_awal,komisi)
                                    }
                                }else if (jenis_transaksi == "openbill"){
                                    val trx_tgl_bayar : String = formatter.format(date)
                                    terbayar = editTextBayar.text.toString()
                                    if (terbayar.isNullOrEmpty()){
                                        editTextBayar.error = "Isi Kolom Bayar"
                                        editTextBayar.requestFocus()
                                    }else{
                                        bayarOpenBill(checkedTRX,terbayar.toInt(),idmethodbayar,trx_tgl_bayar)
                                    }
                                }else{
                                    val trx_tgl_bayar : String = formatter.format(date)
                                    terbayar = editTextBayar.text.toString()
                                    if (terbayar.isNullOrEmpty()){
                                        editTextBayar.error = "Isi Kolom Bayar"
                                        editTextBayar.requestFocus()
                                    }else{
                                        bayarOpenBillSingle(id_trans,terbayar.toInt(),idmethodbayar,trx_tgl_bayar,tax)
                                    }
                                }
                            }
                        }

                        Client.instance.daftartrxpenjualan().enqueue(object : Callback<List<TrxPenjualan>>{
                            override fun onFailure(call: Call<List<TrxPenjualan>>, t: Throwable) {}

                            override fun onResponse(call: Call<List<TrxPenjualan>>, response: Response<List<TrxPenjualan>>) {
                                if (response.isSuccessful){
                                    buttonBayar.isEnabled = true
                                    val listtransaksi : List<TrxPenjualan>? = response.body()
                                    for (item in listtransaksi!!){
                                        arrayId_Trx_Penjualah.add(item.id_trx_penjualan.toInt())
                                    }
                                    Toast.makeText(context,"Status OK",Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }
                }else{
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })

        layoutConBayar.setOnClickListener {
            this.finish()
        }
    }

    private fun bayar(idmethodbayar : String,terbayar : String,total_diskon : Int?,tagihan_awal : Int?,komisi : Int ){
        val intent = Intent(context,Transaksi::class.java)
        intent.putExtra("idmethodbayar",idmethodbayar)
        intent.putExtra("terbayar",terbayar)
        intent.putExtra("total_diskon",total_diskon)
        intent.putExtra("tagihan_awal",tagihan_awal)
        intent.putExtra("komisi",komisi)
        context.setResult(Activity.RESULT_OK,intent)
        context.finish()
    }

    private fun bayarOpenBill(checkedTRX : ArrayList<OpenBillObject>,bayar : Int,id_method_bayar : String,trx_tgl_bayar : String){
        var bayar1 = bayar
        val id_method_bayar = id_method_bayar
        val jenis_tagihan = "Lunas"
        for (item in checkedTRX){
            bayar1 -=  item.tagihan_akhir
            val terbayar = bayar1 + item.tagihan_akhir
            Client.instance.updatetrxpenjualan(item.id_trx_penjualan,trx_tgl_bayar,item.tax,terbayar.toString(),id_method_bayar,jenis_tagihan).enqueue(object : Callback<TrxPenjualanRespon>{
                override fun onFailure(call: Call<TrxPenjualanRespon>, t: Throwable) {
                    Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<TrxPenjualanRespon>, response: Response<TrxPenjualanRespon>) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,"Pembayaran Berhasil",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Bayar, OpenBill::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        this@Bayar.finish()
                    }
                }
            })
        }
    }

    private fun bayarOpenBillSingle(id_trans : String,bayar : Int,id_method_bayar : String,trx_tgl_bayar : String,tax:Int?){
        val id_method_bayar = id_method_bayar
        val jenis_tagihan = "Lunas"
        Client.instance.updatetrxpenjualan(id_trans,trx_tgl_bayar,tax,bayar.toString(),id_method_bayar,jenis_tagihan).enqueue(object : Callback<TrxPenjualanRespon>{
            override fun onFailure(call: Call<TrxPenjualanRespon>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<TrxPenjualanRespon>, response: Response<TrxPenjualanRespon>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,"Pembayaran Berhasil",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Bayar, OpenBill::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    this@Bayar.finish()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
    /*override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_OUTSIDE == event.action) {
            //val intent = Intent(this, Transaksi::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //startActivity(intent)
            this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            this.finish()
            return true
        }
        return super.onTouchEvent(event)
    }*/
}
