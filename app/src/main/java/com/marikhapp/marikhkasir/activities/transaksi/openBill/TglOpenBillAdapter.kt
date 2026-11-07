package com.marikhapp.marikhkasir.activities.transaksi.openBill

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import kotlinx.android.synthetic.main.adapter_id_trx_open_bill.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HolderTgl(itemView: View) : RecyclerView.ViewHolder(itemView){
     var checkBoxTglOpenBill : CheckBox = itemView.findViewById(R.id.checkBoxTglOpenBill)
     var textViewTglOpenBill : TextView = itemView.findViewById(R.id.textViewTglOpenBill)
     var textViewTglTotalOpenBill : TextView = itemView.findViewById(R.id.textViewTglTotalOpenBill)
     var listPesananOpenBill : RecyclerView = itemView.findViewById(R.id.listPesananOpenBill)
}

class TglOpenBillAdapter(var namapelanggan:String,var arrayOpenBill:ArrayList<OpenBillObject>,
                         val comefrom : String,val context:Context,val activity : Activity,var checkTgl : ArrayList<Boolean>,
                         val checkboxnama : CheckBox,val rv : RecyclerView) : RecyclerView.Adapter<HolderTgl>() {
    lateinit var adapterIDTrxOpenBill: AdapterIDTrxOpenBill
    var checkedcount = 0
    var checkedIdTrx = ArrayList<Boolean>()
    var checkedIdTrxHariIni = ArrayList<Boolean>()
    var chekedTrx = ArrayList<String>()

    override fun onBindViewHolder(holder: HolderTgl, position: Int) {
        val listTgl = arrayOpenBill[position]
        holder.textViewTglOpenBill.text =  listTgl.tgl_trx_input

        holder.textViewTglTotalOpenBill.text = ""
        var jclick = 0
        holder.textViewTglOpenBill.setOnClickListener {
            jclick += 1
            if (jclick % 2 == 0){
                holder.listPesananOpenBill.visibility = View.GONE
            }else{
                holder.listPesananOpenBill.visibility = View.VISIBLE
                if (comefrom == "listhariini"){
                    val params = holder.listPesananOpenBill.layoutParams
                    if (arrayOpenBill.size > 0){
                        val heigh = ((adapterIDTrxOpenBill.itemCount)*100)
                        params.height = heigh
                        holder.listPesananOpenBill.layoutParams = params
                    }else{
                        params.height = 0
                        holder.listPesananOpenBill.layoutParams = params
                    }
                }else{
                    val params = holder.listPesananOpenBill.layoutParams
                    if (getIDTRX(position).size > 0){
                        val heigh = ((adapterIDTrxOpenBill.itemCount)*100)
                        params.height = heigh
                        holder.listPesananOpenBill.layoutParams = params
                    }else{
                        params.height = 0
                        holder.listPesananOpenBill.layoutParams = params
                    }
                }
            }
        }

        holder.checkBoxTglOpenBill.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                arrayOpenBill[position].checked = true
                checkedcount += 1
                holder.listPesananOpenBill.visibility = View.VISIBLE
            }else{
                arrayOpenBill[position].checked = false
                checkedcount -= 1
                holder.listPesananOpenBill.visibility = View.GONE
            }
            checkboxnama.isChecked = checkedcount > 0
            if (checkedcount == 0){
                rv.visibility = View.GONE
            }
        }

        if (checkboxnama.isChecked){
            holder.listPesananOpenBill.visibility = View.VISIBLE
        }
        holder.checkBoxTglOpenBill.isChecked = arrayOpenBill[position].checked
        setAdapterIDTRX(holder,position)
    }

    private fun setAdapterIDTRX(holder: HolderTgl,position: Int){
        val LayoutManager = LinearLayoutManager(context)
        holder.listPesananOpenBill.layoutManager = LayoutManager
        if (comefrom == "listhariini"){
           /* adapterIDTrxOpenBill = AdapterIDTrxOpenBill(getIDTRXHariIni(position),getIDTRXHariIniTotal(position),"listhariini",context,activity,holder.checkBoxTglOpenBill,holder.listPesananOpenBill)
            holder.listPesananOpenBill.adapter = adapterIDTrxOpenBill
            adapterIDTrxOpenBill.notifyDataSetChanged()*/
        }else{
          /*  adapterIDTrxOpenBill = AdapterIDTrxOpenBill(getIDTRX(position),getIDTRXTotal(position),"fulllist",context,activity,holder.checkBoxTglOpenBill,holder.listPesananOpenBill)
            holder.listPesananOpenBill.adapter = adapterIDTrxOpenBill
            adapterIDTrxOpenBill.notifyDataSetChanged()*/
        }
    }

    fun getIDTRX(position: Int) : ArrayList<OpenBillObject>{
        val id = ArrayList<OpenBillObject>()
        val listData = (context as OpenBill).openBill()
        for (item in listData){
            if (item.nama == namapelanggan && item.tgl_trx_input == arrayOpenBill[position].tgl_trx_input){
                id.add(OpenBillObject(item.id_trx_penjualan,item.tgl_trx_input,item.tgl_trx_bayar,item.tagihan_akhir,item.tagihan_awal,item.total_diskon,item.tax,item.terbayar,item.id_pelanggan,item.nama,item.no_meja,item.id_method_bayar,item.jenis_tagihan,item.id_user,item.checked))
                checkedIdTrx.add(false)
            }
        }
        return id
    }

    fun getIDTRXTotal(position: Int) : ArrayList<Int>{
        val total_tagihan = ArrayList<Int>()
        val listData = (context as OpenBill).openBill()
        for (item in listData){
            if (item.nama == namapelanggan && item.tgl_trx_input == arrayOpenBill[position].tgl_trx_input){
                total_tagihan.add(item.tagihan_akhir)
            }
        }
        return total_tagihan
    }

    fun getIDTRXHariIni(position: Int) : ArrayList<OpenBillObject>{
        val id = ArrayList<OpenBillObject>()
        val listData = (context as OpenBill).openBillHariIni()
        for (item in listData){
            if (item.nama == namapelanggan && item.tgl_trx_input == arrayOpenBill[position].tgl_trx_input){
                id.add(OpenBillObject(item.id_trx_penjualan,item.tgl_trx_input,item.tgl_trx_bayar,item.tagihan_akhir,item.tagihan_awal,item.total_diskon,item.tax,item.terbayar,item.id_pelanggan,item.nama,item.no_meja,item.id_method_bayar,item.jenis_tagihan,item.id_user,item.checked))
                checkedIdTrxHariIni.add(false)
            }
        }
        return id
    }

    fun getIDTRXHariIniTotal(position: Int) : ArrayList<Int>{
        val total_tagihan = ArrayList<Int>()
        val listData = (context as OpenBill).openBillHariIni()
        for (item in listData){
            if (item.nama == namapelanggan && item.tgl_trx_input == arrayOpenBill[position].tgl_trx_input){
                total_tagihan.add(item.tagihan_akhir)
            }
        }
        return total_tagihan
    }

    @SuppressLint("SetTextI18n")
    private fun getTransaksi(id : ArrayList<Int>){
        val arrayTransaksi = ArrayList<OpenBillObject>()
        var total = 0
            Client.instance.getjenistagihan("Open bill").enqueue(object : Callback<List<TrxPenjualan>>{
                override fun onFailure(call: Call<List<TrxPenjualan>>, t: Throwable) {}

                override fun onResponse(call: Call<List<TrxPenjualan>>, response: Response<List<TrxPenjualan>>) {
                    val data = response.body()
                    if (response.isSuccessful){
                        for (item in data!!){
                            for (item2 in id){
                                if (item.id_trx_penjualan == item2.toString()){
                                    arrayTransaksi.add(OpenBillObject(item.id_trx_penjualan,item.tgl_trx_input,item.tgl_trx_bayar,item.tagihan_akhir,item.tagihan_awal,item.total_diskon,item.tax,item.terbayar,item.id_pelanggan,item.nama,item.no_meja,item.id_method_bayar,item.jenis_tagihan,item.id_user,false))
                                   // Log.d("test123","${arrayTransaksi.size}")
                                }
                            }
                        }
                    }
                }
            })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderTgl {
        val view  = LayoutInflater.from(context).inflate(R.layout.adapter_tgl_open_bill, parent, false)
        return HolderTgl(view)
    }

    override fun getItemCount(): Int {
        return arrayOpenBill.size
    }
}