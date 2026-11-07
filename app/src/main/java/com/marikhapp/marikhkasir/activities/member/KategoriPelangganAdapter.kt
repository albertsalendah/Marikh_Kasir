package com.marikhapp.marikhkasir.activities.member

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.marikhapp.marikhkasir.R

class KategoriPelangganAdapter(val arrayKategoriPelanggan : ArrayList<KategoriPelangganObject>, val context: Context) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_kat_purchasing,parent,false)
        val holder = Holder()
        holder.no = view.findViewById<TextView>(R.id.textViewNoP)
        holder.textViewNamaKatPurchasing = view.findViewById<TextView>(R.id.textViewNamaKatPurchasing)

        val kategori = arrayKategoriPelanggan[position]
        holder.no.text = (position + 1).toString()
        holder.textViewNamaKatPurchasing.text = kategori.nama_kat_pelanggan

        return view
    }

    override fun getItem(position: Int): KategoriPelangganObject {
        return arrayKategoriPelanggan[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayKategoriPelanggan.size
    }
    class Holder{
        lateinit var no : TextView
        lateinit var textViewNamaKatPurchasing : TextView
    }
}