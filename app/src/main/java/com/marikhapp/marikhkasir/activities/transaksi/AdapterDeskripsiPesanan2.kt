package com.marikhapp.marikhkasir.activities.transaksi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.marikhapp.marikhkasir.R

class AdapterDeskripsiPesanan2(var arrayListDeskripsi : ArrayList<String>,val context: Context) : BaseAdapter() {
    private var inflator : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val view : View = inflator.inflate(R.layout.adapter_daftar_deskripsi,null)
        holder.textViewNamaDeskripsi = view.findViewById(R.id.textViewNamaDeskripsi)
        holder.textViewNamaDeskripsi.text = arrayListDeskripsi[position]+", "
        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
       return arrayListDeskripsi.size
    }
    class Holder{
        lateinit var textViewNamaDeskripsi : TextView
    }
}