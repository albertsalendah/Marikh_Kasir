package com.marikhapp.marikhkasir.activities.transaksi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.transaksi.deskripsiPesanan.DeskripsiPesanan
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nama_produk_pesanan = itemView.findViewById(R.id.namaProdukYgDiPesan) as TextView
    val listdeskripsi = itemView.findViewById(R.id.listDesk) as GridView
    val buttonPlus = itemView.findViewById(R.id.buttonPlusPesanan) as Button
    val buttonMin = itemView.findViewById(R.id.buttonMinPesanan) as Button
    val jumlah_pesanan = itemView.findViewById(R.id.editTextJumlahPesanan) as EditText
    val total_perProduk = itemView.findViewById(R.id.textViewTotalHargaPerProduk) as TextView
    val harga_produk = itemView.findViewById(R.id.textViewHargaProduk) as TextView
    val textViewJenisTransaksi = itemView.findViewById(R.id.textViewJenisTransaksi) as TextView
    val addnotes = itemView.findViewById<TextView>(R.id.textViewAddNotes)
    val textCat = itemView.findViewById<TextView>(R.id.textView30)
    val textviewDiscProduk = itemView.findViewById<TextView>(R.id.textViewDiscProduk)
}

class PesananAdapter(var listPesanan:ArrayList<PesananObject>,val context:Context,val activity: Activity,val textViewSub:TextView,var updatePesanan:Boolean,var updateTagihanPesanan:Int) : RecyclerView.Adapter<Holder>() {
    var totalharga = 0
    var arrayListDetailTransaksi = ArrayList<DetailTransaksi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pesanan, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
       return listPesanan.size
    }

    fun removeAt(position: Int) {
        arrayListDetailTransaksi.removeAt(position)
        listPesanan.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: PesananObject,detail : DetailTransaksi, position: Int) {
        arrayListDetailTransaksi.add(position,detail)
        listPesanan.add(position, item)
        notifyItemInserted(position)
    }

    fun getData(): java.util.ArrayList<PesananObject> {
        return listPesanan
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun getTotal(jenis_transaksi:String,updatePesanan:Boolean,updateTagihanPesanan:Int){
        val sharedPreferencesTax: SharedPreferences = context.getSharedPreferences("TAX",Context.MODE_PRIVATE)
        val tax = 0/*sharedPreferencesTax.getInt("tax",0)*/
        val tax2 = sharedPreferencesTax.getInt("tax",0)
        /*val sharedPreferences: SharedPreferences = context.getSharedPreferences("OlFoodCharge",Context.MODE_PRIVATE)
        val charge = sharedPreferences.getInt("olfoodcharge",0)*/
        var subTotal : Int
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMddkm")
        val tglhariini = formatter.format(date)
        var diskon_produk : Int?
        var batas_diskon = ""
        var charge = 0
        var extraCharge = 0
        if (jenis_transaksi == "Gofood"){
            val preferencesGoFood = context.getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
            charge = preferencesGoFood.getInt("PenyesuaianHargaGoFood", 0)
            extraCharge = preferencesGoFood.getInt("ExtraChargeGoFood", 0)
        }else if(jenis_transaksi == "Grabfood"){
            val preferencesGrabfood = context.getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
            charge = preferencesGrabfood.getInt("PenyesuaianHargaGrabFood", 0)
            extraCharge = preferencesGrabfood.getInt("ExtraChargeGrabFood", 0)
        }
        //Log.d("test123","Charge GetTotal $charge")
        if (jenis_transaksi == "Gofood" || jenis_transaksi == "Grabfood"){
            subTotal = 0
            for (item in getData()){
                if (item.tgl_akhir_diskon != null){
                    val tgl_akhir = item.tgl_akhir_diskon+"-23-00"
                    val tgl_akhir_diskon : Date = formatter.parse(tgl_akhir.replace("-",""))!!
                    batas_diskon = formatter.format(tgl_akhir_diskon)
                }
                if (tglhariini > batas_diskon){
                    diskon_produk = 0
                }else{
                    diskon_produk = item.diskon_produk
                }
                if (diskon_produk!! > 0){
                    val olfoodCharge = item.harga_produk * charge/100
                    val EXC = ((item.harga_produk+extraCharge)+((item.harga_produk+extraCharge)*tax2/100))*charge/100
                    val diskon = (item.harga_produk + EXC) * diskon_produk/100
                    //------------------------------------------------------------------------
                    subTotal += item.jumlah_pesanan*pembulatanTagihan(((item.harga_produk + tax)+EXC-diskon))
                    //subTotal += item.jumlah_pesanan*(item.harga_produk+olfoodCharge-diskon)
                }else{
                    val olfoodCharge = item.harga_produk * charge/100
                    val EXC = ((item.harga_produk+extraCharge)+((item.harga_produk+extraCharge)*tax2/100))*charge/100
                    //---------------------------------------------------------------
                    subTotal += item.jumlah_pesanan*pembulatanTagihan(((item.harga_produk + tax)+EXC))
                    //subTotal += item.jumlah_pesanan*(item.harga_produk+olfoodCharge)
                }
            }
        }else{
            subTotal = 0
            for (item in getData()){
                if (item.tgl_akhir_diskon != null){
                    val tgl_akhir = item.tgl_akhir_diskon+"-23-00"
                    val tgl_akhir_diskon : Date = formatter.parse(tgl_akhir.replace("-",""))!!
                    batas_diskon = formatter.format(tgl_akhir_diskon)
                }
                if (tglhariini > batas_diskon){
                    diskon_produk = 0
                }else{
                    diskon_produk = item.diskon_produk
                }
                if (diskon_produk!! > 0){
                    val diskon = item.harga_produk * diskon_produk/100
                    //---------------------------------------------------------------
                    if (item.jenis_transaksi != "Dine In"){
                        subTotal += item.jumlah_pesanan*pembulatanTagihan(((item.harga_produk + tax)-diskon))
                    }else{
                        subTotal += item.jumlah_pesanan*pembulatanTagihan((item.harga_produk-diskon))
                    }
                    //subTotal += item.jumlah_pesanan*(item.harga_produk-diskon)
                }else{
                    //---------------------------------------------------------------
                    if (item.jenis_transaksi != "Dine In"){
                        subTotal += item.jumlah_pesanan*pembulatanTagihan((item.harga_produk + tax))
                    }else{
                        subTotal += item.jumlah_pesanan*pembulatanTagihan(item.harga_produk)
                    }
                    //subTotal += item.jumlah_pesanan*item.harga_produk
                }
            }
        }
        if (updatePesanan){
            textViewSub.text = (subTotal + updateTagihanPesanan).toString()
        }else{
            textViewSub.text = subTotal.toString()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun setTotal(holder: Holder, pesanan: PesananObject, hit:Int, position: Int){
        val sharedPreferencesTax: SharedPreferences = context.getSharedPreferences("TAX",Context.MODE_PRIVATE)
        val tax = 0/*sharedPreferencesTax.getInt("tax",0)*/
        val tax2 = sharedPreferencesTax.getInt("tax",0)
        /*val sharedPreferences: SharedPreferences = context.getSharedPreferences("OlFoodCharge",Context.MODE_PRIVATE)
        val charge = sharedPreferences.getInt("olfoodcharge",0)*/
        pesanan.jumlah_pesanan = hit
        holder.jumlah_pesanan.setText(hit.toString())
        var charge = 0
        var extraCharge = 0
        if (pesanan.jenis_transaksi == "Gofood"){
            val preferencesGoFood = context.getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
            charge = preferencesGoFood.getInt("PenyesuaianHargaGoFood", 0)
            extraCharge = preferencesGoFood.getInt("ExtraChargeGoFood", 0)
        }else if(pesanan.jenis_transaksi == "Grabfood"){
            val preferencesGrabfood = context.getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
            charge = preferencesGrabfood.getInt("PenyesuaianHargaGrabFood", 0)
            extraCharge = preferencesGrabfood.getInt("ExtraChargeGrabFood", 0)
        }
        val olfoodCharge = pesanan.harga_produk * charge/100
        val EXC = ((pesanan.harga_produk+extraCharge)+((pesanan.harga_produk+extraCharge)*tax2/100))*charge/100
        Log.d("test123","Charge : $EXC")
        //Log.d("test123","Charge SetTotal $charge")
        //Log.d("test123",olfoodCharge.toString())
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMddkm")
        val tglhariini = formatter.format(date)
        val diskon_produk : Int?
        var batas_diskon = ""
        if (pesanan.tgl_akhir_diskon != null){
            val tgl_akhir = pesanan.tgl_akhir_diskon+"-23-00"
            val tgl_akhir_diskon : Date = formatter.parse(tgl_akhir.replace("-",""))!!
            batas_diskon = formatter.format(tgl_akhir_diskon)
        }
        if (pesanan.jenis_transaksi == "Gofood" || pesanan.jenis_transaksi == "Grabfood"){
            if (tglhariini > batas_diskon){
                diskon_produk = 0
            }else{
                diskon_produk = pesanan.diskon_produk
            }
            if (diskon_produk!! > 0){
                val diskon = (pesanan.harga_produk + EXC)*diskon_produk/100
                val harga_produk = pesanan.harga_produk + EXC-diskon
                //-----------------------------------------------------------
                totalharga = hit * pembulatanTagihan(((pesanan.harga_produk + tax) + EXC-diskon))
                //totalharga = hit * (pesanan.harga_produk + olfoodCharge-diskon)
                holder.total_perProduk.text = totalharga.toString()
                val diskon_terjual = /*hit * */diskon
                val tagihan_awal = hit * (pesanan.harga_produk + EXC)
                arrayListDetailTransaksi.add(DetailTransaksi(pesanan.id_produk,hit,harga_produk,totalharga,pesanan.jenis_transaksi,diskon_terjual,pesanan.listDeskripsi,pesanan.addnotes,tagihan_awal,position))
            }else {
                val harga_produk = pesanan.harga_produk + EXC
                //---------------------------------------------------------
                totalharga = hit * pembulatanTagihan(((pesanan.harga_produk + tax) + EXC))
                //totalharga = hit * (pesanan.harga_produk + olfoodCharge)
                holder.total_perProduk.text = totalharga.toString()
                val tagihan_awal = hit * (pesanan.harga_produk + EXC)
                arrayListDetailTransaksi.add(DetailTransaksi(pesanan.id_produk,hit,harga_produk,totalharga,pesanan.jenis_transaksi,diskon_produk,pesanan.listDeskripsi,pesanan.addnotes,tagihan_awal,position))
            }
        }else{
            if (tglhariini > batas_diskon){
                diskon_produk = 0
            }else{
                diskon_produk = pesanan.diskon_produk
            }
            if (diskon_produk!! > 0 ){
                val diskon = pesanan.harga_produk*diskon_produk/100
                val harga_produk = pesanan.harga_produk - diskon
                //---------------------------------------------------
                if (pesanan.jenis_transaksi != "Dine In"){
                    totalharga = hit * pembulatanTagihan(((pesanan.harga_produk + tax) - diskon))
                }else{
                    totalharga = hit * pembulatanTagihan((pesanan.harga_produk - diskon))
                }
                //totalharga = hit * (pesanan.harga_produk - diskon)
                holder.total_perProduk.text = totalharga.toString()
                val diskon_terjual = /*hit * */diskon
                val tagihan_awal = hit * pesanan.harga_produk
                arrayListDetailTransaksi.add(DetailTransaksi(pesanan.id_produk,hit,harga_produk,totalharga,pesanan.jenis_transaksi,diskon_terjual,pesanan.listDeskripsi,pesanan.addnotes,tagihan_awal,position))
            }else {
                val harga_produk = pesanan.harga_produk
                //---------------------------------------------
                if (pesanan.jenis_transaksi != "Dine In"){
                    totalharga = hit * pembulatanTagihan((pesanan.harga_produk + tax))
                }else{
                    totalharga = hit * pembulatanTagihan(pesanan.harga_produk)
                }
                //totalharga = hit * pesanan.harga_produk
                holder.total_perProduk.text = totalharga.toString()
                val tagihan_awal = hit * pesanan.harga_produk
                arrayListDetailTransaksi.add(DetailTransaksi(pesanan.id_produk,hit,harga_produk,totalharga,pesanan.jenis_transaksi,diskon_produk,pesanan.listDeskripsi,pesanan.addnotes,tagihan_awal,position))
            }
        }

        holder.jumlah_pesanan.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                arrayListDetailTransaksi[position].jumlah_pesanan = holder.jumlah_pesanan.text.toString().toInt()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                arrayListDetailTransaksi[position].jumlah_pesanan = holder.jumlah_pesanan.text.toString().toInt()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                arrayListDetailTransaksi[position].jumlah_pesanan = holder.jumlah_pesanan.text.toString().toInt()
            }
        })
    }
    private fun pembulatanTagihan(tgh_akhir : Int) : Int {
        val hasil : Int
        if (tgh_akhir % 100 != 0){
            val sisa = tgh_akhir % 100
            hasil = (tgh_akhir-sisa)+100
            /*if (sisa >= 50){
                hasil = (tgh_akhir-sisa)+100
            }else{
                hasil = (tgh_akhir-sisa)//+100
            }*/
        }else{
            hasil = tgh_akhir
        }
        return hasil
    }
    fun getDetail():ArrayList<DetailTransaksi>{
        return arrayListDetailTransaksi
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        var hit = 0
        val adapter : AdapterDeskripsiPesanan2
        val pesanan : PesananObject = listPesanan[position]
        hit = pesanan.jumlah_pesanan
        holder.nama_produk_pesanan.text = pesanan.nama_produk
        holder.jumlah_pesanan.setText(hit.toString())
        holder.textViewJenisTransaksi.text = pesanan.jenis_transaksi
        holder.addnotes.text = pesanan.addnotes
        if (pesanan.addnotes.toString().isEmpty()){
            holder.addnotes.visibility = View.GONE
            holder.textCat.visibility = View.GONE
        }else{
            holder.addnotes.visibility = View.VISIBLE
            holder.textCat.visibility = View.VISIBLE
        }
        //var hit = holder.jumlah_pesanan.text.toString().toInt()
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMddkm")
        val tglhariini = formatter.format(date)
        val diskon_produk : Int?
        var batas_diskon = ""
        /*val sharedPreferences: SharedPreferences = context.getSharedPreferences("OlFoodCharge",Context.MODE_PRIVATE)
        val charge = sharedPreferences.getInt("olfoodcharge",0)*/
        var charge = 0
        var extraCharge = 0
        val preferencesGoFood = context.getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
        val preferencesGrabfood = context.getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
        if (pesanan.jenis_transaksi == "Gofood"){
            charge = preferencesGoFood.getInt("PenyesuaianHargaGoFood", 0)
            extraCharge = preferencesGoFood.getInt("ExtraChargeGoFood", 0)
        }else if(pesanan.jenis_transaksi == "Grabfood"){
            charge = preferencesGrabfood.getInt("PenyesuaianHargaGrabFood", 0)
            extraCharge = preferencesGrabfood.getInt("ExtraChargeGrabFood", 0)
        }

        val sharedPreferencesTax: SharedPreferences = context.getSharedPreferences("TAX",Context.MODE_PRIVATE)
        val tax2 = sharedPreferencesTax.getInt("tax",0)

        val olfoodCharge = pesanan.harga_produk * charge/100
        val EXC = ((pesanan.harga_produk+extraCharge)+((pesanan.harga_produk+extraCharge)*tax2/100))*charge/100

        if (pesanan.tgl_akhir_diskon != null){
            val tgl_akhir = pesanan.tgl_akhir_diskon+"-23-00"
            val tgl_akhir_diskon : Date = formatter.parse(tgl_akhir.replace("-",""))!!
            batas_diskon = formatter.format(tgl_akhir_diskon)
        }
    //----------------------------------------------------------------
        if (pesanan.jenis_transaksi == "Gofood" || pesanan.jenis_transaksi == "Grabfood"){
            if (tglhariini > batas_diskon){
                diskon_produk = 0
            }else{
                diskon_produk = pesanan.diskon_produk
            }
            if (diskon_produk!!.toInt() > 0){
                //val diskon = pesanan.harga_produk * diskon_produk/100
                val diskon = (pesanan.harga_produk+EXC) * diskon_produk/100
                holder.harga_produk.text = (pesanan.harga_produk+EXC).toString()
                holder.textviewDiscProduk.text = "(Rp.${diskon * hit})"
            }else {
                holder.harga_produk.text = (pesanan.harga_produk + EXC).toString()
            }
        }else{
            if (tglhariini > batas_diskon){
                diskon_produk = 0
            }else{
                diskon_produk = pesanan.diskon_produk
            }
            if (diskon_produk!!.toInt() > 0){
                val diskon = pesanan.harga_produk * diskon_produk/100
                holder.harga_produk.text = pesanan.harga_produk.toString()
                holder.textviewDiscProduk.text = "(Rp.${diskon * hit})"
            }else {
                holder.harga_produk.text = pesanan.harga_produk.toString()
            }
        }
        pesanan.jumlah_pesanan = hit
        setTotal(holder,pesanan,hit,position)
        holder.buttonPlus.setOnClickListener{
           if (hit >= 1){
               hit += 1
               setTotal(holder,pesanan,hit,position)
               setdisc(pesanan,tglhariini,batas_diskon,holder,hit)
           }
        }
        holder.buttonMin.setOnClickListener {
            if(hit > 1){
                hit -= 1
                setTotal(holder,pesanan,hit,position)
                setdisc(pesanan,tglhariini,batas_diskon,holder,hit)
            }
        }

        holder.nama_produk_pesanan.setOnClickListener {
            val intent = Intent(activity, DeskripsiPesanan::class.java)
            intent.putExtra("comefrom","daftarpesanan")
            intent.putExtra("id_produk",pesanan.id_produk)
            intent.putExtra("nama_produk",pesanan.nama_produk)
            intent.putExtra("harga_produk",pesanan.harga_produk)
            intent.putExtra("jumlah_pesanan",pesanan.jumlah_pesanan.toString())
            intent.putExtra("id_kat_produk",pesanan.id_kat_produk.toString())
            intent.putIntegerArrayListExtra("idDeskripsi",pesanan.listIdDeskripsi)
            intent.putExtra("indexpesanan",position.toString())
            activity.startActivityForResult(intent,4)
        }

        holder.total_perProduk.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (!updatePesanan){
                    getTotal(pesanan.jenis_transaksi,updatePesanan,0)
                }else{
                    getTotal(pesanan.jenis_transaksi,updatePesanan,updateTagihanPesanan)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (!updatePesanan){
                    getTotal(pesanan.jenis_transaksi,updatePesanan,0)
                }else{
                    getTotal(pesanan.jenis_transaksi,updatePesanan,updateTagihanPesanan)
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!updatePesanan){
                    getTotal(pesanan.jenis_transaksi,updatePesanan,0)
                }else{
                    getTotal(pesanan.jenis_transaksi,updatePesanan,updateTagihanPesanan)
                }
            }
        })

        if (pesanan.listDeskripsi.size == 0){
            val params = holder.listdeskripsi.layoutParams
            params.height = 0
        }
        adapter = AdapterDeskripsiPesanan2(pesanan.listDeskripsi,context)
        holder.listdeskripsi.adapter = adapter
        holder.listdeskripsi.deferNotifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun setdisc(pesanan: PesananObject, tglhariini:String, batas_diskon:String, holder: Holder, hit: Int){
        val diskon_produk : Int?
        var charge = 0
        var extraCharge = 0
        val preferencesGoFood = context.getSharedPreferences("GOFOOD", Context.MODE_PRIVATE)
        val preferencesGrabfood = context.getSharedPreferences("GRABFOOD", Context.MODE_PRIVATE)
        if (pesanan.jenis_transaksi == "Gofood"){
            charge = preferencesGoFood.getInt("PenyesuaianHargaGoFood", 0)
            extraCharge = preferencesGoFood.getInt("ExtraChargeGoFood", 0)
        }else if(pesanan.jenis_transaksi == "Grabfood"){
            charge = preferencesGrabfood.getInt("PenyesuaianHargaGrabFood", 0)
            extraCharge = preferencesGrabfood.getInt("ExtraChargeGrabFood", 0)
        }
        val sharedPreferencesTax: SharedPreferences = context.getSharedPreferences("TAX",Context.MODE_PRIVATE)
        val tax2 = sharedPreferencesTax.getInt("tax",0)
        if (pesanan.jenis_transaksi == "Gofood" || pesanan.jenis_transaksi == "Grabfood"){
            if (tglhariini > batas_diskon){
                diskon_produk = 0
            }else{
                diskon_produk = pesanan.diskon_produk
            }
            if (diskon_produk!!.toInt() > 0){
                //----------------------------------------------------------------------------
                val olfoodCharge = pesanan.harga_produk * charge/100
                val EXC = ((pesanan.harga_produk+extraCharge)+((pesanan.harga_produk+extraCharge)*tax2/100))*charge/100
                val diskon = hit * ((pesanan.harga_produk+EXC) * diskon_produk/100)
                holder.textviewDiscProduk.text = "(Rp.${diskon})"
            }
        }else{
            if (tglhariini > batas_diskon){
                diskon_produk = 0
            }else{
                diskon_produk = pesanan.diskon_produk
            }
            if (diskon_produk!!.toInt() > 0){
                val diskon = pesanan.harga_produk * diskon_produk/100
                holder.textviewDiscProduk.text = "(Rp.${diskon * hit})"
            }
        }
    }

}