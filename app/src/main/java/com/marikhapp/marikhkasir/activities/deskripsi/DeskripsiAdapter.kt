package com.marikhapp.marikhkasir.activities.deskripsi

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
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NAME_SHADOWING")
class DeskripsiAdapter(private var daftardeskripsi : ArrayList<DeskripsiObject>, val context: Context) : BaseAdapter() {

    private var inflator : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val view : View = inflator.inflate(R.layout.adapter_daftar_deskripsi,null)

        holder.textViewNamaDeskripsi = view.findViewById(R.id.textViewNamaDeskripsi) as TextView

        val deskripsi : DeskripsiObject = daftardeskripsi[position]
        holder.textViewNamaDeskripsi.text = deskripsi.nama_deskripsi

        view.setOnClickListener {
            popup(view,context.applicationContext,deskripsi.id_deskripsi.toString(),deskripsi.nama_deskripsi,deskripsi.id_kat_produk.toString())
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
        return daftardeskripsi.size
    }

    class Holder{
        lateinit var textViewNamaDeskripsi : TextView
    }

    private fun popup(view:View,context: Context,id_deskripsi : String,nama_deskripsi:String,id_kat_produk:String){
        val menu = PopupMenu(context,view)
        menu.menu.add("Edit Deskripsi")
        menu.menu.add("Hapus Deskripsi")
        menu.show()
        menu.setOnMenuItemClickListener { item ->
            when{
                item.toString() == "Edit Deskripsi"->{
                    val intent = Intent(context,TambahEditDeskripsi::class.java)
                    intent.putExtra("mode","edit")
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    intent.putExtra("id_deskripsi",id_deskripsi)
                    intent.putExtra("nama_deskripsi",nama_deskripsi)
                    view.context.startActivity(intent)
                }
                item.toString() == "Hapus Deskripsi"->{
                    val hps = PopupMenu(context,view)
                    hps.menu.add("Ya")
                    hps.menu.add("Tidak")
                    hps.show()
                    hps.setOnMenuItemClickListener { item ->
                        when{
                            item.toString() == "Ya"->{
                                hapusDeskripsi(view,context,id_deskripsi,id_kat_produk)
                            }
                            item.toString() == "Tidak"->{

                            }
                        }
                        true
                    }
                }
            }
            true
        }
    }

    private fun hapusDeskripsi(view: View,context: Context,id_deskripsi: String,id_kat_produk: String){
        Client.instance.hapusdeskripsiproduk(id_deskripsi.toInt()).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context,"Terjadi Masalah Saat Masalah Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(context,"Deskripsi Berhasil Dihapus",Toast.LENGTH_SHORT).show()
                    val intent = Intent(context,DaftarDeskripsi::class.java)
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    view.context.startActivity(intent)
                }else{
                    Toast.makeText(context,"Gagal Menghapus Deskrisi",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}