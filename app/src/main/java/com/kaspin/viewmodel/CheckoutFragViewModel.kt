package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kaspin.data.model.BarangDataClass
import com.kaspin.data.model.DetailTransaksiDataClass
import com.kaspin.data.model.HeaderTransaksiDataClass
import com.kaspin.helper.SQLiteHelper
import com.kaspin.util.Constants
import com.kaspin.util.SharedPreferenceUtil

class CheckoutFragViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var sharedPref: SharedPreferenceUtil

    val resultCheckoutList = MutableLiveData<List<DetailTransaksiDataClass>>()
    val resultCheckoutSubmit = MutableLiveData<Boolean>()
    val resultSubmit = MutableLiveData<Boolean>()

    var flag = false

    fun init(context: Context){
        sqLiteHelper = SQLiteHelper(context)
        sharedPref = SharedPreferenceUtil(context)
    }

    fun loadCheckoutList(){
        val id_detail_transaksi = sharedPref.getString(Constants.PREF_ID_DETAIL_TRANSAKSI)
        val dataCheckout = sqLiteHelper.getAllCheckout(id_detail_transaksi)
        flag = false

        var newList = ArrayList<DetailTransaksiDataClass>()
        if (dataCheckout.size > 0){
            for (i in dataCheckout){
                //check stock on barang tbl
                var data = DetailTransaksiDataClass()
                data.id_detail_transaksi = i.id_detail_transaksi
                data.id_barang = i.id_barang
                data.kode_barang = i.kode_barang
                data.nama_barang = i.nama_barang
                data.stock = i.stock

                val barang = sqLiteHelper.getBarang(i.id_barang)
                if (i.stock > barang.stock){
                    data.flag = true
                    flag = true
                }else{
                    data.flag = false
                }
                newList.add(data)
            }
            resultCheckoutSubmit.value = flag
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

    fun submitCheckout(){
        if (!flag){
            var success = true
            val id_detail_transaksi = sharedPref.getString(Constants.PREF_ID_DETAIL_TRANSAKSI)

            //update status header transaksi
            val header = sqLiteHelper.getTransaksiHeader(id_detail_transaksi)
            var data = HeaderTransaksiDataClass()
            data.id_transaksi = header.id_transaksi
            data.id_detail_transaksi = header.id_detail_transaksi
            data.nama_transaksi = header.nama_transaksi
            data.status = 0
            val statusHeader = sqLiteHelper.updateHeaderTransaksi(data)
            if (statusHeader > -1){
                Log.i("CheckoutFrag","statusHeader: ${statusHeader}")
            }else{
                success = false
            }

            //decrease stock barang
            val dataCheckout = sqLiteHelper.getAllCheckout(id_detail_transaksi)
            if (dataCheckout.size > 0){
                for (i in dataCheckout){
                    val barang = sqLiteHelper.getBarang(i.id_barang)
                    val updateStockBarang = sqLiteHelper.updateStockBarang(i.id_barang, (barang.stock - i.stock))
                    if (updateStockBarang > -1){
                        Log.i("CheckoutFrag","updateStockBarang: ${updateStockBarang}")
                    }else{
                        success = false
                    }
                }
            }

            resultSubmit.value = success
        }
    }
}