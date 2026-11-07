package com.marikhapp.marikhkasir.models.ProdukModels

data class Produk (val id_produk:Int,val nama_produk:String,val harga_produk:Int,val qty_produk:Int,var diskon_produk :Int?,val id_kat_produk:Int,var tgl_awal_diskon:String?,var tgl_akhir_diskon:String?,var diskon_olfood : Int?,var tgl_awal_diskon_olfood:String?,var tgl_akhir_diskon_olfood:String?,val gbr_produk:String?)
