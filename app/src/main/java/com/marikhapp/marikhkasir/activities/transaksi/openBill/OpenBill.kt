package com.marikhapp.marikhkasir.activities.transaksi.openBill

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.transaksi.Bayar.Bayar
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualanRespon
import kotlinx.android.synthetic.main.activity_open_bill.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OpenBill : AppCompatActivity() {
    val contex = this@OpenBill
    val activity : Activity = this
    var arrayOpenbill = ArrayList<OpenBillObject>()
    lateinit var adapterOpenBill : OpenBillAdapter
    var arrayListNama = ArrayList<String>()
    var Checked = ArrayList<Boolean>()
    var CheckedHariIni = ArrayList<Boolean>()
    var CheckedTglHariIni = ArrayList<Boolean>()
    var CheckedTgl = ArrayList<Boolean>()
    var arrayOpenbillHariIni = ArrayList<OpenBillObject>()
    var arrayListNamaHariIni = ArrayList<String>()
    val checkedTrx = ArrayList<OpenBillObject>()

    lateinit var adapterOpenBillHariIni : OpenBillAdapter
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
        setContentView(R.layout.activity_open_bill)
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        textViewBopenBIll.text = "< LIst OpenBill"
        textViewBopenBIll.setOnClickListener {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
            this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            this.finish()
        }

        Client.instance.getjenistagihan("Open bill").enqueue(object : Callback<List<TrxPenjualan>>{
            override fun onFailure(call: Call<List<TrxPenjualan>>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<TrxPenjualan>>, response: Response<List<TrxPenjualan>>) {
                val bill = response.body()
                if (response.isSuccessful){
                    for (item in bill!!){
                        val date = Date()
                        val tgl : Date = formatter.parse(item.tgl_trx_input)!!
                        val tglInput = formatter.format(tgl)
                        val tglHariIni = formatter.format(date)
                        if (tglHariIni.replace("-","").toInt() <= tglInput.replace("-","").toInt()){
                            arrayOpenbillHariIni.add(OpenBillObject(item.id_trx_penjualan,tglInput,item.tgl_trx_bayar,item.tagihan_akhir,item.tagihan_awal,item.total_diskon,item.tax,item.terbayar,item.id_pelanggan,item.nama,item.no_meja,item.id_method_bayar,item.jenis_tagihan,item.id_user,false))
                            var duplicate  = false
                            for (item2 in arrayListNamaHariIni){
                                if (item2 == item.nama){
                                    duplicate = true
                                    break
                                }
                            }
                            if (!duplicate){
                                arrayListNamaHariIni.add(item.nama)
                                arrayListNamaHariIni.sortBy { it }
                                CheckedHariIni.add(false)
                            }
                        }else if (tglHariIni.replace("-","").toInt() > tglInput.replace("-","").toInt()){
                            arrayOpenbill.add(OpenBillObject(item.id_trx_penjualan,tglInput,item.tgl_trx_bayar,item.tagihan_akhir,item.tagihan_awal,item.total_diskon,item.tax,item.terbayar,item.id_pelanggan,item.nama,item.no_meja,item.id_method_bayar,item.jenis_tagihan,item.id_user,false))
                            var duplicate  = false
                            for (item2 in arrayListNama){
                                if (item2 == item.nama){
                                    duplicate = true
                                    break
                                }
                            }
                            if (!duplicate){
                                arrayListNama.add(item.nama)
                                arrayListNama.sortBy { it }
                                Checked.add(false)
                            }
                        }
                    }
                }
                adapterOpenBill.notifyDataSetChanged()
                adapterOpenBillHariIni.notifyDataSetChanged()
            }
        })
        val LayoutManager = LinearLayoutManager(this)
        val LayoutManagerOpenBill = LinearLayoutManager(this)
        val listOpenBillHariIni : RecyclerView = findViewById(R.id.listOpenbillHariIni)
        listOpenBillHariIni.layoutManager = LayoutManager
        adapterOpenBillHariIni = OpenBillAdapter(arrayListNamaHariIni,"listhariini",contex,activity,CheckedHariIni,arrayOpenbillHariIni)
        listOpenBillHariIni.adapter = adapterOpenBillHariIni
        adapterOpenBillHariIni.notifyDataSetChanged()

        val listOpenBill : RecyclerView = findViewById(R.id.listOpenBill)
        listOpenBill.layoutManager = LayoutManagerOpenBill
        adapterOpenBill = OpenBillAdapter(arrayListNama,"fulllist",contex,activity,Checked,arrayOpenbill)
        listOpenBill.adapter = adapterOpenBill
        adapterOpenBill.notifyDataSetChanged()

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,  IntentFilter("ID_TRX"))

    }
    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            val action = intent.getStringExtra("action")
            val id_trx = intent.getSerializableExtra("id_trx") as OpenBillObject
           // getCheckedId(action,id_trx)
            if (action == "Tambah"){
                checkedTrx.add(id_trx)
                adapterOpenBill.notifyDataSetChanged()
                adapterOpenBillHariIni.notifyDataSetChanged()
            }
            if (action == "Hapus"){
                checkedTrx.remove(id_trx)
                adapterOpenBill.notifyDataSetChanged()
                adapterOpenBillHariIni.notifyDataSetChanged()
            }
           // Log.d("test123",checkedTrx.size.toString())
            getTRX(checkedTrx,id_trx.id_trx_penjualan,action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCheckedId(action : String?, id_trx : OpenBillObject){
        val checkedIdTrx = ArrayList<OpenBillObject>()
        var count = 0
        val checkedId = ArrayList<OpenBillObject?>()
        if (action == "Tambah"){
            checkedTrx.add(id_trx)
        } else if (action == "Hapus"){
            checkedTrx.remove(id_trx)
            checkedIdTrx.remove(id_trx)
        }
        for (item in checkedTrx){
            var duplicate = false
            for (item2 in checkedIdTrx){
                if (item == item2){
                    duplicate = true
                }
            }
            if (!duplicate){
                checkedIdTrx.add(item)
            }
        }

        //getTRX(checkedIdTrx,id_trx.id_trx_penjualan,action)
    }
    private fun getCheckedTransaksi(id : ArrayList<OpenBillObject?>,id_trx : String?,action: String?){
        val checkedTRX = ArrayList<OpenBillObject>()
       /* Client.instance.getjenistagihan("Open bill").enqueue(object : Callback<List<TrxPenjualan>>{
            override fun onFailure(call: Call<List<TrxPenjualan>>, t: Throwable) {}

            override fun onResponse(call: Call<List<TrxPenjualan>>, response: Response<List<TrxPenjualan>>) {
                val data = response.body()
                for (item in data!!){
                    for (item2 in id){
                        if (item2 == item.id_trx_penjualan){
                            checkedTRX.add(OpenBillObject(item.id_trx_penjualan,item.tgl_trx_input,item.tgl_trx_bayar,item.tagihan_akhir,item.tagihan_awal,item.total_diskon,item.tax,item.terbayar,item.id_pelanggan,item.nama,item.no_meja,item.id_method_bayar,item.jenis_tagihan,item.id_user,false))
                        }
                    }
                }
                getTRX(checkedTRX,id_trx,action)
            }
        })*/
        //getTRX(id,id_trx,action)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun getTRX(checkedTRX : ArrayList<OpenBillObject>, id_trx : String?, action: String?) {

        val buttonBayar = findViewById<TextView>(R.id.buttonBayarOpenBill)
        var totalTagihanAll = 0
        var kembali = 0
           for (item in checkedTRX){
               totalTagihanAll += item.tagihan_akhir
               if (action == "Hapus" && id_trx == item.id_trx_penjualan){
                   totalTagihanAll -= item.tagihan_akhir
               }
           }
        buttonBayar.isEnabled = checkedTRX.isNotEmpty()
        buttonBayar.text = "Rp.$totalTagihanAll"
        if (checkedTRX.isNotEmpty()){
          //  buttonBayar.text = "Rp.$totalTagihanAll"
            buttonBayar.setOnClickListener {
                val intent = Intent(contex,Bayar::class.java)
                intent.putExtra("array_trx_openbill",checkedTRX)
                intent.putExtra("total_tagihan",totalTagihanAll.toString())
                intent.putExtra("jenistransaksi","openbill")
                contex.startActivity(intent)
                /*val trx_tgl_bayar : String = formatter.format(date)
                val bayar = editTextBayar.text.toString().toInt()
                bayarOpenBill(checkedTRX,bayar,kembali,trx_tgl_bayar)*/
            }
        }
    }
    private fun bayarOpenBill(checkedTRX : ArrayList<OpenBillObject>,bayar : Int,kembali : Int,trx_tgl_bayar : String){
        var bayar1 = bayar
        val id_method_bayar = "1"
        val jenis_tagihan = "Lunas"
        for (item in checkedTRX){
           bayar1 -=  item.tagihan_akhir
            val terbayar = bayar1 + item.tagihan_akhir
            Client.instance.updatetrxpenjualan(item.id_trx_penjualan,trx_tgl_bayar,item.tax,terbayar.toString(),id_method_bayar,jenis_tagihan).enqueue(object : Callback<TrxPenjualanRespon>{
                override fun onFailure(call: Call<TrxPenjualanRespon>, t: Throwable) {}

                override fun onResponse(call: Call<TrxPenjualanRespon>, response: Response<TrxPenjualanRespon>) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,"Pembayaran Berhasil",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@OpenBill, Transaksi::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        this@OpenBill.finish()
                    }
                }
            })
        }
    }

    fun openBill() : ArrayList<OpenBillObject>{
        return arrayOpenbill
    }

    fun openBillHariIni() : ArrayList<OpenBillObject>{
        return arrayOpenbillHariIni
    }

    private fun a(){
        //val args = intent.getBundleExtra("BUNDLE")
        //val dataOpenBill : ArrayList<PesananObject> = args.getSerializable("ARRAYLIST") as ArrayList<PesananObject>


        //Passing Array
        /*val args = Bundle()
                  args.putSerializable("ARRAYLIST", arrayPesanan)
                  val intent = Intent(this,OpenBill::class.java)
                  intent.putExtra("BUNDLE",args)
                  startActivity(intent)*/
    }
    override fun onBackPressed() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onBackPressed()
        this.finish()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_OUTSIDE == event.action) {
            //val intent = Intent(this, Transaksi::class.java)
           // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
           // startActivity(intent)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
            this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            this.finish()
            return true
        }
        return super.onTouchEvent(event)
    }
}
