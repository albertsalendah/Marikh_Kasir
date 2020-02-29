package com.marikhapp.marikhkasir.activities.transaksi.DeskripsiPesanan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.deskripsi.DeskripsiObject

class AdapterDeskripsiPesanan(private var daftardeskripsi : ArrayList<DeskripsiObject>, val context: Context) : BaseAdapter() {
    private var inflator : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val view : View = inflator.inflate(R.layout.adapter_deskripsi_pesanan,null)
        holder.checkBoxDeskripsiPesanan = view.findViewById(R.id.checkBoxDeskripsiPesanan) as CheckBox

        val deskripsi : DeskripsiObject = daftardeskripsi[position]
        holder.checkBoxDeskripsiPesanan.text = deskripsi.nama_deskripsi
        return view
    }

    override fun getItem(position: Int): Any? {
      return null
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