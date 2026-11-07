package com.marikhapp.marikhkasir.api

import android.util.Base64
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    var gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

    private const val BASE_URL = "http://192.168.43.201/Marikh_Kasir/public/"
    //----marikh---
//    private const val BASE_URL = "http://www.marikhsalatiga.com/MarikhAPI/public/"

    private val AUTH = "Basic "+Base64.encodeToString("marikh:marikh".toByteArray(),Base64.NO_WRAP)
    private  val okHttpClient = OkHttpClient.Builder().retryOnConnectionFailure(true)
        .addInterceptor{
                chain ->  val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Authorization",AUTH)
                .addHeader("Connection","close")
                .method(original.method, original.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()
    val instance : MarikhAPI by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(MarikhAPI::class.java)
    }

}