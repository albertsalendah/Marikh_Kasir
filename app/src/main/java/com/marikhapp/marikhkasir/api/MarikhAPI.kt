package com.marikhapp.marikhkasir.api

import com.marikhapp.marikhkasir.models.DefaultResponse
import com.marikhapp.marikhkasir.models.DeskripsiModels.Deskripsi
import com.marikhapp.marikhkasir.models.ProdukModels.Produk
import com.marikhapp.marikhkasir.models.ProdukModels.ProdukRespon
import com.marikhapp.marikhkasir.models.kategoriModels.Kategori
import com.marikhapp.marikhkasir.models.kategoriModels.UpdateKategoriResponse
import com.marikhapp.marikhkasir.models.userModels.LoginResponse
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
                     @Field("diskon_produk") diskon_produk:Int,
                     @Field("id_kat_produk") id_kat_produk:Int,
                     @Field("tgl_awal_diskon") tgl_awal_diskon:String,
                     @Field("tgl_akhir_diskon") tgl_akhir_diskon:String,
                     @Field("gbr_produk") gbr_produk:String
):Call<ProdukRespon>

    @FormUrlEncoded
    @POST("Produk.php/daftarproduk")
    fun daftarproduk(@Field("id_kat_produk")id_kat_produk:Int
    ):Call<List<Produk>>

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
                     @Field("diskon_produk") diskon_produk:Int,
                     @Field("id_kat_produk") id_kat_produk:Int,
                     @Field("tgl_awal_diskon") tgl_awal_diskon:String,
                     @Field("tgl_akhir_diskon") tgl_akhir_diskon:String,
                     @Field("gbr_produk") gbr_produk:String
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
    fun daftardeskripsiproduk(@Field("id_kat_produk")id_kat_produk:Int
    ):Call<List<Deskripsi>>


    @FormUrlEncoded
    @POST("Deskripsi.php/updatedeskripsiproduk")
    fun updatedeskripsiproduk(@Field("id_deskripsi") id_deskripsi:Int,
        @Field("nama_deskripsi") nama_deskripsi:String

    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Deskripsi.php/hapusdeskripsiproduk")
    fun hapusdeskripsiproduk(@Field("id_deskripsi")id_deskripsi:Int
    ):Call<DefaultResponse>
}