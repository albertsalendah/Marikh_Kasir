package com.marikhapp.marikhkasir.api

import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.DeskripsiModels.Deskripsi
import com.marikhapp.marikhkasir.models.DetailPenjualan.DetailPenjualan
import com.marikhapp.marikhkasir.models.DiskonModels.Diskon
import com.marikhapp.marikhkasir.models.DiskonModels.DiskonResponse
import com.marikhapp.marikhkasir.models.MethodBayarModels.MethodBayarModel
import com.marikhapp.marikhkasir.models.MethodBayarModels.MethodBayarRespon
import com.marikhapp.marikhkasir.models.PelangganModels.KategoriPelanggan
import com.marikhapp.marikhkasir.models.PelangganModels.Pelanggan
import com.marikhapp.marikhkasir.models.ProdukModels.Produk
import com.marikhapp.marikhkasir.models.ProdukModels.ProdukRespon
import com.marikhapp.marikhkasir.models.PurchasingModels.KategoriPurchasing
import com.marikhapp.marikhkasir.models.PurchasingModels.Purchasing
import com.marikhapp.marikhkasir.models.ReportOperational.ReportOperational
import com.marikhapp.marikhkasir.models.SaldoHarianModels.SaldoHarian
import com.marikhapp.marikhkasir.models.SettingModels.Setting
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualan
import com.marikhapp.marikhkasir.models.TrxPenjualanModels.TrxPenjualanRespon
import com.marikhapp.marikhkasir.models.kategoriModels.Kategori
import com.marikhapp.marikhkasir.models.kategoriModels.UpdateKategoriResponse
import com.marikhapp.marikhkasir.models.userModels.LoginResponse
import com.marikhapp.marikhkasir.models.userModels.User
import com.marikhapp.marikhkasir.models.userModels.UserToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MarikhAPI {

    @FormUrlEncoded
    @POST("createuser")
    fun createuser(
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("nama_cafe") nama_cafe:String,
        @Field("alamat") alamat:String,
        @Field("email") email:String
    ):Call<DefaultResponse>
//----USER------
    @FormUrlEncoded
    @POST("User.php/userlogin")
    fun userlogin(
        @Field("username") username:String,
        @Field("password") password: String
    ):Call<LoginResponse>
    @FormUrlEncoded
    @POST("User.php/getusername")
    fun getUser(@Field("username")username:String
    ):Call<User>
//----KATEGORI---------
    @FormUrlEncoded
    @POST("Kategori.php/tambahkategori")
    fun tambahkategori(
        @Field("nama_kat_produk") nama_kat_produk:String
    ):Call<UpdateKategoriResponse>

    @FormUrlEncoded
    @POST("Kategori.php/updatekategori")
    fun updatekategori(
        @Field("id_kat_produk") id_kat_produk:Int,
        @Field("nama_kat_produk") nama_kat_produk:String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Kategori.php/hapuskategori")
    fun hapuskategori(@Field("nama_kat_produk")nama_kat_produk: String):Call<DefaultResponse>

    @GET("Kategori.php/listkategori")
    fun listkategori():Call<List<Kategori>>

//----PRODUK---------
    @FormUrlEncoded
    @POST("Produk.php/tambahproduk")
    fun tambahproduk(@Field("nama_produk") nama_produk:String,
                     @Field("harga_produk") harga_produk:Int,
                     @Field("qty_produk") qty_produk:Int,
                     @Field("diskon_produk") diskon_produk:Int?,
                     @Field("id_kat_produk") id_kat_produk:Int,
                     @Field("tgl_awal_diskon") tgl_awal_diskon:String?,
                     @Field("tgl_akhir_diskon") tgl_akhir_diskon:String?,
                     @Field("diskon_olfood") diskon_olfood:Int?,
                     @Field("tgl_awal_diskon_olfood") tgl_awal_diskon_olfood:String?,
                     @Field("tgl_akhir_diskon_olfood") tgl_akhir_diskon_olfood:String?,
                     @Field("gbr_produk") gbr_produk:String?):Call<ProdukRespon>

    @FormUrlEncoded
    @POST("Produk.php/daftarproduk")
    fun daftarproduk(@Field("id_kat_produk")id_kat_produk:Int
    ):Call<List<Produk>>

    @GET("Produk.php/allproduk")
    fun allproduk():Call<List<Produk>>

    @FormUrlEncoded
    @POST("Produk.php/getproduk")
    fun getproduk(@Field("id_produk")id_produk:Int
    ):Call<Produk>

    @FormUrlEncoded
    @POST("Produk.php/updateproduk")
    fun updateproduk(@Field("id_produk")id_produk:Int,
                     @Field("nama_produk") nama_produk:String,
                     @Field("harga_produk") harga_produk:Int,
                     @Field("qty_produk") qty_produk:Int,
                     @Field("diskon_produk") diskon_produk:Int?,
                     @Field("id_kat_produk") id_kat_produk:Int,
                     @Field("tgl_awal_diskon") tgl_awal_diskon:String?,
                     @Field("tgl_akhir_diskon") tgl_akhir_diskon:String?,
                     @Field("diskon_olfood") diskon_olfood:Int?,
                     @Field("tgl_awal_diskon_olfood") tgl_awal_diskon_olfood:String?,
                     @Field("tgl_akhir_diskon_olfood") tgl_akhir_diskon_olfood:String?,
                     @Field("gbr_produk") gbr_produk:String?
    ):Call<ProdukRespon>

    @FormUrlEncoded
    @POST("Produk.php/hapusproduk")
    fun hapusproduk(@Field("id_produk")id_produk:Int
    ):Call<ProdukRespon>

    /*---Deskripsi----*/

    @FormUrlEncoded
    @POST("Deskripsi.php/tambahdeskripsiproduk")
    fun tambahdeskripsiproduk(@Field("nama_deskripsi") nama_deskripsi:String,
                     @Field("id_kat_produk") id_kat_produk:Int
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Deskripsi.php/daftardeskripsiproduk")
    fun daftardeskripsiproduk(@Field("id_kat_produk") id_kat_produk:Int
    ):Call<List<Deskripsi>>


    @FormUrlEncoded
    @POST("Deskripsi.php/updatedeskripsiproduk")
    fun updatedeskripsiproduk(@Field("id_deskripsi") id_deskripsi:Int,
        @Field("nama_deskripsi") nama_deskripsi:String

    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Deskripsi.php/hapusdeskripsiproduk")
    fun hapusdeskripsiproduk(@Field("id_deskripsi") id_deskripsi:Int
    ):Call<DefaultResponse>

    /*---ReportSaldoHarian---*/

    @GET("ReportSaldoHarian.php/daftarReportSaldoHarian")
    fun daftarReportSaldoHarian():Call<List<SaldoHarian>>

    @FormUrlEncoded
    @POST("ReportSaldoHarian.php/insertSaldoHarian")
    fun insertSaldoHarian(@Field("tgl_report_sh") tgl_report_sh:String,
                          @Field("saldo_awal") saldo_awal : Int
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("ReportSaldoHarian.php/updateReportSaldoHarian")
    fun updateReportSaldoHarian(
                          @Field("id_report_sh") id_report_sh : Int,
                          @Field("tgl_report_sh") tgl_report_sh:String,
                          @Field("saldo_awal") saldo_awal : Int,
                          @Field("saldo_akhir") saldo_akhir : Int
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("ReportSaldoHarian.php/getReportSaldoHarian")
    fun getReportSaldoHarian(@Field("tgl_report_sh") tgl_report_sh:String
    ):Call<SaldoHarian>

    /*---Pelanggan---*/
    @GET("Pelanggan.php/daftarpelanggan")
    fun daftarpelanggan():Call<List<Pelanggan>>

    @FormUrlEncoded
    @POST("Pelanggan.php/insertpelanggan")
    fun insertpelanggan(@Field("nama_pelanggan") nama_pelanggan:String,
                        @Field("hp_pelanggan") hp_pelanggan:String?,
                        @Field("email_pelanggan") email_pelanggan:String?,
                        @Field("tgl_lahir_pelanggan") tgl_lahir_pelanggan:String?,
                        @Field("id_kat_pelanggan") id_kat_pelanggan:String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Pelanggan.php/updatePelanggan")
    fun updatePelanggan(@Field("id_pelanggan") id_pelanggan:String,
                        @Field("nama_pelanggan") nama_pelanggan:String,
                        @Field("hp_pelanggan") hp_pelanggan:String?,
                        @Field("email_pelanggan") email_pelanggan:String?,
                        @Field("tgl_lahir_pelanggan") tgl_lahir_pelanggan:String?,
                        @Field("id_kat_pelanggan") id_kat_pelanggan:String
    ):Call<DefaultResponse>

    /*---KategoriPelanggan---*/
    @GET("Pelanggan.php/daftarkategoripelanggan")
    fun daftarkategoripelanggan():Call<List<KategoriPelanggan>>

    @FormUrlEncoded
    @POST("Pelanggan.php/insertkategoripelanggan")
    fun insertkategoripelanggan(@Field("nama_kat_pelanggan") nama_kat_pelanggan:String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Pelanggan.php/updatekategoriPelanggan")
    fun updatekategoriPelanggan(@Field("id_kat_pelanggan") id_kat_pelanggan:String,
        @Field("nama_kat_pelanggan") nama_kat_pelanggan:String
    ):Call<DefaultResponse>


    /*---TrxPenjualan---*/
    @GET("TRX_Penjualan.php/daftartrxpenjualan")
    fun daftartrxpenjualan():Call<List<TrxPenjualan>>

    @FormUrlEncoded
    @POST("TRX_Penjualan.php/hapusTrxPenjualan")
    fun hapusTrxPenjualan(@Field("id_trx_penjualan") id_trx_penjualan:String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("TRX_Penjualan.php/getjenistagihan")
    fun getjenistagihan(@Field("jenis_tagihan") jenis_tagihan:String
    ):Call<List<TrxPenjualan>>

    @FormUrlEncoded
    @POST("TRX_Penjualan.php/gettagihanbynama")
    fun gettagihanbynama(@Field("nama") nama:String
    ):Call<List<TrxPenjualan>>

    @FormUrlEncoded
    @POST("TRX_Penjualan.php/getidtransaksi")
    fun getidtransaksi(@Field("id_trx_penjualan") id_trx_penjualan:Int
    ):Call<TrxPenjualan>

    @FormUrlEncoded
    @POST("TRX_Penjualan.php/updatetrxpenjualan")
    fun updatetrxpenjualan(@Field("id_trx_penjualan") id_trx_penjualan: String,
                      @Field("tgl_trx_bayar") tgl_trx_bayar: String,
                      @Field("tax") tax : Int?,
                      @Field("terbayar") terbayar: String,
                      @Field("id_method_bayar") id_method_bayar:String,
                      @Field("jenis_tagihan") jenis_tagihan: String):Call<TrxPenjualanRespon>
    @FormUrlEncoded
    @POST("TRX_Penjualan.php/tambahpesanan")
    fun tambahpesanan(@Field("id_trx_penjualan") id_trx_penjualan: String,
                      @Field("tgl_trx_input") tgl_trx_input: String,
                      @Field("tagihan_akhir") tagihan_akhir: Int,
                      @Field("tagihan_awal") tagihan_awal:Int,
                      @Field("total_diskon") total_diskon: Int?,
                      @Field("tax") tax : Int?):Call<TrxPenjualanRespon>

    @FormUrlEncoded
    @POST("TRX_Penjualan.php/inserttrxpenjualan")
    fun inserttrxpenjualan(@Field("id_trx_penjualan") id_trx_penjualan: String,
                           @Field("tgl_trx_input") tgl_trx_input: String,
                           @Field("tgl_trx_bayar") tgl_trx_bayar: String?,
                           @Field("tagihan_akhir") tagihan_akhir: Int,
                           @Field("tagihan_awal") tagihan_awal:Int,
                           @Field("total_diskon") total_diskon: Int?,
                           @Field("tax") tax : Int?,
                           @Field("terbayar") terbayar: Int?,
                           @Field("komisi") komisi: Int?,
                           @Field("id_pelanggan") id_pelanggan: String,
                           @Field("nama") nama: String,
                           @Field("no_meja") no_meja : String,
                           @Field("id_method_bayar") id_method_bayar: Int?,
                           @Field("jenis_tagihan") jenis_tagihan: String,
                           @Field("id_user") id_user: Int?):Call<DefaultResponse>


    /*---Detail Penjualan---*/
    @GET("DetailPenjualan.php/daftarDetailPenjualan")
    fun daftarDetailPenjualan():Call<List<DetailPenjualan>>

    @FormUrlEncoded
    @POST("DetailPenjualan.php/insertdetailpenjualan")
    fun insertdetailpenjualan(@Field("id_trx_detpenjualan") id_trx_detpenjualan : String,
                              @Field("id_trx_penjualan") id_trx_penjualan : String,
                              @Field("qty_terjual") qty_terjual : Int,
                              @Field("harga_terjual") harga_terjual : Int,
                              @Field("diskon_terjual") diskon_terjual : Int?,
                              @Field("status_pesanan") status_pesanan : String,
                              @Field("id_produk") id_produk : Int,
                              @Field("jenis_transaksi") jenis_transaksi : String,
                              @Field("deskripsi") deskripsi:String?,
                              @Field("catatan_tambahan") catatan_tambahan:String?):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("DetailPenjualan.php/getdetailpenjualan")
    fun getdetailpenjualan(@Field("id_trx_penjualan") id_trx_penjualan:Int
    ):Call<List<DetailPenjualan>>

    /*---Purchasing---*/
    @GET("Pengeluaran.php/allpengeluaran")
    fun daftarpengeluaran():Call<List<Purchasing>>

    @GET("Pengeluaran.php/allkategoripengeluaran")
    fun daftarkategoripengeluaran():Call<List<KategoriPurchasing>>

    @FormUrlEncoded
    @POST("Pengeluaran.php/insertpengeluaran")
    fun insertPengeluaran(
                              @Field("id_kat_pengeluaran") id_kat_pengeluaran : String,
                              @Field("nama_pengeluaran") nama_pengeluaran : String,
                              @Field("tgl_pengeluaran") tgl_pengeluaran : String,
                              @Field("nominal_pengeluaran") nominal_pengeluaran : Int,
                              @Field("qty_pengeluaran") qty_pengeluaran : Int,
                              @Field("total_pengeluaran") total_pengeluaran : Int,
                              @Field("jenis_post_pengeluaran") jenis_post_pengeluaran : String):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Pengeluaran.php/updatePengeluaran")
    fun updatePengeluaran(@Field("id_trx_pengeluaran") id_trx_pengeluaran : String,
        @Field("id_kat_pengeluaran") id_kat_pengeluaran : String,
        @Field("nama_pengeluaran") nama_pengeluaran : String,
        @Field("tgl_pengeluaran") tgl_pengeluaran : String,
        @Field("nominal_pengeluaran") nominal_pengeluaran : Int,
        @Field("qty_pengeluaran") qty_pengeluaran : Int,
        @Field("total_pengeluaran") total_pengeluaran : Int,
        @Field("jenis_post_pengeluaran") jenis_post_pengeluaran : String):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Pengeluaran.php/insertkategoripengeluaran")
    fun insertKategoriPengeluaran(@Field("nama_kat_pengeluaran") nama_kat_pengeluaran : String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Pengeluaran.php/getkategoripengeluaranbyid")
    fun getkategoripengeluaran(@Field("id_kat_pengeluaran") id_kat_pengeluaran:String
    ):Call<KategoriPurchasing>

    @FormUrlEncoded
    @POST("Pengeluaran.php/updatekategoriPengeluaran")
    fun updatekategoriPengeluaran(
        @Field("id_kat_pengeluaran") id_kat_pengeluaran : String,
        @Field("nama_kat_pengeluaran") nama_kat_pengeluaran : String):Call<DefaultResponse>


    //-----MethodBayar-----
    @GET("MethodPembayaran.php/daftarmethodbayar")
    fun daftarmethodbayar():Call<List<MethodBayarModel>>

    @FormUrlEncoded
    @POST("MethodPembayaran.php/insertmethodbayar")
    fun insertmethodbayar(@Field("nama_method") nama_method:String,
                          @Field("reguler") reguler:String,
                          @Field("olfood") olfood:String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("MethodPembayaran.php/updateMethodBayar")
    fun updateMethodBayar(@Field("id_method_bayar") id_method_bayar:String,
                          @Field("nama_method") nama_method:String,
                          @Field("reguler") reguler:String,
                          @Field("olfood") olfood:String
    ):Call<MethodBayarRespon>

    //----------Setting-----------
    @GET("Setting.php/settings")
    fun settings():Call<List<Setting>>

    @FormUrlEncoded
    @POST("Setting.php/updateModalAwal")
    fun updateModalAwal(
        @Field("id") id : String,
        @Field("modal_awal") modal_awal : String):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Setting.php/updateTax")
    fun updateTax(
        @Field("id") id : String,
        @Field("tax") tax : String):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Setting.php/updateGofood")
    fun updateGofood(
        @Field("id") id : String,
        @Field("gofoodph") gofoodph : String,
        @Field("gofoodec") gofoodec : String,
        @Field("gofoodkomisi") gofoodkomisi : String):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Setting.php/updateGrabfood")
    fun updateGrabfood(
        @Field("id") id : String,
        @Field("grabfoodph") grabfoodph : String,
        @Field("grabfoodec") grabfoodec : String,
        @Field("grabfoodkomisi") grabfoodkomisi : String):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Setting.php/insertSetting")
    fun insertSetting(
        @Field("modal_awal") modal_awal : String,
        @Field("tax") tax : String,
        @Field("gofoodph") gofoodph : String,
        @Field("gofoodec") gofoodec : String,
        @Field("gofoodkomisi") gofoodkomisi : String,
        @Field("grabfoodph") grabfoodph : String,
        @Field("grabfoodec") grabfoodec : String,
        @Field("grabfoodkomisi") grabfoodkomisi : String):Call<DefaultResponse>

    @GET("Setting.php/listreportoperational")
    fun listreportoperational():Call<List<ReportOperational>>

    @FormUrlEncoded
    @POST("Setting.php/insertreportoperational")
    fun insertreportoperational(
        @Field("id_report_all") id_report_all : String,
        @Field("tgl_trx_operational") tgl_trx_operational : String,
        @Field("keterangan") keterangan : String,
        @Field("kredit_op") kredit_op : String,
        @Field("debit_op") debit_op : String
    ):Call<DefaultResponse>

    //--------DISKON-----------
    @GET("Diskon.php/daftardiskon")
    fun daftardiskon():Call<List<Diskon>>

    @FormUrlEncoded
    @POST("Diskon.php/insertDiskon")
    fun insertDiskon(
        @Field("deskripsi_disc") deskripsi_disc : String,
        @Field("nominal_disc") nominal_disc : Int
    ):Call<DefaultResponse>


    @FormUrlEncoded
    @POST("Diskon.php/updateDiskon")
    fun updateDiskon(
        @Field("id_disc") id_disc : String,
        @Field("deskripsi_disc") deskripsi_disc : String,
        @Field("nominal_disc") nominal_disc : Int):Call<DiskonResponse>

    //-----------------Voucher--------------------------------------
    @FormUrlEncoded
    @POST("Voucher.php/getVoucherByKodeVoucher")
    fun getVoucherByKodeVoucher(
        @Field("kode_voucher")kode_voucher:String,
        @Field("nama_database") nama_database: String
    ): Call<Int>

    @FormUrlEncoded
    @POST("Voucher.php/updateStatusVoucherPelanggan")
    fun updateStatusVoucherPelanggan(
        @Field("status")status:String,
        @Field("kode_voucher")kode_voucher:String,
        @Field("id_pelanggan")id_pelanggan:String,
        @Field("nama_database") nama_database: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Voucher.php/updatePoinPelanggan")
    fun updatePoinPelanggan(
        @Field("total_point")total_point:String,
        @Field("id_pelanggan")id_pelanggan:String,
        @Field("nama_database") nama_database: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Voucher.php/getTokenPelanggan")
    fun getTokenPelanggan(
        @Field("id_pelanggan")id_pelanggan:String,
        @Field("nama_database") nama_database: String
    ): Call<UserToken>

}