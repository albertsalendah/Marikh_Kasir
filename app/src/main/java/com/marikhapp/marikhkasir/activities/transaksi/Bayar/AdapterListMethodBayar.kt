package com.marikhapp.marikhkasir.activities.transaksi.Bayar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.Toast
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.methodbayar.MethodBayarObject

class AdapterListMethodBayar (var arrayListMethodBayar : ArrayList<MethodBayarObject>,val context:Context) : BaseAdapter() {
    private var selectedPosition = -1
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_bayar,parent,false)
        val holder = Holder()

        holder.nama_method = view.findViewById(R.id.radioButtonMethodBayar)

        val method_bayar = arrayListMethodBayar[position]
        holder.nama_method.text = method_bayar.nama_method

        //check the radio button if both position and selectedPosition matches
        holder.nama_method.isChecked = position == selectedPosition

        holder.nama_method.tag = position
        holder.nama_method.setOnClickListener(View.OnClickListener {
                v -> itemCheckChanged(v)
        })

        return view
    }

    private fun itemCheckChanged(v: View) {
        selectedPosition = v.tag as Int
        notifyDataSetChanged()
    }

    fun getSelectedItem(): String? {
        if (selectedPosition != -1) {
            //Toast.makeText(context, "Selected Item : " + arrayListMethodBayar.get(selectedPosition), Toast.LENGTH_SHORT).show()
            return arrayListMethodBayar.get(selectedPosition).nama_method
        }
        return ""
    }

    override fun getItem(position: Int): MethodBayarObject {
        return arrayListMethodBayar[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayListMethodBayar.size
    }

    class Holder {
        lateinit var nama_method : RadioButton
    }
}