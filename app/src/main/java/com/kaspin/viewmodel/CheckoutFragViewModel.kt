package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kaspin.data.model.BarangDataClass
import com.kaspin.data.model.DetailTransaksiDataClass
import com.kaspin.helper.SQLiteHelper
import com.kaspin.util.Constants
import com.kaspin.util.SharedPreferenceUtil

class CheckoutFragViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var sharedPref: SharedPreferenceUtil

    val resultCheckoutList = MutableLiveData<List<DetailTransaksiDataClass>>()

    fun init(context: Context){
        sqLiteHelper = SQLiteHelper(context)
        sharedPref = SharedPreferenceUtil(context)
    }

    fun loadCheckoutList(){
        val id_detail_transaksi = sharedPref.getString(Constants.PREF_ID_DETAIL_TRANSAKSI)
        val dataList = sqLiteHelper.getAllCheckout(id_detail_transaksi)
        var newList = ArrayList<DetailTransaksiDataClass>()

        if (dataList.size > 0){
            for (i in dataList){
                var data = DetailTransaksiDataClass()
                data.id_detail_transaksi = i.id_detail_transaksi
                data.id_barang = i.id_barang
                data.kode_barang = i.kode_barang
                data.nama_barang = i.nama_barang
                data.stock = i.stock
                newList.add(data)
            }
        }

        resultCheckoutList.postValue(newList)
    }

    fun deleteFromCheckout(data: DetailTransaksiDataClass){
        val status = sqLiteHelper.deleteCheckout(data.id_detail_transaksi, data.id_barang)
        if (status > -1){
            loadCheckoutList()
        }else{

        }
    }
}