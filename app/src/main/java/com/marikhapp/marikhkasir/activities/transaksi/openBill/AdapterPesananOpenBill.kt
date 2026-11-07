package com.marikhapp.marikhkasir.activities.transaksi.openBill

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.ProdukModels.Produk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterPesananOpenBill (var arrayProduk : ArrayList<OpenBillPesananObject>,val context: Context) : BaseAdapter(){

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val view  = LayoutInflater.from(context).inflate(R.layout.adapter_pesanan_open_bill,parent,false)
        holder.no = view.findViewById(R.id.textViewNoPesananOB)
        holder.namaProduk = view.findViewById(R.id.textViewPesananOpenBill)
        holder.qty = view.findViewById(R.id.textViewQTYOB)
        holder.totalTagihan = view.findViewById(R.id.textViewTotalPesananOpenBill)
        holder.status_pesanan = view.findViewById(R.id.textViewStatusPesanan)
        val pesanan = arrayProduk[position]

        holder.no.text = (position+1).toString()

        Client.instance.getproduk(arrayProduk[position].id_produk).enqueue(object : Callback<Produk>{
            override fun onFailure(call: Call<Produk>, t: Throwable) {}

            override fun onResponse(call: Call<Produk>, response: Response<Produk>) {
                if (response.isSuccessful){
                    val data = response.body()
                    holder.namaProduk.text = data!!.nama_produk
                }
            }
        })
        holder.totalTagihan.text = "Rp.${pesanan.harga_terjual}"
        holder.qty.text = "${pesanan.qty_terjual}"
        holder.status_pesanan.text = pesanan.status_pesanan
        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
       return arrayProduk.size
    }

    fun removeAt(position: Int) {
        arrayProduk.removeAt(position)
        //notifyItemRemoved(position)
    }

    fun restoreItem(item: OpenBillPesananObject, position: Int) {
        arrayProduk.add(position, item)
        //notifyItemInserted(position)
    }

    fun getData(): java.util.ArrayList<OpenBillPesananObject> {
        return arrayProduk
    }

    class Holder{
        lateinit var no : TextView
        lateinit var namaProduk : TextView
        lateinit var qty : TextView
        lateinit var totalTagihan : TextView
        lateinit var status_pesanan : TextView
    }
}