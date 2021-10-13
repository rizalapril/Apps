package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kaspin.data.model.Barang
import com.kaspin.data.model.BarangDataClass
import com.kaspin.helper.SQLiteHelper

class BarangViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper

    val resultBarangList = MutableLiveData<List<BarangDataClass>>()
    val resultInsert = MutableLiveData<String>()
    val resultUpdate = MutableLiveData<String>()
    val resultDelete = MutableLiveData<String>()

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
            resultInsert.value = "success insert new barang"
        }else{
            resultInsert.value = "failed insert new barang"
        }
    }

    fun updateBarang(idBarang: Int, kdBarang: String, namaBarang: String, stockBarang: Int){
        val data = Barang()
        data.id_barang = idBarang
        data.kode_barang = kdBarang
        data.nama_barang = namaBarang
        data.stock = stockBarang

        val status = sqLiteHelper.updateBarang(data)
        if (status > -1){
            loadBarangList()
            resultUpdate.value = "success update barang"
        }else{
            resultUpdate.value = "failed update barang"
        }
    }

    fun deleteBarang(idBarang: Int){
        val status = sqLiteHelper.deleteBarang(idBarang)
        if (status > -1){
            loadBarangList()
            resultDelete.value = "success delete barang"
        }else{
            resultDelete.value = "failed delete barang"
        }
    }
}