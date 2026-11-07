package com.marikhapp.marikhkasir.activities.produk

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.ProdukModels.Produk
import com.marikhapp.marikhkasir.models.ProdukModels.ProdukRespon
import kotlinx.android.synthetic.main.activity_tambah_edit_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TambahEditProduct : AppCompatActivity(){

    private val GALLERY = 1
    private  var tgl_awal_diskon:String? = null
    private var tgl_akhir_diskon:String? = null
    private  var tgl_awal_diskon_Olfood:String? = null
    private var tgl_akhir_diskon_Olfood:String? = null
    var old_gbr:String = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_edit_product)
        val kategoriproduct = intent.getStringExtra("kategoriproduct")
        val id_kat_produk = intent.getStringExtra("id_kat_produk")
        val id_produk = intent.getStringExtra("id_produk")
        val mode = intent.getStringExtra("mode")
        requestPermissions()

        editTextKategori.text = kategoriproduct

        productMode.setOnClickListener {
            val view = View(this)
            val intent = Intent(view.context,DaftarProduct::class.java)
            intent.putExtra("kategoriproduct",kategoriproduct)
            intent.putExtra("id_kat_produk",id_kat_produk)
            view.context.startActivity(intent)
        }
        if(mode == "new") {
            productMode.text = "< $mode Product"
            buttonTambahEditProduct.text = "Tambah Produk"

        }else if(mode == "edit"){
            productMode.text = "< $mode Product"
            buttonTambahEditProduct.text = "Edit Produk"
            progressBarAddProduk.visibility = View.VISIBLE
            getproduk(id_produk!!)
        }
            previewProduct.setOnClickListener {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY)
            }

            editTextDurasiAwalDiskonProduct.setOnClickListener {
                showDatePickerDialog(editTextDurasiAwalDiskonProduct)
            }

            editTextDurasiAkhirDiskonProduct.setOnClickListener {
                showDatePickerDialog(editTextDurasiAkhirDiskonProduct)
            }

        textViewDurasi_Awal_Diskon_Olfood.setOnClickListener {
            showDatePickerDialog(textViewDurasi_Awal_Diskon_Olfood)
        }

        textViewDurasi_Akhir_Diskon_Olfood.setOnClickListener {
            showDatePickerDialog(textViewDurasi_Akhir_Diskon_Olfood)
        }

        editTextDiskonOlfood.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                checkDiskonOlfood()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checkDiskonOlfood()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkDiskonOlfood()
            }
        })

            editTextDiskonProduct.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    checkDiskon()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                   checkDiskon()
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    checkDiskon()
                }
            })

            buttonTambahEditProduct.setOnClickListener {
                progressBarAddProduk.visibility = View.VISIBLE
                val namaproduk = editTextProduct.text.toString().toUpperCase(Locale.getDefault()).trim()
                val hargaproduct = editTextHargaProduct.text.toString()
                val jumlahproduct = editTextJumlahProduct.text.toString()
                var diskon_produk : Int? = null
                var diskon_olfood : Int? = null
                if (namaproduk.isEmpty()){
                    editTextProduct.error = "Nama Produk Kosong"
                    editTextProduct.requestFocus()
                    return@setOnClickListener
                }

                if (hargaproduct.isEmpty()){
                    editTextHargaProduct.error = "Harga Produk Kosong"
                    editTextHargaProduct.requestFocus()
                    return@setOnClickListener
                }

                if (jumlahproduct.isEmpty()){
                    editTextJumlahProduct.error = "Jumlah Produk Kosong"
                    editTextJumlahProduct.requestFocus()
                    return@setOnClickListener
                }

                val tgl1 = editTextDurasiAwalDiskonProduct.text.substring(3,5)
                val bln1 = editTextDurasiAwalDiskonProduct.text.substring(0,2)
                val thn1 = editTextDurasiAwalDiskonProduct.text.substring(6,10)

                val tgl2 = editTextDurasiAkhirDiskonProduct.text.substring(3,5)
                val bln2 = editTextDurasiAkhirDiskonProduct.text.substring(0,2)
                val thn2 = editTextDurasiAkhirDiskonProduct.text.substring(6,10)

                val tgl1_Olfood = textViewDurasi_Awal_Diskon_Olfood.text.substring(3,5)
                val bln1_Olfood = textViewDurasi_Awal_Diskon_Olfood.text.substring(0,2)
                val thn1_Olfood = textViewDurasi_Awal_Diskon_Olfood.text.substring(6,10)

                val tgl2_Olfood = textViewDurasi_Akhir_Diskon_Olfood.text.substring(3,5)
                val bln2_Olfood = textViewDurasi_Akhir_Diskon_Olfood.text.substring(0,2)
                val thn2_Olfood = textViewDurasi_Akhir_Diskon_Olfood.text.substring(6,10)

                if(mode == "new"){
                    if(editTextDiskonProduct.text.isNullOrEmpty() && editTextDiskonOlfood.text.isNullOrEmpty()){
                        diskon_produk = 0
                        tgl_awal_diskon = ""
                        tgl_akhir_diskon = ""
                        diskon_olfood = 0
                        tgl_awal_diskon_Olfood = ""
                        tgl_akhir_diskon_Olfood = ""
                    } else if (editTextDiskonOlfood.text.isNullOrEmpty()){
                        diskon_olfood = 0
                        tgl_awal_diskon_Olfood = ""
                        tgl_akhir_diskon_Olfood = ""
                        diskon_produk = editTextDiskonProduct.text.toString().toInt()
                        tgl_awal_diskon = "$thn1$bln1$tgl1"
                        tgl_akhir_diskon = "$thn2$bln2$tgl2"
                    }else if(editTextDiskonProduct.text.isNullOrEmpty()){
                        diskon_produk = 0
                        tgl_awal_diskon = ""
                        tgl_akhir_diskon = ""
                        diskon_olfood = editTextDiskonOlfood.text.toString().toInt()
                        tgl_awal_diskon_Olfood = "$thn1_Olfood$bln1_Olfood$tgl1_Olfood"
                        tgl_akhir_diskon_Olfood = "$thn2_Olfood$bln2_Olfood$tgl2_Olfood"
                    }else{
                        diskon_produk = editTextDiskonProduct.text.toString().toInt()
                        tgl_awal_diskon = "$thn1$bln1$tgl1"
                        tgl_akhir_diskon = "$thn2$bln2$tgl2"
                        diskon_olfood = editTextDiskonOlfood.text.toString().toInt()
                        tgl_awal_diskon_Olfood = "$thn1_Olfood$bln1_Olfood$tgl1_Olfood"
                        tgl_akhir_diskon_Olfood = "$thn2_Olfood$bln2_Olfood$tgl2_Olfood"
                    }
                    tambahProduk(namaproduk,hargaproduct,jumlahproduct,diskon_produk,id_kat_produk!!,tgl_awal_diskon,tgl_akhir_diskon,diskon_olfood,tgl_awal_diskon_Olfood,tgl_akhir_diskon_Olfood)
                }else if(mode == "edit"){
                    if(editTextDiskonProduct.text.isNullOrEmpty() && editTextDiskonOlfood.text.isNullOrEmpty()){
                        diskon_produk = 0
                        tgl_awal_diskon = ""
                        tgl_akhir_diskon = ""
                        diskon_olfood = 0
                        tgl_awal_diskon_Olfood = ""
                        tgl_akhir_diskon_Olfood = ""
                    } else if (editTextDiskonOlfood.text.isNullOrEmpty()){
                        diskon_olfood = 0
                        tgl_awal_diskon_Olfood = ""
                        tgl_akhir_diskon_Olfood = ""
                        diskon_produk = editTextDiskonProduct.text.toString().toInt()
                        tgl_awal_diskon = "$thn1$bln1$tgl1"
                        tgl_akhir_diskon = "$thn2$bln2$tgl2"
                    }else if(editTextDiskonProduct.text.isNullOrEmpty()){
                        diskon_produk = 0
                        tgl_awal_diskon = ""
                        tgl_akhir_diskon = ""
                        diskon_olfood = editTextDiskonOlfood.text.toString().toInt()
                        tgl_awal_diskon_Olfood = "$thn1_Olfood$bln1_Olfood$tgl1_Olfood"
                        tgl_akhir_diskon_Olfood = "$thn2_Olfood$bln2_Olfood$tgl2_Olfood"
                    }else{
                        diskon_produk = editTextDiskonProduct.text.toString().toInt()
                        tgl_awal_diskon = "$thn1$bln1$tgl1"
                        tgl_akhir_diskon = "$thn2$bln2$tgl2"
                        diskon_olfood = editTextDiskonOlfood.text.toString().toInt()
                        tgl_awal_diskon_Olfood = "$thn1_Olfood$bln1_Olfood$tgl1_Olfood"
                        tgl_akhir_diskon_Olfood = "$thn2_Olfood$bln2_Olfood$tgl2_Olfood"
                    }
                    updateproduk(id_produk!!,namaproduk,hargaproduct,jumlahproduct,diskon_produk,id_kat_produk!!,tgl_awal_diskon,tgl_akhir_diskon,diskon_olfood,tgl_awal_diskon_Olfood,tgl_akhir_diskon_Olfood)
                }
            }

    }

    private fun checkDiskon(){
        if(editTextDiskonProduct.text.isNullOrEmpty()){
            textView14.visibility = View.GONE
            textView15.visibility = View.GONE
            editTextDurasiAwalDiskonProduct.visibility = View.GONE
            editTextDurasiAkhirDiskonProduct.visibility = View.GONE
        }else{
            textView14.visibility = View.VISIBLE
            textView15.visibility = View.VISIBLE
            editTextDurasiAwalDiskonProduct.visibility = View.VISIBLE
            editTextDurasiAkhirDiskonProduct.visibility = View.VISIBLE
        }
    }

    private fun checkDiskonOlfood(){
        if(editTextDiskonOlfood.text.isNullOrEmpty()){
            textView16.visibility = View.GONE
            textView37.visibility = View.GONE
            textViewDurasi_Awal_Diskon_Olfood.visibility = View.GONE
            textViewDurasi_Akhir_Diskon_Olfood.visibility = View.GONE
        }else{
            textView16.visibility = View.VISIBLE
            textView37.visibility = View.VISIBLE
            textViewDurasi_Awal_Diskon_Olfood.visibility = View.VISIBLE
            textViewDurasi_Akhir_Diskon_Olfood.visibility = View.VISIBLE
        }
    }


    private var selectedPhoto: Bitmap? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }

        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    selectedPhoto = getResizedBitmap(bitmap, 100)
                    previewProduct.setImageBitmap(selectedPhoto)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    private fun tambahProduk(namaproduk:String,hargaproduct:String,jumlahproduct:String,diskon_produk:Int?,id_kat_produk:String,tgl_awal_diskon:String?,tgl_akhir_diskon:String?,diskon_olfood:Int?,tgl_awal_diskon_olfood : String?,tgl_akhir_diskon_olfood:String?) {
        var gambar:String? = null

        gambar = if(selectedPhoto == null){
           ""
        } else{
            val byteArrayOutputStream = ByteArrayOutputStream()
            selectedPhoto!!.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
            val encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
            encodedImage
        }


        Client.instance.tambahproduk(namaproduk, hargaproduct.toInt(), jumlahproduct.toInt(), diskon_produk, id_kat_produk.toInt(), tgl_awal_diskon, tgl_akhir_diskon, diskon_olfood, tgl_awal_diskon_olfood, tgl_akhir_diskon_olfood, gambar).enqueue(object : Callback<ProdukRespon> {
            override fun onFailure(call: Call<ProdukRespon>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                Log.d("test123", "T :" + t.message)
                progressBarAddProduk.visibility = View.GONE
            }

            override fun onResponse(call: Call<ProdukRespon>, response: Response<ProdukRespon>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                    progressBarAddProduk.visibility = View.GONE
                    val kategoripro = intent.getStringExtra("kategoriproduct")
                    val view = View(this@TambahEditProduct)
                    val intent = Intent(view.context,DaftarProduct::class.java)
                    intent.putExtra("kategoriproduct",kategoripro)
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    view.context.startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                    progressBarAddProduk.visibility = View.GONE
                }
            }
        })
}

    private fun getproduk(id_produk:String){
            Client.instance.getproduk(id_produk.toInt()).enqueue(object : Callback<Produk>{
                override fun onFailure(call: Call<Produk>, t: Throwable) {
                    progressBarAddProduk.visibility = View.GONE
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Produk>, response: Response<Produk>) {
                    val produk = response.body()
                    if (response.isSuccessful){
                        progressBarAddProduk.visibility = View.GONE
                        editTextProduct.setText(produk?.nama_produk.toString())
                        editTextHargaProduct.setText(produk?.harga_produk.toString())
                        editTextJumlahProduct.setText(produk?.qty_produk.toString())

                        if (produk?.diskon_produk == null || produk.diskon_produk == 0){
                            editTextDiskonProduct.setText("")
                        }else{
                            editTextDiskonProduct.setText(produk.diskon_produk.toString())
                        }

                        if(produk?.tgl_awal_diskon.isNullOrEmpty() && produk?.tgl_akhir_diskon.isNullOrEmpty()){
                            editTextDurasiAwalDiskonProduct.text  = "00-00-0000"
                            editTextDurasiAkhirDiskonProduct.text = "00-00-0000"
                        }else{
                            val tgl1 = produk?.tgl_awal_diskon.toString().substring(8,10)
                            val bln1 = produk?.tgl_awal_diskon.toString().substring(5,7)
                            val thn1 = produk?.tgl_awal_diskon.toString().substring(0,4)

                            val tgl2 = produk?.tgl_akhir_diskon.toString().substring(8,10)
                            val bln2 = produk?.tgl_akhir_diskon.toString().substring(5,7)
                            val thn2 = produk?.tgl_akhir_diskon.toString().substring(0,4)
                            editTextDurasiAwalDiskonProduct.text  = "$bln1-$tgl1-$thn1"
                            editTextDurasiAkhirDiskonProduct.text = "$bln2-$tgl2-$thn2"
                        }

                        if (produk?.diskon_olfood == null || produk.diskon_olfood == 0){
                            editTextDiskonOlfood.setText("")
                        }else{
                            editTextDiskonOlfood.setText(produk.diskon_olfood.toString())
                        }

                        if(produk?.tgl_awal_diskon_olfood.isNullOrEmpty() && produk?.tgl_akhir_diskon_olfood.isNullOrEmpty()){
                            textViewDurasi_Awal_Diskon_Olfood.text  = "00-00-0000"
                            textViewDurasi_Akhir_Diskon_Olfood.text = "00-00-0000"
                        }else{
                            val tgl1 = produk?.tgl_awal_diskon_olfood.toString().substring(8,10)
                            val bln1 = produk?.tgl_awal_diskon_olfood.toString().substring(5,7)
                            val thn1 = produk?.tgl_awal_diskon_olfood.toString().substring(0,4)

                            val tgl2 = produk?.tgl_akhir_diskon_olfood.toString().substring(8,10)
                            val bln2 = produk?.tgl_akhir_diskon_olfood.toString().substring(5,7)
                            val thn2 = produk?.tgl_akhir_diskon_olfood.toString().substring(0,4)
                            textViewDurasi_Awal_Diskon_Olfood.text  = "$bln1-$tgl1-$thn1"
                            textViewDurasi_Akhir_Diskon_Olfood.text = "$bln2-$tgl2-$thn2"
                        }

                        if(produk?.gbr_produk.isNullOrEmpty() || produk?.gbr_produk.toString() == ""){
                            val icon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.logomarikh)
                            val compresdBitmap = getResizedBitmap(icon, 150)
                            previewProduct.setImageBitmap(compresdBitmap)
                        }else{
                            old_gbr = produk?.gbr_produk.toString()
                            val stringGambar = Base64.decode(produk?.gbr_produk, Base64.DEFAULT)
                            val decodedStringGambar = BitmapFactory.decodeByteArray(stringGambar, 0, stringGambar.size)
                            //val compresdBitmap = getResizedBitmap(decodedStringGambar, 100)
                            previewProduct.setImageBitmap(decodedStringGambar)

                        }
                    }
                }
            })
    }

    private fun updateproduk(id_produk:String,namaproduk:String,hargaproduct:String,jumlahproduct:String,diskon_produk:Int?,id_kat_produk:String,tgl_awal_diskon:String?,tgl_akhir_diskon:String?,diskon_olfood:Int?,tgl_awal_diskon_olfood : String?,tgl_akhir_diskon_olfood:String?){
        val gambar:String

        gambar = if(selectedPhoto == null){
            old_gbr
        }else{
            val byteArrayOutputStream = ByteArrayOutputStream()
            selectedPhoto!!.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
            val encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
            encodedImage
        }

        Client.instance.updateproduk(
            id_produk.toInt(),
            namaproduk,
            hargaproduct.toInt(),
            jumlahproduct.toInt(),
            diskon_produk,
            id_kat_produk.toInt(),
            tgl_awal_diskon,
            tgl_akhir_diskon,
            diskon_olfood,
            tgl_awal_diskon_olfood,
            tgl_akhir_diskon_olfood,
            gambar
        ).enqueue(object : Callback<ProdukRespon>{
            override fun onFailure(call: Call<ProdukRespon>, t: Throwable) {
                try {
                    val kategoripro = intent.getStringExtra("kategoriproduct")
                    val view = View(this@TambahEditProduct)
                    val intent = Intent(view.context,DaftarProduct::class.java)
                    intent.putExtra("kategoriproduct",kategoripro)
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    view.context.startActivity(intent)
                    Toast.makeText(this@TambahEditProduct,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
                }catch (e:Exception){

                }
                progressBarAddProduk.visibility = View.GONE
            }

            override fun onResponse(call: Call<ProdukRespon>, response: Response<ProdukRespon>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                    progressBarAddProduk.visibility = View.GONE
                    val kategoripro = intent.getStringExtra("kategoriproduct")
                    val view = View(this@TambahEditProduct)
                    val intent = Intent(view.context,DaftarProduct::class.java)
                    intent.putExtra("kategoriproduct",kategoripro)
                    intent.putExtra("id_kat_produk",id_kat_produk)
                    view.context.startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                    progressBarAddProduk.visibility = View.GONE
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePickerDialog(textView:TextView) {
        val now = Calendar.getInstance()
        val datepicker = DatePickerDialog(this,AlertDialog.THEME_TRADITIONAL,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR,year)
            selectedDate.set(Calendar.MONTH,month)
            selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            val date = GregorianCalendar(year, month, dayOfMonth)
            val formatter = SimpleDateFormat("MM-dd-yyyy")

            textView.text = formatter.format(date.time)
        },
            now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datepicker.show()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(
                                applicationContext,
                                "All permissions are granted by user!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener {
                    Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show()
                }
                .onSameThread()
                .check()
        }
    }

    override fun onBackPressed() {
        val view = View(this)
        val id_kat_produk = intent.getStringExtra("id_kat_produk")
        val kategoriproduct = intent.getStringExtra("kategoriproduct")
        val intent = Intent(view.context,DaftarProduct::class.java)
        intent.putExtra("kategoriproduct",kategoriproduct)
        intent.putExtra("id_kat_produk",id_kat_produk)
        view.context.startActivity(intent)
        super.onBackPressed()
    }
}
