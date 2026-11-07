package com.marikhapp.marikhkasir.activities.transaksi.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.produk.ProdukObject
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.ProdukModels.Produk
import com.marikhapp.marikhkasir.models.kategoriModels.Kategori
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var customadapterdaftarproduk : ProdukPesananAdapter
    val arrayProduk = ArrayList<ProdukObject>()
    val container = ArrayList<ProdukObject>()
    val container2 = ArrayList<ProdukObject>()
    lateinit var dataPasser: OnDataPass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_transaksi, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var index = 0
        var namaTab = ""
        val gridview = view.findViewById<GridView>(R.id.gridviewdaftarproduk)
        val search = view.findViewById<EditText>(R.id.editTextSearchProdukPesanan)
        val refresh = view.findViewById<FloatingActionButton>(R.id.refreshListProdukTransaksi)
        val progressBar = view.findViewById<ConstraintLayout>(R.id.progressBarProdukTransaksi)
        val textViewfloat = view.findViewById<TextView>(R.id.textViewfloat)
        val context = view.context
        progressBar.visibility = View.VISIBLE
        pageViewModel.getIndex.observe(viewLifecycleOwner, Observer<Int> { index = it

            Client.instance.listkategori().enqueue(object : Callback<List<Kategori>> {
                override fun onFailure(call: Call<List<Kategori>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Kategori>>, response: Response<List<Kategori>>) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        val data = response.body()
                        val id_kat_produk: Int = data!![index].id_kat_produk
                        daftarproduk(id_kat_produk, gridview, refresh,progressBar,context,textViewfloat)
                    }
                }
            })
        })

        allproduk()

        search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s!!.toString().toLowerCase(Locale.getDefault()))
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s!!.toString().toLowerCase(Locale.getDefault()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                filter(s!!.toString().toLowerCase(Locale.getDefault()))
            }

        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun filter(text: CharSequence) {
        arrayProduk.clear()
        if(text.isNotEmpty()){
            for (item in container) {
                if (item.nama_produk.toLowerCase().contains(text)) {
                    arrayProduk.add(item)
                }
            }
        }else{
            arrayProduk.addAll(container2)
        }
        customadapterdaftarproduk.notifyDataSetChanged()
        return
    }

    private fun daftarproduk(id_kat_produk : Int,gridview:GridView,refresh: FloatingActionButton,progressBar:ConstraintLayout,context: Context,textViewfloat:TextView){
        progressBar.visibility = View.VISIBLE
        Client.instance.allproduk().enqueue(object : Callback<List<Produk>>{
            override fun onFailure(call: Call<List<Produk>>, t: Throwable) {}
            override fun onResponse(call: Call<List<Produk>>, response: Response<List<Produk>>) {
                if (response.isSuccessful){
                    val jumlahdata = response.body()
                    arrayProduk.clear()
                    container2.clear()
                    customadapterdaftarproduk = ProdukPesananAdapter(arrayProduk,context)
                    gridview.adapter = customadapterdaftarproduk
                    for(item in jumlahdata!!){
                       if (item.id_kat_produk == id_kat_produk){
                           arrayProduk.add(ProdukObject(item.id_produk,item.nama_produk,item.harga_produk,item.gbr_produk,item.id_kat_produk,item.diskon_produk,item.tgl_awal_diskon,item.tgl_akhir_diskon,item.diskon_olfood,item.tgl_awal_diskon_olfood,item.tgl_akhir_diskon_olfood))
                           container2.add(ProdukObject(item.id_produk,item.nama_produk,item.harga_produk,item.gbr_produk,item.id_kat_produk,item.diskon_produk,item.tgl_awal_diskon,item.tgl_akhir_diskon,item.diskon_olfood,item.tgl_awal_diskon_olfood,item.tgl_akhir_diskon_olfood))
                       }
                    }
                    customadapterdaftarproduk.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }else{
                    textViewfloat.visibility = View.VISIBLE
                    textViewfloat.isSelected = true
                    progressBar.visibility = View.GONE
                }
            }
        })

        refresh.setOnClickListener {
            textViewfloat.visibility = View.GONE
            textViewfloat.isSelected = false
            daftarproduk(id_kat_produk,gridview,refresh,progressBar,context,textViewfloat)
        }

        passData(gridview,arrayProduk,id_kat_produk)
    }

    private fun allproduk(){
        Client.instance.allproduk().enqueue(object : Callback<List<Produk>>{
            override fun onFailure(call: Call<List<Produk>>, t: Throwable) {}
            override fun onResponse(call: Call<List<Produk>>, response: Response<List<Produk>>) {
                val jumlahdata = response.body()
                arrayProduk.clear()
                container.clear()
                for(item in jumlahdata!!){
                    container.add(ProdukObject(item.id_produk,item.nama_produk,item.harga_produk,item.gbr_produk,item.id_kat_produk,item.diskon_produk,item.tgl_awal_diskon,item.tgl_akhir_diskon,item.diskon_olfood,item.tgl_awal_diskon_olfood,item.tgl_akhir_diskon_olfood))
                }
            }
        })
    }

    interface OnDataPass {
        fun onDataPass(gridview: GridView,arrayProduk:ArrayList<ProdukObject>,id_kat_produk: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    fun passData(gridview: GridView,arrayProduk:ArrayList<ProdukObject>,id_kat_produk: Int){
        dataPasser.onDataPass(gridview,arrayProduk,id_kat_produk)
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}