package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kaspin.data.model.BarangDataClass
import com.kaspin.helper.SQLiteHelper

class TransaksiFragViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper

    val resultBarangList = MutableLiveData<List<BarangDataClass>>()

    fun init(context: Context){
        sqLiteHelper = SQLiteHelper(context)
    }

    fun loadBarangList(){
        val dataList = sqLiteHelper.getAllBarang()
        var newList = ArrayList<BarangDataClass>()
        if (dataList.size > 0){
            for (i in dataList){
                var data = BarangDataClass()
                data.id_barang = i.id_barang ?: 0
                data.kode_barang = i.kode_barang
                data.nama_barang = i.nama_barang
                data.stock = i.stock
                newList.add(data)
            }
        }
        resultBarangList.postValue(newList)
    }
}