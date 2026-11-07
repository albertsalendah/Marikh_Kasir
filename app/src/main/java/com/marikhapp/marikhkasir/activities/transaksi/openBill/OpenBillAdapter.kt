package com.marikhapp.marikhkasir.activities.transaksi.openBill

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marikhapp.marikhkasir.R

class HolderNama(itemView: View) : RecyclerView.ViewHolder(itemView){
     var checkBoxnamaOpenBill : CheckBox = itemView.findViewById(R.id.checBoxViewNamaOpenBill)
     var textViewNamaOpenBill : TextView = itemView.findViewById(R.id.textViewNamaOpenBill)
     var listTglOpenBill : RecyclerView = itemView.findViewById(R.id.listIDTRXOpenBill)
}

@SuppressLint("SetTextI18n")
class OpenBillAdapter(var arrayListNama:ArrayList<String>,val comefrom: String,val context: Context, val activity: Activity,var Checked : ArrayList<Boolean>,var listIDTRX : ArrayList<OpenBillObject>) :RecyclerView.Adapter<HolderNama>() {
    lateinit var adapterIDTrxOpenBill: AdapterIDTrxOpenBill
    private var selectedPosition : Int = -1
    var clickFrom = ""
    var numclick = 1
    override fun onBindViewHolder(holder: HolderNama, position: Int) {
        val listNama : String = arrayListNama[position]
        holder.textViewNamaOpenBill.text = listNama

        holder.textViewNamaOpenBill.tag = position
        holder.textViewNamaOpenBill.setOnClickListener {
            if (returnPos() != position){
                holder.textViewNamaOpenBill.tag = position
                numclick = 1
                checkListVisibility(holder.listTglOpenBill)
            }else{
                numclick = 0
                holder.textViewNamaOpenBill.tag = position
                checkListVisibility(holder.listTglOpenBill)
            }
            if (numclick == 1 && !Checked[position]){
                clickFrom = "listNama"
                setAdapterIDTRX(holder,listNama,clickFrom)
                numclick = 0
                getPos(it)
            }
        }
        holder.checkBoxnamaOpenBill.setOnClickListener {
            clickFrom = "checkBox"
            setAdapterIDTRX(holder,listNama,clickFrom)
            holder.listTglOpenBill.visibility = View.VISIBLE
        }
        holder.checkBoxnamaOpenBill.setOnCheckedChangeListener { _, isChecked ->
            Checked[position] = isChecked
        }
        holder.checkBoxnamaOpenBill.tag = position
        holder.checkBoxnamaOpenBill.isChecked = Checked[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderNama {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_open_bill, parent, false)
        return HolderNama(view)
    }

    override fun getItemCount(): Int {
        return arrayListNama.size
    }

    private fun checkListVisibility(v : View){
        if (v.visibility == View.GONE){
            v.visibility = View.VISIBLE
        }else{
            v.visibility = View.GONE
        }
    }

    private fun getPos(view: View){
        selectedPosition = view.tag as Int
    }
    private fun returnPos() : Int{
        if (selectedPosition != -1){
            return selectedPosition
        }
        return -1
    }
    private fun setAdapterIDTRX(holder: HolderNama,nama:String,clickFrom:String){
        val LayoutManager = LinearLayoutManager(context)
        holder.listTglOpenBill.layoutManager = LayoutManager
        val listLama = ArrayList<OpenBillObject>()
        val listBaru = ArrayList<OpenBillObject>()

        if (comefrom == "listhariini"){
            for (item in listIDTRX){
                if (item.nama == nama){
                    listBaru.add(item)
                }
            }
            adapterIDTrxOpenBill = AdapterIDTrxOpenBill(listBaru,"listhariini",context,activity,holder.checkBoxnamaOpenBill,holder.listTglOpenBill,clickFrom)
            holder.listTglOpenBill.adapter = adapterIDTrxOpenBill
            adapterIDTrxOpenBill.notifyDataSetChanged()
        }else{
            for (item in listIDTRX){
                if (item.nama == nama){
                    listLama.add(item)
                }
            }
            adapterIDTrxOpenBill = AdapterIDTrxOpenBill(listLama,"fulllist",context,activity,holder.checkBoxnamaOpenBill,holder.listTglOpenBill,clickFrom)
            holder.listTglOpenBill.adapter = adapterIDTrxOpenBill
            adapterIDTrxOpenBill.notifyDataSetChanged()
        }
    }
}
