package com.marikhapp.marikhkasir.activities.transaksi.ModalHarian

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.Main_Menu
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.DefaultResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ModalHarian : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
        setContentView(R.layout.activity_modal_harian)

        val comefrom = intent.getStringExtra("comefrom").toString()

        val editTextViewModal = findViewById<EditText>(R.id.editTextModalAwal)
        val buttonSimpanModal = findViewById<Button>(R.id.buttonSimpanModalAwal)

        buttonSimpanModal.setOnClickListener {
            val modalAwal = editTextViewModal.text.toString()
            inserModalAwal(modalAwal.toInt(),comefrom)
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun inserModalAwal(modalAwal : Int, comefrom:String){
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMdd")
        val tglhariini: String = formatter.format(date)

        Client.instance.insertSaldoHarian(tglhariini,modalAwal).enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Terjadi Kesalahan Saat Memuat Data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                    if (comefrom == "main_menu"){
                        val intent = Intent(this@ModalHarian, Transaksi::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }else if(comefrom == "transaksi"){
                        val intent = Intent(this@ModalHarian,Transaksi::class.java)
                        this@ModalHarian.setResult(Activity.RESULT_OK,intent)
                        this@ModalHarian.finish()
                    }
                }else{
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(applicationContext, jObjError.getString("message"), Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_OUTSIDE == event.action) {
            val intent = Intent(this,Transaksi::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            this.finish()
            return true
        }
        return super.onTouchEvent(event)
    }
}
