package com.marikhapp.marikhkasir.activities.methodbayar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.marikhapp.marikhkasir.R


class MethodBayarAdapter(val arrayMethodBayar : ArrayList<MethodBayarObject>,val context : Context) : BaseAdapter()  {
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view : View = LayoutInflater.from(context).inflate(R.layout.adapter_method_bayar,parent,false)
        val holder = Holder()
        holder.no = view.findViewById<TextView>(R.id.no_method_bayar)
        holder.nama_method = view.findViewById<TextView>(R.id.nama_method_bayar)
        holder.reguler = view.findViewById<TextView>(R.id.reguler_method_bayar)
        holder.olfood = view.findViewById(R.id.olfood_method_bayar)

        val methodbayar = arrayMethodBayar[position]
        holder.no.text = (position + 1).toString()
        holder.nama_method.text = methodbayar.nama_method
        if (methodbayar.reguler == "1"){
            holder.reguler.text = "Yes"
        }else{
            holder.reguler.text = "No"
        }
        if (methodbayar.olfood == "1"){
            holder.olfood.text = "Yes"
        }else{
            holder.olfood.text = "No"
        }

        return view
    }

    override fun getItem(position: Int): Any {
       return arrayMethodBayar[position]
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
       return arrayMethodBayar.size
    }

    class Holder(){
        lateinit var no : TextView
        lateinit var nama_method : TextView
        lateinit var reguler : TextView
        lateinit var olfood : TextView
    }
}