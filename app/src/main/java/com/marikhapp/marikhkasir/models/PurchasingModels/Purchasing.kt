package com.marikhapp.marikhkasir.models.PurchasingModels

data class Purchasing(val id_trx_pengeluaran : String,val id_kat_pengeluaran : String ,val nama_pengeluaran : String,val tgl_pengeluaran : String,val nominal_pengeluaran : Int,val qty_pengeluaran : Int,val total_pengeluaran : Int,val jenis_post_pengeluaran : String)