package com.marikhapp.marikhkasir.activities.diskon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.DiskonModels.Diskon
import com.marikhapp.marikhkasir.models.DiskonModels.DiskonResponse
import kotlinx.android.synthetic.main.activity_diskon__management.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Diskon_Management : AppCompatActivity() {
    lateinit var adapter : Diskon_Management_Adapter
    var arrayListDiskon = ArrayList<DiskonObject>()
    val context = this@Diskon_Management
    var update_mode = false
    var id_diskon = "0"
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diskon__management)

        textViewDiskon.text = "< Diskon Management"
        textViewDiskon.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        textViewAddEditDiskon.text = "Tambah Diskon"
        adapter = Diskon_Management_Adapter(arrayListDiskon,context)
        listviewDiskon.adapter = adapter
        listDiskon()
        constraitDiskonManagement.setOnClickListener {
            arrayListDiskon.clear()
            listDiskon()
            update_mode = false
            id_diskon = ""
            textViewAddEditDiskon.text = "Tambah Diskon"
            editTextDiskonM.setText("")
        }
        listviewDiskon.setOnItemClickListener { parent, view, position, id ->
            textViewAddEditDiskon.text = "Update Diskon"
            editTextDeskripsiDiskon.setText(arrayListDiskon[position].deskripsi_disc)
            editTextDiskonM.setText(arrayListDiskon[position].nominal_disc.toString())
            id_diskon = arrayListDiskon[position].id_disc
            update_mode = true
        }
            buttonSimpanDiskon.setOnClickListener {
                val deskripsi = editTextDeskripsiDiskon.text.toString().toUpperCase()
                val nominal = editTextDiskonM.text.toString()
                if (deskripsi.isEmpty()){
                    editTextDeskripsiDiskon.error = "Isi Deskripsi Nominal"
                    editTextDeskripsiDiskon.requestFocus()
                }
                if (nominal.isEmpty() || nominal == ""){
                    editTextDiskonM.error = "Isi Kolom Nominal"
                    editTextDiskonM.requestFocus()
                }
                if (!update_mode){
                    if (nominal != ""){
                        tambahDiskon(deskripsi,nominal.toInt())
                    }else{
                        editTextDiskonM.error = "Isi Kolom Nominal"
                        editTextDiskonM.requestFocus()
                    }
                }else{
                    if (nominal != ""){
                        updatediskon(id_diskon,deskripsi,nominal.toInt())
                    }else{
                        editTextDiskonM.error = "Isi Kolom Nominal"
                        editTextDiskonM.requestFocus()
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun listDiskon(){
        update_mode = false
        progressBarDiskonMenagement.visibility = View.VISIBLE
        Client.instance.daftardiskon().enqueue(object : Callback<List<Diskon>>{
            override fun onFailure(call: Call<List<Diskon>>, t: Throwable) {
                progressBarDiskonMenagement.visibility = View.GONE
                Toast.makeText(this@Diskon_Management,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Diskon>>, response: Response<List<Diskon>>) {
                if (response.isSuccessful){
                    progressBarDiskonMenagement.visibility = View.GONE
                    val data = response.body()
                    if (data!!.isNotEmpty()){
                        for (item in data){
                            arrayListDiskon.add(DiskonObject(item.id_disc,item.deskripsi_disc,item.nominal_disc))
                        }
                        adapter.notifyDataSetChanged()
                    }else{
                        Toast.makeText(this@Diskon_Management,"Daftar Diskon Kosong",Toast.LENGTH_LONG).show()
                    }
                    adapter.notifyDataSetChanged()
                }else{
                    progressBarDiskonMenagement.visibility = View.GONE
                    Toast.makeText(this@Diskon_Management,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun tambahDiskon(deskripsi : String,nominal : Int){
        progressBarDiskonMenagement.visibility = View.VISIBLE
        Client.instance.insertDiskon(deskripsi,nominal).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                progressBarDiskonMenagement.visibility = View.GONE
                Toast.makeText(this@Diskon_Management,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
               if (response.isSuccessful){
                   progressBarDiskonMenagement.visibility = View.GONE
                   Toast.makeText(this@Diskon_Management,response.body()?.message,Toast.LENGTH_LONG).show()
                   editTextDiskonM.setText("")
                   editTextDeskripsiDiskon.setText("")
                   arrayListDiskon.clear()
                   listDiskon()
                   editTextDiskonM.error = null
               }else{
                   progressBarDiskonMenagement.visibility = View.GONE
                   editTextDiskonM.error = null
                   Toast.makeText(this@Diskon_Management,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
               }
            }
        })
    }

    private fun updatediskon(id:String,deskripsi:String,nominal: Int){
        progressBarDiskonMenagement.visibility = View.VISIBLE
        Client.instance.updateDiskon(id,deskripsi,nominal).enqueue(object : Callback<DiskonResponse>{
            override fun onFailure(call: Call<DiskonResponse>, t: Throwable) {
                progressBarDiskonMenagement.visibility = View.GONE
                Toast.makeText(this@Diskon_Management,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                id_diskon = ""
                update_mode = false
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DiskonResponse>, response: Response<DiskonResponse>) {
                if (response.isSuccessful){
                    progressBarDiskonMenagement.visibility = View.GONE
                    Toast.makeText(this@Diskon_Management,response.body()?.message,Toast.LENGTH_LONG).show()
                    editTextDiskonM.setText("")
                    editTextDeskripsiDiskon.setText("")
                    id_diskon = ""
                    update_mode = false
                    arrayListDiskon.clear()
                    listDiskon()
                    editTextDiskonM.error = null
                    textViewAddEditDiskon.text = "Tambah Diskon"
                }else{
                    progressBarDiskonMenagement.visibility = View.GONE
                    id_diskon = ""
                    update_mode = false
                    editTextDiskonM.error = null
                    textViewAddEditDiskon.text = "Tambah Diskon"
                    Toast.makeText(this@Diskon_Management,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this,Main_Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        super.onBackPressed()
    }

}
