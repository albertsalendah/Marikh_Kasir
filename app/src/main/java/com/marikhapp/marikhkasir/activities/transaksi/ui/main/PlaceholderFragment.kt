package com.marikhapp.marikhkasir.activities.transaksi.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.marikhapp.marikhkasir.R
import com.marikhapp.marikhkasir.activities.produk.ProdukAdapter
import com.marikhapp.marikhkasir.activities.produk.ProdukObject
import com.marikhapp.marikhkasir.api.Client
import com.marikhapp.marikhkasir.models.ProdukModels.Produk
import com.marikhapp.marikhkasir.models.kategoriModels.Kategori
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var customadapterdaftarproduk : ProdukAdapter
    val arrayProduk = ArrayList<ProdukObject>()

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
        val gridview = view.findViewById<GridView>(R.id.gridviewdaftarproduk)
        val context = view.context
        pageViewModel.getIndex.observe(this, Observer<Int> { index = it })

        Client.instance.listkategori().enqueue(object : Callback<List<Kategori>> {
            override fun onFailure(call: Call<List<Kategori>>, t: Throwable) {}
            override fun onResponse(call: Call<List<Kategori>>, response: Response<List<Kategori>>) {
                if (response.isSuccessful){
                    val id_kat_produk: Int = response.body()!![index].id_kat_produk
                    val kategoriproduct: String = response.body()!![index].nama_kat_produk
                    daftarproduk(id_kat_produk,kategoriproduct,gridview,context)
                }
            }
        })
        super.onViewCreated(view, savedInstanceState)
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

    private fun daftarproduk(id_kat_produk : Int,kategoriproduct:String,gridview:GridView,context: Context){
        val stats = "fragmentTransaksi"
        customadapterdaftarproduk = ProdukAdapter(arrayProduk,context,id_kat_produk.toString(),kategoriproduct,stats)
        Client.instance.daftarproduk(id_kat_produk).enqueue(object : Callback<List<Produk>>{
            override fun onFailure(call: Call<List<Produk>>, t: Throwable) {}
            override fun onResponse(call: Call<List<Produk>>, response: Response<List<Produk>>) {
                val jumlahdata = response.body()
                arrayProduk.clear()
                for(item in jumlahdata!!){
                    arrayProduk.add(ProdukObject(item.id_produk,item.nama_produk,item.harga_produk,item.gbr_produk))
                }
                gridview.adapter = customadapterdaftarproduk
                customadapterdaftarproduk.notifyDataSetChanged()
            }
        })
    }

}