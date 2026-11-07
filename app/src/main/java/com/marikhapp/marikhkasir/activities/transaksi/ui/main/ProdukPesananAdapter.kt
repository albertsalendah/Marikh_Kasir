package com.marikhapp.marikhkasir.activities.transaksi.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.produk.ProdukObject

class ProdukPesananAdapter (private var daftarproduk : ArrayList<ProdukObject>, val context: Context) : BaseAdapter() {

    //private var inflator: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view : View?= convertView
        val holder = Holder()
            view = LayoutInflater.from(context).inflate(R.layout.adapter_daftar_product, parent,false)
            holder.textViewNamaProduk = view.findViewById(R.id.textViewNamaProduk) as TextView
            holder.textViewHargaProduk = view.findViewById(R.id.textViewHargaProduk) as TextView
            holder.imageViewGambarProduk = view.findViewById(R.id.imageViewGambarProduk) as ImageView
            holder.textViewIdProduk = view.findViewById(R.id.textViewIdProduk) as TextView

        if (daftarproduk.isNotEmpty()){
            val produk: ProdukObject = daftarproduk[position]
            //decode gambar
            if (produk.gbr_produk == null || produk.gbr_produk == "") {
                val icon = BitmapFactory.decodeResource(context.resources, R.drawable.logomarikh)
                val compresdBitmap = getResizedBitmap(icon, 150)
                holder.imageViewGambarProduk.setImageBitmap(compresdBitmap)
                //holder.imageViewGambarProduk.setImageResource(R.drawable.logomarikh)
            } else {
                val stringGambar = Base64.decode(produk.gbr_produk, Base64.DEFAULT)
                val decodedStringGambar = BitmapFactory.decodeByteArray(stringGambar, 0, stringGambar.size)
                //val compresdBitmap = getResizedBitmap(decodedStringGambar, 100)
                holder.imageViewGambarProduk.setImageBitmap(decodedStringGambar)
                holder.imageViewGambarProduk.scaleType = ImageView.ScaleType.FIT_XY
            }

            holder.textViewIdProduk.text = produk.id_produk.toString()
            holder.textViewNamaProduk.text = produk.nama_produk
            holder.textViewHargaProduk.text = "Rp." + produk.harga_produk
        }else{
            Toast.makeText(context,"Gagal Memuat Data (Tekan Refresh)",Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

        return view
    }
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return daftarproduk.size
    }


    class Holder {
        lateinit var textViewNamaProduk: TextView
        lateinit var textViewHargaProduk: TextView
        lateinit var imageViewGambarProduk: ImageView
        lateinit var textViewIdProduk: TextView
    }
}