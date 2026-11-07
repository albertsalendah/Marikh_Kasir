package com.marikhapp.marikhkasir.activities.transaksi.deskripsiPesanan

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.deskripsi.DeskripsiObject


class AdapterDeskripsiPesanan(private var daftardeskripsi: ArrayList<DeskripsiObject>, val context: Context,
                              val comefrom: String?,
                              val selectedDeskripsi:ArrayList<Int>) : BaseAdapter() {
    private var inflator : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var arrayDeskripsi = ArrayList<String>()
    val arrayIddeskripsi = ArrayList<Int>()
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val view : View = inflator.inflate(R.layout.adapter_deskripsi_pesanan,null)
        holder.checkBoxDeskripsiPesanan = view.findViewById(R.id.checkBoxDeskripsiPesanan) as CheckBox
        val deskripsi : DeskripsiObject = daftardeskripsi[position]

        holder.checkBoxDeskripsiPesanan.text = deskripsi.nama_deskripsi
        if (comefrom != "daftarproduk"){
            for (item in selectedDeskripsi){
                if (item == deskripsi.id_deskripsi){
                    holder.checkBoxDeskripsiPesanan.isChecked = true
                    arrayDeskripsi.add(deskripsi.nama_deskripsi)
                    arrayIddeskripsi.add(deskripsi.id_deskripsi)
                }
            }
        }

        holder.checkBoxDeskripsiPesanan.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                arrayDeskripsi.add(deskripsi.nama_deskripsi)
                arrayIddeskripsi.add(deskripsi.id_deskripsi)
            }else{
                arrayDeskripsi.remove(deskripsi.nama_deskripsi)
                arrayIddeskripsi.remove(deskripsi.id_deskripsi)
            }
        }

        return view
    }

    fun getDeskripsi() : ArrayList<String>{
        return arrayDeskripsi
    }

    fun getIdDeskripsi() : ArrayList<Int>{
        return arrayIddeskripsi
    }

    override fun getItem(position: Int): Any? {
      return daftardeskripsi[position]
    }

    override fun getItemId(position: Int): Long {
       return 0
    }

    override fun getCount(): Int {
       return daftardeskripsi.size
    }

    class Holder{
        lateinit var checkBoxDeskripsiPesanan : CheckBox
    }
}