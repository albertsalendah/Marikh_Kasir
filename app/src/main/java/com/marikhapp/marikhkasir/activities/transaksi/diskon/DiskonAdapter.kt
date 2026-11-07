package com.marikhapp.marikhkasir.activities.transaksi.diskon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.diskon.DiskonObject
import com.marikhapp.marikhkasir.activities.transaksi.Transaksi
import kotlin.coroutines.coroutineContext

class DiskonAdapter(var listDiskon:ArrayList<DiskonObject>,val context: Context,val activity:Activity) : BaseAdapter() {
    private var inflator : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val arrayDiskon = ArrayList<Int>()
    var diskon = 0
    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = Holder()
        val view : View = inflator.inflate(R.layout.adapter_diskon_transaksi,null)
        holder.textDiskon = view.findViewById(R.id.textViewDiskonTransaksi) as TextView

        holder.textDiskon.text = listDiskon[position].deskripsi_disc
        view.setOnClickListener {
            diskon = listDiskon[position].nominal_disc
            val sharedPreferences: SharedPreferences = activity.getSharedPreferences("DISKON",Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putInt("saved_diskon",diskon)
            editor.apply()

               val intent = Intent(context,Transaksi::class.java)
               intent.putExtra("Diskon",diskon)
               activity.setResult(Activity.RESULT_OK,intent)
               activity.finish()
        }
        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listDiskon.size
    }
    class Holder{
        lateinit var textDiskon : TextView
    }
}