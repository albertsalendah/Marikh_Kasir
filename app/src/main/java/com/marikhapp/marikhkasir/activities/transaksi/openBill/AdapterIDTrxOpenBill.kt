package com.marikhapp.marikhkasir.activities.transaksi.openBill

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DetailPenjualan.DetailPenjualan
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HolderId(itemView: View) : RecyclerView.ViewHolder(itemView){
     var checkBoxId_Trx = itemView.findViewById(R.id.checkBoxIdTrxOpenBill) as CheckBox
     var id_trx : TextView = itemView.findViewById(R.id.textViewIdTrx) as TextView
     var m_g : TextView = itemView.findViewById(R.id.textViewMG) as TextView
     var total_trx : TextView = itemView.findViewById(R.id.textViewTotalTrxOpenBill) as TextView
     var buttonDetail : Button = itemView.findViewById(R.id.buttondetails) as Button
}

class AdapterIDTrxOpenBill (var arrayId : ArrayList<OpenBillObject>,val comefrom : String,val context : Context,val activity : Activity, val checkBox: CheckBox,val rv : RecyclerView,clickFrom:String) : RecyclerView.Adapter<HolderId>(){
    //lateinit var adapterPesananOpenBill: AdapterPesananOpenBill
    @SuppressLint("ViewHolder", "SetTextI18n")
    var jumalahpesanan = 0
    var count = 0
    var pos : Int? = null
    val clickedFrom = clickFrom
    private var selectedPosition = -1
    fun getPesanan(position: Int) : ArrayList<OpenBillPesananObject>{
        val arrayP = ArrayList<OpenBillPesananObject>()
        Client.instance.getdetailpenjualan(arrayId[position].id_trx_penjualan.toInt()).enqueue(object : Callback<List<DetailPenjualan>>{
            override fun onFailure(call: Call<List<DetailPenjualan>>, t: Throwable) {}

            override fun onResponse(call: Call<List<DetailPenjualan>>, response: Response<List<DetailPenjualan>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    for (item in data!!){
                        jumalahpesanan = data.size
                        arrayP.add(OpenBillPesananObject(item.id_trx_detpenjualan,item.id_trx_penjualan,item.qty_terjual,item.harga_terjual,item.diskon_terjual,item.status_pesanan,item.id_produk,item.jenis_transaksi,item.deskripsi,item.catatan_tambahan))
                    }
                }
            }
        })
        return arrayP
    }

    fun getIdStats(position: Int,holder : HolderId) {
        var stats: String
        Client.instance.getidtransaksi(arrayId[position].id_trx_penjualan.toInt()).enqueue(object : Callback<TrxPenjualan>{
            override fun onFailure(call: Call<TrxPenjualan>, t: Throwable) {}

            override fun onResponse(call: Call<TrxPenjualan>, response: Response<TrxPenjualan>) {
               if (response.body()!!.id_pelanggan == "1"){
                   stats = "G"
               }else{
                   stats = "M"
               }
                holder.m_g.text = stats
            }
        })
    }

    private fun popup(view: View,context: Context,id: Int){
        val menu = PopupMenu(context, view)
        menu.menu.add("Tambah Pesanan")
        menu.show()
        menu.setOnMenuItemClickListener { item ->
            when{
                item.toString() == "Tambah Pesanan" -> {
                    getIdInfo(id)
                }
            }
            true
        }
    }

    private fun getIdInfo(id:Int) {

        Client.instance.getidtransaksi(id).enqueue(object : Callback<TrxPenjualan>{
            override fun onFailure(call: Call<TrxPenjualan>, t: Throwable) {}

            override fun onResponse(call: Call<TrxPenjualan>, response: Response<TrxPenjualan>) {
               val data = response.body()
                if (response.isSuccessful){
                    if (data != null){
                        val  id_trx_penjualan = data.id_trx_penjualan
                        val  tgl_trx_input    = data.tgl_trx_input
                        val  tgl_trx_bayar    = data.tgl_trx_bayar
                        val  tagihan_akhir    = data.tagihan_akhir
                        val  tagihan_awal     = data.tagihan_awal
                        val  total_diskon     = data.total_diskon
                        val  tax              = data.tax
                        val  terbayar         = data.terbayar
                        val  id_pelanggan     = data.id_pelanggan
                        val  nama             = data.nama
                        val  no_meja          = data.no_meja
                        val  id_method_bayar  = data.id_method_bayar.toString()
                        val  jenis_tagihan    = data.jenis_tagihan
                        val  id_user          = data.id_user.toString()

                        val intent = Intent(activity,Transaksi::class.java)
                        intent.putExtra("id_trx_penjualan",id_trx_penjualan)
                        intent.putExtra("tgl_trx_input",tgl_trx_input)
                        intent.putExtra("tgl_trx_bayar",tgl_trx_bayar)
                        intent.putExtra("tagihan_akhir",tagihan_akhir)
                        intent.putExtra("tagihan_awal",tagihan_awal)
                        intent.putExtra("total_diskon",total_diskon)
                        intent.putExtra("tax",tax)
                        intent.putExtra("terbayar",terbayar)
                        intent.putExtra("id_pelanggan",id_pelanggan)
                        intent.putExtra("nama",nama)
                        intent.putExtra("no_meja",no_meja)
                        intent.putExtra("id_method_bayar",id_method_bayar)
                        intent.putExtra("jenis_tagihan",jenis_tagihan)
                        intent.putExtra("id_user",id_user)
                        intent.putExtra("jumalahpesanan",jumalahpesanan)
                        activity.setResult(Activity.RESULT_OK,intent)
                        activity.finish()
                    }
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderId {
        val view  = LayoutInflater.from(context).inflate(R.layout.adapter_id_trx_open_bill,parent,false)
        return HolderId(view)
    }

    override fun getItemCount(): Int {
        return arrayId.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HolderId, position: Int) {
        val id = arrayId[position]
        val intent = Intent("ID_TRX")

        if (clickedFrom == "listNama"){
            arrayId[position].checked = false
            checkBox.isChecked = false
            holder.checkBoxId_Trx.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    count +=1
                    arrayId[position].checked = true
                    intent.putExtra("id_trx", arrayId[position])
                    intent.putExtra("action", "Tambah")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                }else{
                    count -=1
                    arrayId[position].checked = false
                    intent.putExtra("id_trx", arrayId[position])
                    intent.putExtra("action", "Hapus")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                }
                checkBox.isChecked = count > 0
                if (count == 0){
                    rv.visibility = View.GONE
                }
            }
            if (!checkBox.isChecked){
                intent.putExtra("id_trx", arrayId[position])
                intent.putExtra("action", "Hapus")
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }else{
            arrayId[position].checked = checkBox.isChecked
            holder.checkBoxId_Trx.tag = position
            holder.checkBoxId_Trx.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    count +=1
                    selectedPosition = count
                    intent.putExtra("id_trx", arrayId[position])
                    intent.putExtra("action", "Tambah")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                }else{
                    count -=1
                    selectedPosition = count
                    intent.putExtra("id_trx", arrayId[position])
                    intent.putExtra("action", "Hapus")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                }
              checkBox.isChecked = count > 0
            }

            if (!checkBox.isChecked){
                intent.putExtra("id_trx", arrayId[position])
                intent.putExtra("action", "Hapus")
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                rv.visibility =View.GONE
            }
        }

        holder.checkBoxId_Trx.isChecked = arrayId[position].checked

        getIdStats(position,holder)
        holder.id_trx.text = id.id_trx_penjualan
        holder.total_trx.text = "Rp.${id.tagihan_akhir}"
        val view = holder.itemView

        holder.buttonDetail.setOnClickListener {
            val intentD = Intent(context,DetailTransaksiOpenBill::class.java)
            intentD.putExtra("id_transaksi_OB",arrayId[position].id_trx_penjualan.toInt())
            context.startActivity(intentD)
        }
        if (comefrom == "listhariini"){
            holder.id_trx.setOnClickListener {
                popup(view,context,id.id_trx_penjualan.toInt())
                getPesanan(position)
            }
        }
    }
}