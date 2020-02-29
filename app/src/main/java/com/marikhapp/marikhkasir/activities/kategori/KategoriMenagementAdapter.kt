package com.marikhapp.marikhkasir.activities.kategori

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.deskripsi.DaftarDeskripsi
import com.marikhapp.marikhkasir.activities.produk.DaftarProduct
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("NAME_SHADOWING")
class KategoriMenagementAdapter(private var listKategori: ArrayList<KategoriObject>,val context: Context) : BaseAdapter() {
    private var inflator : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val kategori : KategoriObject = listKategori[position]
        val view : View = inflator.inflate(R.layout.adapter_kategori_menagement,null)

        holder.textViewNamaKategori = view.findViewById(R.id.textViewNamaKategori) as TextView
        holder.textViewNamaKategori.text = kategori.nama_kat_produk

        holder.cardKategori = view.findViewById(R.id.cardKategori) as CardView

        view.setOnClickListener {
            view.z =1f
            popupmenu(view,context.applicationContext,kategori.nama_kat_produk,kategori.id_kat_produk.toString())
            return@setOnClickListener
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
        return listKategori.size
    }
    class Holder{
        lateinit var textViewNamaKategori : TextView
        lateinit var cardKategori : CardView
    }

    private fun popupmenu(view: View,context: Context,list_nama_kat_produk:String,list_id_kat_produk:String){
        val menu = PopupMenu(context, view)
        //menu.inflate(R.menu.dropdownmenu)

        menu.menu.add("Daftar Product")
        menu.menu.add("Daftar Deskripsi")
        menu.menu.add("Edit Kategori")
        //menu.menu.add("Hapus Kategori")
        menu.show()
        menu.setOnMenuItemClickListener { item ->
            when {
                item.toString() == "Daftar Product" -> {
                    val intent = Intent(view.context, DaftarProduct::class.java)
                    intent.putExtra("kategoriproduct",list_nama_kat_produk)
                    intent.putExtra("id_kat_produk",list_id_kat_produk)
                    view.context.startActivity(intent)
                }
                item.toString() == "Daftar Deskripsi" -> {
                    val intent = Intent(view.context,DaftarDeskripsi::class.java)
                        intent.putExtra("id_kat_produk",list_id_kat_produk)
                        view.context.startActivity(intent)
                }
                item.toString() == "Edit Kategori" -> {
                    val intent = Intent(view.context, TambahEditKategori::class.java)
                    intent.putExtra("nama_kat_produk",list_nama_kat_produk)
                    intent.putExtra("id_kat_produk",list_id_kat_produk)
                    intent.putExtra("mode","edit")
                    view.context.startActivity(intent)
                }
                item.toString() == "Hapus Kategori"->{
                    val delete = PopupMenu(context,view)
                    delete.menu.add("Yes")
                    delete.menu.add("No")
                    delete.show()
                    delete.setOnMenuItemClickListener{item ->
                        when{
                            item.toString() == "Yes" ->{
                                Client.instance.hapuskategori(list_nama_kat_produk).enqueue(object : Callback<DefaultResponse>{
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                        Toast.makeText(context,response.body()!!.message,Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                            item.toString() == "No"->{
                                delete.dismiss()
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