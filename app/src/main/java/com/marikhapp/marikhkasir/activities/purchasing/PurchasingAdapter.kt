package com.marikhapp.marikhkasir.activities.purchasing

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.marikhapp.marikhkasir.R

class PurchasingAdapter(val arrayList : ArrayList<PurchasingObject>,val context: Context) : BaseAdapter()  {
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_purchasing,parent,false)
       val holder = Holder()
        holder.namaPembelian = view.findViewById(R.id.textviewNamaPembelian)
        holder.kategoriPembelian = view.findViewById(R.id.textviewKategoriPembelian)
        holder.jumlahPembelian = view.findViewById(R.id.textviewJumlahPembelian)
        holder.hargaPembelian = view.findViewById(R.id.textviewHargaPembelian)
        holder.totalPembelian = view.findViewById(R.id.textviewTotalPembelian)

        val purchasing = arrayList[position]

        holder.namaPembelian.text = purchasing.nama_pembelian
        holder.kategoriPembelian.text = purchasing.namaKategoriPembelian
        holder.jumlahPembelian.text = purchasing.jumlah_pembelian.toString()
        holder.hargaPembelian.text = "Rp.${purchasing.harga_pembelian}"
        holder.totalPembelian.text = "Rp.${purchasing.total_pembelian}"

            return view
    }

    override fun getItem(position: Int): PurchasingObject {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    class Holder{
        lateinit var namaPembelian : TextView
        lateinit var kategoriPembelian : TextView
        lateinit var jumlahPembelian : TextView
        lateinit var hargaPembelian : TextView
        lateinit var totalPembelian : TextView
    }
}