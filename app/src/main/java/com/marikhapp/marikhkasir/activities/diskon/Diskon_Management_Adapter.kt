package com.marikhapp.marikhkasir.activities.diskon

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.marikhapp.marikhkasir.R

class Diskon_Management_Adapter(var arrayListDiskon : ArrayList<DiskonObject>,val context: Context) : BaseAdapter(){
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_diskon_management,parent,false)
        val holder = Holder()
        holder.no = view.findViewById<TextView>(R.id.textViewNoDisc)
        holder.deskripsiDiskon = view.findViewById<TextView>(R.id.textViewDeskripsiDiskon)
        holder.nominalDiskon = view.findViewById<TextView>(R.id.textViewNominal)

        val diskon = arrayListDiskon[position]
        holder.no.text = (position+1).toString()
        holder.deskripsiDiskon.text = diskon.deskripsi_disc
        holder.nominalDiskon.text = diskon.nominal_disc.toString()

        return view
    }

    override fun getItem(position: Int): Any {
        return arrayListDiskon[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
       return arrayListDiskon.size
    }

    class Holder{
        lateinit var no : TextView
        lateinit var deskripsiDiskon : TextView
        lateinit var nominalDiskon : TextView
    }
}