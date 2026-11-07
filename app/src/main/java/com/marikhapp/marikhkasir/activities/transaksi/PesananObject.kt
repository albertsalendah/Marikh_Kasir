package com.marikhapp.marikhkasir.activities.transaksi

import java.io.Serializable

class PesananObject(val id_produk:Int,val nama_produk:String,val harga_produk:Int,val id_kat_produk:Int,var jumlah_pesanan:Int,var listDeskripsi:ArrayList<String>,var jenis_transaksi:String,var listIdDeskripsi:ArrayList<Int>,var diskon_produk:Int?,val tgl_awal_diskon:String?,val tgl_akhir_diskon:String?,var addnotes:String?):Serializable