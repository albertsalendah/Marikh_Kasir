package com.marikhapp.marikhkasir.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        checkLoginStatus()
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
                    val arrayUser = response.body()
                    if(response.body() != null && !response.body()!!.error){
                        val intent = Intent(applicationContext,Main_Menu::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                        saveUserInfo(user,response.body()!!.user.id_user)
                    }else{
                        Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    private fun saveUserInfo(user: String,id : String){
        val preferences = getSharedPreferences("USER_NAME", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("user_name", user)
        editor.apply()
        val preferencesid = getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val editorid = preferencesid.edit()
        editorid.putString("user_id", id)
        editorid.apply()
    }

    private fun checkLoginStatus(){
        val preferences = getSharedPreferences("USER_NAME", Context.MODE_PRIVATE)
        val user_name = preferences.getString("user_name", "").toString()

        if (user_name.isNotEmpty()){
            val intent = Intent(applicationContext,Main_Menu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }else{
            return
        }

    }

}
