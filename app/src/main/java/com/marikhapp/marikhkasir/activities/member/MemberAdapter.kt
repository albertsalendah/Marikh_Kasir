package com.marikhapp.marikhkasir.activities.member

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.marikhapp.marikhkasir.R

class MemberAdapter(val arrayMember :  ArrayList<MemberObject>,val context:Context) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_daftar_member,parent,false)
        val holder = Holder()
        holder.namapelanggan = view.findViewById<TextView>(R.id.textViewNamaPelanggan)
        holder.tglLahirPelanggan = view.findViewById<TextView>(R.id.textViewTglLahirPelanggan)
        holder.nohpPelanggan = view.findViewById<TextView>(R.id.textViewNoHpPelanggan)
        holder.emailPelanggan = view.findViewById<TextView>(R.id.textViewEmailPelanggan)
        holder.kategoripelanggan = view.findViewById<TextView>(R.id.textViewKatPelanggan)

        val member = arrayMember[position]

        holder.namapelanggan.text = member.nama_pelanggan.toString()
        holder.tglLahirPelanggan.text = "${member.tgl_lahir_pelanggan}"
        holder.nohpPelanggan.text = member.hp_pelanggan.toString()
        holder.emailPelanggan.text = "${member.email_pelanggan}"
        holder.kategoripelanggan.text = member.namaKategori.toString()

        return view
    }

    override fun getItem(position: Int): MemberObject {
        return arrayMember[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayMember.size
    }
    class Holder{
        lateinit var namapelanggan : TextView
        lateinit var tglLahirPelanggan : TextView
        lateinit var nohpPelanggan : TextView
        lateinit var emailPelanggan : TextView
        lateinit var kategoripelanggan : TextView
    }
    private fun popupmenu(view: View,context: Context){
        val menu = PopupMenu(context, view)
        //menu.inflate(R.menu.dropdownmenu)
        menu.menu.add("Daftar Product")
        menu.menu.add("Daftar Deskripsi")
        menu.menu.add("Edit Kategori")
        //menu.menu.add("Hapus Kategori")
        menu.show()
    }
}