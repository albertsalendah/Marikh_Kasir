package com.marikhapp.marikhkasir.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.userModels.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonlogin.setOnClickListener {
            val user =  username.text.toString()
            val pass =  password.text.toString()
            if(user.isEmpty()){
                username.error = "Username Kosong"
                username.requestFocus()
            }
            if(pass.isEmpty()){
                password.error = "Password Kosong"
                password.requestFocus()
            }
           if(user.isNotEmpty() && pass.isNotEmpty()){
               login(user,pass)
           }
        }
    }
    private fun login(user:String,pass:String){
        Client.instance.userlogin(user,pass)
            .enqueue(object : Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if(!response.body()?.error!!){
                        val intent = Intent(applicationContext,Main_Menu::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                        Log.d("TestData",response.body()!!.user.tgl_lahir_user)
                    }else{
                        Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                    }
                }

            })
    }
}
