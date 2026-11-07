package com.marikhapp.marikhkasir.activities.transaksi.openBill

import android.os.Parcelable
import java.io.Serializable

class OpenBillObject (val id_trx_penjualan : String,val tgl_trx_input : String,val tgl_trx_bayar : String?,val tagihan_akhir : Int,var tagihan_awal :Int?,val total_diskon : Int?,var tax : Int?,var terbayar : Int?,val id_pelanggan : String,val nama : String,val no_meja : String,val id_method_bayar : Int,val jenis_tagihan : String,val id_user : Int,var checked : Boolean) : Serializable