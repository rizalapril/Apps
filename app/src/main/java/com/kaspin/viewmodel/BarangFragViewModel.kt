package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kaspin.data.model.Barang
import com.kaspin.data.model.BarangDataClass
import com.kaspin.helper.SQLiteHelper

class BarangFragViewModel(application: Application): AndroidViewModel(application) {
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

    fun insertBarang(kdBarang: String, namaBarang: String, stockBarang: Int){
        val data = Barang(null, kdBarang, namaBarang, stockBarang)
        val status = sqLiteHelper.insertBarang(data)
        if (status > -1){
            loadBarangList()
        }else{

        }
    }

    fun updateBarang(kdBarang: String, namaBarang: String, stockBarang: Int){
        val data = BarangDataClass()
        data.kode_barang = kdBarang
        data.nama_barang = namaBarang
        data.stock = stockBarang

        val status = sqLiteHelper.updateBarang(data)
        if (status > -1){
            loadBarangList()
        }else{

        }
    }
}