package com.marikhapp.marikhkasir.activities.purchasing

import android.widget.TextView
import com.app.feng.fixtablelayout.inter.IDataAdapter

class FixTableAdapter(var titles: Array<String>, data: List<PurchasingObject>) : IDataAdapter {
    private var data: List<PurchasingObject>
    fun setData(data: List<PurchasingObject>) {
        this.data = data
    }

    override fun getTitleAt(pos: Int): String {
        return titles[pos]
    }

    override fun getTitleCount(): Int {
        return titles.size
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun convertData(position: Int, bindViews: List<TextView>) {
        val dataBean: PurchasingObject = data[position]
       // bindViews[0].setText((position+1).toString())
       // bindViews[0].setText(dataBean.nama_pembelian)
        bindViews[1].setText(dataBean.kategori_pembelian)
        bindViews[2].setText(dataBean.jumlah_pembelian.toString())
        bindViews[3].setText(dataBean.harga_pembelian.toString())
        bindViews[4].setText(dataBean.total_pembelian.toString())
    }

    override fun convertLeftData(position: Int, bindView: TextView) {
        bindView.setText(data[position].nama_pembelian)
    }

    init {
        this.data = data
    }
}
