package com.marikhapp.marikhkasir.activities.produk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.transaksi.DeskripsiPesanan.DeskripsiPesanan
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.ProdukModels.ProdukRespon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdukAdapter (private var daftarproduk : ArrayList<ProdukObject>, val context: Context, val id_kat_produk:String, val kategoriproduct:String,val stats: String):BaseAdapter(){

    private var inflator : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val view : View = inflator.inflate(R.layout.adapter_daftar_product,null)
        holder.textViewNamaProduk = view.findViewById(R.id.textViewNamaProduk) as TextView
        holder.textViewHargaProduk = view.findViewById(R.id.textViewHargaProduk) as TextView
        holder.imageViewGambarProduk = view.findViewById(R.id.imageViewGambarProduk) as ImageView
        holder.textViewIdProduk = view.findViewById(R.id.textViewIdProduk) as TextView

        val produk:ProdukObject = daftarproduk[position]
        //decode gambar
        if(produk.gbr_produk == null || produk.gbr_produk == ""){
            holder.imageViewGambarProduk.setImageResource(R.drawable.logomarikh)
        }else{
            val stringGambar = Base64.decode(produk.gbr_produk, Base64.DEFAULT)
            val decodedStringGambar = BitmapFactory.decodeByteArray(stringGambar, 0, stringGambar.size)
            holder.imageViewGambarProduk.setImageBitmap(decodedStringGambar)
            holder.imageViewGambarProduk.scaleType = ImageView.ScaleType.FIT_XY
        }

        holder.textViewNamaProduk.text = produk.nama_produk
        holder.textViewHargaProduk.text = "Rp."+produk.harga_produk
        holder.textViewIdProduk.text = produk.id_produk.toString()

        if(stats == "daftarproduk"){
            view.setOnClickListener {
                popup(view,context.applicationContext,produk.id_produk,id_kat_produk,kategoriproduct)
            }
        }else if(stats == "fragmentTransaksi"){
            view.setOnClickListener {
               deskripsipesanan(view)
            }
        }
        return view
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


    class Holder{
        lateinit var textViewNamaProduk : TextView
        lateinit var textViewHargaProduk : TextView
        lateinit var imageViewGambarProduk : ImageView
        lateinit var textViewIdProduk : TextView
    }

    private fun deskripsipesanan(view: View){
        val intent = Intent(this.context,DeskripsiPesanan::class.java)
        intent.putExtra("id_kat_produk",id_kat_produk)
        this.context.startActivity(intent)
    }

    private fun popup(view: View,context: Context,id_produk:Int,id_kat_produk:String,kategoriproduct:String){
        val menu = PopupMenu(context, view)
        menu.menu.add("Edit Product")
        menu.menu.add("Hapus Product")

        menu.show()

        menu.setOnMenuItemClickListener { item ->
            when{
                item.toString() == "Edit Product" -> {
                    val intent = Intent(view.context,TambahEditProduct::class.java)
                    intent.putExtra("kategoriproduct",kategoriproduct)
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    intent.putExtra("id_produk",id_produk.toString())
                    intent.putExtra("mode","edit")
                    view.context.startActivity(intent)
                }
                item.toString() == "Hapus Product" -> {
                    val hps = PopupMenu(context,view)
                    hps.menu.add("Ya")
                    hps.menu.add("Tidak")
                    hps.show()
                    hps.setOnMenuItemClickListener { item ->
                        when{
                            item.toString() == "Ya" ->{
                                Client.instance.hapusproduk(id_produk).enqueue(object : Callback<ProdukRespon>{
                                    override fun onFailure(call: Call<ProdukRespon>, t: Throwable) {
                                        Toast.makeText(context,"Terjadi Kesalahan",Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<ProdukRespon>, response: Response<ProdukRespon>) {
                                        Toast.makeText(context,"Produk Berhasil Dihapus",Toast.LENGTH_SHORT).show()
                                        val intent = Intent(view.context,DaftarProduct::class.java)
                                        intent.putExtra("kategoriproduct",kategoriproduct)
                                        intent.putExtra("id_kat_produk",id_kat_produk)
                                        view.context.startActivity(intent)
                                    }
                                })
                            }
                            item.toString() == "Tidak" ->{
                                hps.dismiss()
                            }
                        }
                        true
                    }
                }
            }
            true
        }
    }

}