package com.marikhapp.marikhkasir.activities.transaksi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()

    val text: LiveData<String> = Transformations.map(_index) {
        "$it"
    }

    val getIndex: LiveData<Int> = Transformations.map(_index) {
       it
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}