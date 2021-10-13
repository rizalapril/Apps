package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kaspin.data.model.BarangDataClass
import com.kaspin.data.model.DetailTransaksiDataClass
import com.kaspin.data.model.HeaderTransaksiDataClass
import com.kaspin.helper.SQLiteHelper
import com.kaspin.util.Constants
import com.kaspin.util.SharedPreferenceUtil

class TransaksiFragViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var sharedPref: SharedPreferenceUtil

    val resultBarangList = MutableLiveData<List<BarangDataClass>>()
    val resultInsertDetail = MutableLiveData<String>()
    val resultUpdateDetail = MutableLiveData<String>()
    val resultCheckoutBtn = MutableLiveData<Int>()

    fun init(context: Context){
        sqLiteHelper = SQLiteHelper(context)
        sharedPref = SharedPreferenceUtil(context)
    }

    fun loadBarangList(){
        val dataList = sqLiteHelper.getAllBarangWithStock()
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

    fun createParentTransaksi(){
        val lastRecord = sqLiteHelper.getLastRecordTransaksiHeader()
        if (lastRecord.id_transaksi == 0){
            //create init header
            var systemCurrent = System.currentTimeMillis()
            var data = HeaderTransaksiDataClass()
            data.id_transaksi = null
            data.id_detail_transaksi = "${systemCurrent}"
            data.nama_transaksi = "Order 1"
            data.status = 1
            sqLiteHelper.insertHeaderTransaksi(data)
            sharedPref.put(Constants.PREF_ID_DETAIL_TRANSAKSI, "${systemCurrent}")
        }else{
            val isActive = lastRecord.status
            if (isActive == 1){
                //continue
                    sharedPref.put(Constants.PREF_ID_DETAIL_TRANSAKSI, lastRecord.id_detail_transaksi)
            }else{
                //create new header
                var systemCurrent = System.currentTimeMillis()
                var data = HeaderTransaksiDataClass()
                data.id_transaksi = null
                data.id_detail_transaksi = "${systemCurrent}"
                data.nama_transaksi = "Order ${lastRecord.id_transaksi ?: 0 + 1}"
                data.status = 1
                sqLiteHelper.insertHeaderTransaksi(data)
                sharedPref.put(Constants.PREF_ID_DETAIL_TRANSAKSI, "${systemCurrent}")
            }
        }
    }

    fun addToCart(data: BarangDataClass){
        val id_detail_transaksi = sharedPref.getString(Constants.PREF_ID_DETAIL_TRANSAKSI)
        //check isHas
        val detailTransaksi = sqLiteHelper.getDetailTransaksi(id_detail_transaksi, data.id_barang)
        if (detailTransaksi.id_detail_transaksi.isNullOrEmpty()){
            //insert
            val data = DetailTransaksiDataClass(id_detail_transaksi, data.id_barang, data.kode_barang, data.nama_barang, 1)
            val status = sqLiteHelper.insertDetailTransaksi(data)
            if (status > -1){
                updateBtnCheckout()
                resultInsertDetail.value = "success add to cart"
            }else{
                resultInsertDetail.value = "failed add to cart"
            }
        }else{
            //update
            val data = DetailTransaksiDataClass(id_detail_transaksi, data.id_barang, data.kode_barang, data.nama_barang, detailTransaksi.stock + 1)
            val status = sqLiteHelper.updateDetailTransaksi(data)
            if (status > -1){
                updateBtnCheckout()
                resultUpdateDetail.value = "success increase stock in cart"
            }else{
                resultUpdateDetail.value = "failed increase stock in cart"
            }
        }
    }

    fun updateBtnCheckout(){
        val id_detail_transaksi = sharedPref.getString(Constants.PREF_ID_DETAIL_TRANSAKSI)

        val detailTransaksi = sqLiteHelper.getAllCheckout(id_detail_transaksi)
        resultCheckoutBtn.value = detailTransaksi.size
    }
}