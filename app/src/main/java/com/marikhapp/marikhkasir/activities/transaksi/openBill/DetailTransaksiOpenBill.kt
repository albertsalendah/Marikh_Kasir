package com.marikhapp.marikhkasir.activities.transaksi.openBill

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.transaksi.Bayar.Bayar
import com.marikhapp.marikhkasir.activities.transaksi.DetailTransaksi
import com.marikhapp.marikhkasir.activities.transaksi.PesananObject
import com.marikhapp.marikhkasir.activities.transaksi.SwipeToDeleteCallback
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DetailPenjualan.DetailPenjualan
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import kotlinx.android.synthetic.main.activity_detail_transaksi_open_bill.*
import kotlinx.android.synthetic.main.activity_transaksi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTransaksiOpenBill : AppCompatActivity() {
    var jumalahpesanan = 0
    val context = this@DetailTransaksiOpenBill
    val arrayP = ArrayList<OpenBillPesananObject>()
    lateinit var adapterPesananOpenBill: AdapterPesananOpenBill
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi_open_bill)
        val id_transaksi = intent.getIntExtra("id_transaksi_OB",0)
        constraitDetailTransOB.setOnClickListener {
            this.finish()
        }
         if (id_transaksi != 0){
             loadingDetailTransOpenBill.visibility = View.VISIBLE
             getIdInfo(id_transaksi)
         }else{
             Toast.makeText(this@DetailTransaksiOpenBill,"Terjadi Kesalahan Saat Memuat Data", Toast.LENGTH_LONG).show()
             this.finish()
             return
         }
    }

    private fun getIdInfo(id:Int) {
        Client.instance.getidtransaksi(id).enqueue(object : Callback<TrxPenjualan>{
            override fun onFailure(call: Call<TrxPenjualan>, t: Throwable) {}

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<TrxPenjualan>, response: Response<TrxPenjualan>) {
                val data = response.body()
                if (response.isSuccessful){
                    if (data != null){
                        val  id_trx_penjualan = data.id_trx_penjualan
                        val  tgl_trx_input    = data.tgl_trx_input
                        val  tgl_trx_bayar    = data.tgl_trx_bayar
                        val  tagihan_akhir    = data.tagihan_akhir
                        val  tagihan_awal     = data.tagihan_awal
                        val  total_diskon     = data.total_diskon
                        val  tax              = data.tax
                        val  terbayar         = data.terbayar
                        val  id_pelanggan     = data.id_pelanggan
                        val  nama             = data.nama
                        val  no_meja          = data.no_meja
                        val  id_method_bayar  = data.id_method_bayar.toString()
                        val  jenis_tagihan    = data.jenis_tagihan
                        val  id_user          = data.id_user.toString()

                        adapterPesananOpenBill = AdapterPesananOpenBill(getPesanan(id_trx_penjualan.toInt()),context)
                        daftar_pesanan_openbill.adapter = adapterPesananOpenBill

                        textViewNamaPelangganOB.text = ": $nama"
                        textViewTransOB.text = ": $id_trx_penjualan"
                        textViewTglTransOB.text = ": $tgl_trx_input"
                        textViewTaxOB.text = ": Rp.${pembulatanTagihan(tax!!)}"
                        textViewTo.text = "Rp.$tagihan_akhir"
                        textViewTo.setOnClickListener {
                            val intent = Intent(context, Bayar::class.java)
                            intent.putExtra("jenistransaksi","single_open_bill")
                            intent.putExtra("total_tagihan",tagihan_akhir.toString())
                            intent.putExtra("tax",tax)
                            intent.putExtra("id_trans",id_trx_penjualan)
                            context.startActivity(intent)
                        }

                    }
                }
            }
        })
    }

    fun getPesanan(id_transaksi : Int) : ArrayList<OpenBillPesananObject> {

        Client.instance.getdetailpenjualan(id_transaksi).enqueue(object :
            Callback<List<DetailPenjualan>> {
            override fun onFailure(call: Call<List<DetailPenjualan>>, t: Throwable) {}

            override fun onResponse(call: Call<List<DetailPenjualan>>, response: Response<List<DetailPenjualan>>) {
                if (response.isSuccessful){
                    loadingDetailTransOpenBill.visibility = View.GONE
                    val data = response.body()
                    for (item in data!!){
                        jumalahpesanan = data.size
                        arrayP.add(OpenBillPesananObject(item.id_trx_detpenjualan,item.id_trx_penjualan,item.qty_terjual,item.harga_terjual,item.diskon_terjual,item.status_pesanan,item.id_produk,item.jenis_transaksi,item.deskripsi,item.catatan_tambahan))
                    }
                    adapterPesananOpenBill.notifyDataSetChanged()
                }
            }
        })
        return arrayP
    }

    private fun pembulatanTagihan(tgh_akhir : Int) : Int {
        val hasil : Int
        if (tgh_akhir % 100 != 0){
            val sisa = tgh_akhir % 100
            hasil = (tgh_akhir-sisa)+100
        }else{
            hasil = tgh_akhir
        }
        return hasil
    }

  /*  private fun swipedelete(jenis_transaksi:String){

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val item = adapterPesananOpenBill.getData()[position]

                adapterPesananOpenBill.removeAt(position)
                if (adapterPesananOpenBill.itemCount >= 0){
                    if (!update_pesanan){
                        adapter.getTotal(jenis_transaksi,update_pesanan,0)
                    }else{
                        adapter.getTotal(jenis_transaksi,update_pesanan,upadateTagihanPesanan)
                    }
                }
                val snackbar: Snackbar = Snackbar.make(root_view_transaksi, item.nama_produk+"dihapus ", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO ?") {
                    adapter.restoreItem(item,detail,position)
                    listViewPesanan.scrollToPosition(position)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(listViewPesanan)
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
