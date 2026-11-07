package com.marikhapp.marikhkasir.activities.purchasing

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marikhapp.marikhkasir.R

class KategoriPurchasingAdapter(val arrayKategoriPurchasing : ArrayList<KategoriPurchasingObject>,val context: Context) :BaseAdapter()  {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_kat_purchasing,parent,false)
        val holder = Holder()
        holder.no = view.findViewById<TextView>(R.id.textViewNoP)
        holder.textViewNamaKatPurchasing = view.findViewById<TextView>(R.id.textViewNamaKatPurchasing)

        val kategori = arrayKategoriPurchasing[position]
        holder.no.text = (position + 1).toString()
        holder.textViewNamaKatPurchasing.text = kategori.nama_kat_pengeluaran

        return view
    }

    override fun getItem(position: Int): KategoriPurchasingObject {
        return arrayKategoriPurchasing[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayKategoriPurchasing.size
    }
    class Holder{
        lateinit var no : TextView
        lateinit var textViewNamaKatPurchasing : TextView
    }

}