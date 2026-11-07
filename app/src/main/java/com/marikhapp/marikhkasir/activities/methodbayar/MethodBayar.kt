package com.marikhapp.marikhkasir.activities.methodbayar

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
import com.marikhapp.marikhkasir.models.MethodBayarModels.MethodBayarModel
import com.marikhapp.marikhkasir.models.MethodBayarModels.MethodBayarRespon
import kotlinx.android.synthetic.main.activity_method_bayar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MethodBayar : AppCompatActivity() {
    lateinit var adapter : MethodBayarAdapter
    var arraylistMethodBayar = ArrayList<MethodBayarObject>()
    val context = this@MethodBayar
    var mode = ""
    var id_method : String= ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_method_bayar)

        textViewpayment.text = "< Payment Method"
        textViewpayment.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        adapter = MethodBayarAdapter(arraylistMethodBayar,context)
        listMethodBayar.adapter = adapter
        adapter.notifyDataSetChanged()

        daftarMethodBayar()

        constraitMethodBayar.setOnClickListener {
            constraintLayout7.visibility = View.GONE
            mode = ""
            id_method = ""
            editTextInMethodBayar.error = null
        }

        textViewAddMethodBayar.text = "+ Method Bayar"
        textViewAddMethodBayar.setOnClickListener {
            constraintLayout7.visibility = View.VISIBLE
            textViewMode_Method_Bayar.text = "Tambah Method Bayar"
            mode = "tambah"
            id_method = ""
            editTextInMethodBayar.error = null
            editTextInMethodBayar.setText("")
            checkBoxReguler.isChecked = false
            checkBoxOlFood.isChecked = false
        }

        listMethodBayar.setOnItemClickListener { parent, view, position, id ->
            constraintLayout7.visibility = View.VISIBLE
            textViewMode_Method_Bayar.text = "Edit Method Bayar"
            editTextInMethodBayar.error = null
            mode = "edit"
            id_method = arraylistMethodBayar[position].id_method_bayar
            editTextInMethodBayar.setText(arraylistMethodBayar[position].nama_method)
            checkBoxReguler.isChecked = arraylistMethodBayar[position].reguler == "1"
            checkBoxOlFood.isChecked = arraylistMethodBayar[position].olfood == "1"
        }
        buttonSimpanMethodBayar.setOnClickListener {
            val nama_method = editTextInMethodBayar.text.toString().toUpperCase()
            var reguler = ""
            var olfood = ""
            if (nama_method.isEmpty()){
                editTextInMethodBayar.error = "Nama Method Tidak Boleh Kosong"
                editTextInMethodBayar.requestFocus()
            }
            if (checkBoxReguler.isChecked){
                reguler = "1"
            }else{
                reguler = "0"
            }
            if(checkBoxOlFood.isChecked){
                olfood = "1"
            }else{
                olfood = "0"
            }
            if (mode == "tambah"){
                tambahMethodBayar(nama_method,reguler,olfood)
            }else if(mode == "edit"){
                editMethodBayar(id_method,nama_method,reguler,olfood)
            }
        }
    }

    private fun daftarMethodBayar(){
        Client.instance.daftarmethodbayar().enqueue(object : Callback<List<MethodBayarModel>>{
            override fun onFailure(call: Call<List<MethodBayarModel>>, t: Throwable) {
                Toast.makeText(context,t.message,Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<MethodBayarModel>>, response: Response<List<MethodBayarModel>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        for (item in data){
                            arraylistMethodBayar.add(MethodBayarObject(item.id_method_bayar,item.nama_method.toString(),item.reguler,item.olfood))
                        }
                        adapter.notifyDataSetChanged()
                    }
                }else{
                    Toast.makeText(context,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun tambahMethodBayar(nama_method:String,reguler:String,olfood:String){
        Client.instance.insertmethodbayar(nama_method,reguler,olfood).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    id_method = ""
                    mode = ""
                    arraylistMethodBayar.clear()
                    daftarMethodBayar()
                    editTextInMethodBayar.error = null
                    editTextInMethodBayar.setText("")
                    checkBoxReguler.isChecked = false
                    checkBoxOlFood.isChecked = false
                    constraintLayout7.visibility = View.GONE
                    Toast.makeText(context,"Data Berhasil Ditambahkan",Toast.LENGTH_LONG).show()
                }else{
                    id_method = ""
                    mode = ""
                    arraylistMethodBayar.clear()
                    daftarMethodBayar()
                    editTextInMethodBayar.error = null
                    editTextInMethodBayar.setText("")
                    checkBoxReguler.isChecked = false
                    checkBoxOlFood.isChecked = false
                    constraintLayout7.visibility = View.GONE
                    Toast.makeText(context,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun editMethodBayar(id_method_bayar:String,nama_method:String,reguler:String,olfood:String){
        Client.instance.updateMethodBayar(id_method_bayar,nama_method,reguler,olfood).enqueue(object : Callback<MethodBayarRespon>{
            override fun onFailure(call: Call<MethodBayarRespon>, t: Throwable) {
                Toast.makeText(context,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MethodBayarRespon>, response: Response<MethodBayarRespon>) {
                if (response.isSuccessful){
                    id_method = ""
                    mode = ""
                    arraylistMethodBayar.clear()
                    daftarMethodBayar()
                    editTextInMethodBayar.error = null
                    editTextInMethodBayar.setText("")
                    checkBoxReguler.isChecked = false
                    checkBoxOlFood.isChecked = false
                    constraintLayout7.visibility = View.GONE
                    Toast.makeText(context,"Data Berhasil Diupdate",Toast.LENGTH_LONG).show()
                }else{
                    id_method = ""
                    mode = ""
                    arraylistMethodBayar.clear()
                    daftarMethodBayar()
                    editTextInMethodBayar.error = null
                    editTextInMethodBayar.setText("")
                    checkBoxReguler.isChecked = false
                    checkBoxOlFood.isChecked = false
                    constraintLayout7.visibility = View.GONE
                    Toast.makeText(context,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
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
