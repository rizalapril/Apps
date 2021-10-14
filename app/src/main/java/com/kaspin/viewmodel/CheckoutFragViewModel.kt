package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kaspin.data.model.*
import com.kaspin.helper.SQLiteHelper
import com.kaspin.util.Constants
import com.kaspin.util.SharedPreferenceUtil

class CheckoutFragViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var sharedPref: SharedPreferenceUtil
    private lateinit var dbFirebase: FirebaseDatabase
    private lateinit var referance: DatabaseReference

    val resultCheckoutList = MutableLiveData<List<DetailTransaksiDataClass>>()
    val resultCheckoutSubmit = MutableLiveData<Boolean>()
    val resultSubmit = MutableLiveData<Boolean>()

    var flag = false

    fun init(context: Context){
        sqLiteHelper = SQLiteHelper(context)
        sharedPref = SharedPreferenceUtil(context)
        dbFirebase = FirebaseDatabase.getInstance()
        referance = dbFirebase.getReference("Transaksi")
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
        resultCheckoutList.value = newList
    }

    fun loadFromFirebase(data: HeaderOrderFirebaseDataClass?){
        var newList = ArrayList<DetailTransaksiDataClass>()
        data?.detail_order?.let { detail ->
            if (detail.size > 0){
                for (i in detail){
                    //check stock on barang tbl
                    var newData = DetailTransaksiDataClass()
                    newData.id_detail_transaksi = i.id_detail_transaksi
                    newData.id_barang = i.id_barang
                    newData.kode_barang = i.kode_barang
                    newData.nama_barang = i.nama_barang
                    newData.stock = i.stock

                    val barang = sqLiteHelper.getBarang(i.id_barang)
                    if (i.stock > barang.stock){
                        newData.flag = true
                        flag = true
                    }else{
                        newData.flag = false
                    }
                    newList.add(newData)
                }
            }
        }
        resultCheckoutList.value = newList
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

    fun saveOrder(){
        val id_detail_transaksi = sharedPref.getString(Constants.PREF_ID_DETAIL_TRANSAKSI)
        val dataCheckout = sqLiteHelper.getAllCheckout(id_detail_transaksi)

        if (dataCheckout.size > 0){
            val header = sqLiteHelper.getTransaksiHeader(id_detail_transaksi)
            val headerId = referance.push().key

            var data = HeaderOrderFirebaseDataClass()
            var arrList = ArrayList<DetailOrderFirebaseDataClass>()

            for (i in dataCheckout){
                var detail = DetailOrderFirebaseDataClass()
                detail.id_detail_transaksi = id_detail_transaksi
                detail.id_barang = i.id_barang
                detail.kode_barang = i.kode_barang
                detail.nama_barang = i.nama_barang
                detail.stock = i.stock
                arrList.add(detail)
            }
            data.id = headerId ?: ""
            data.id_transaksi = header.id_transaksi
            data.id_detail_transaksi = header.id_detail_transaksi
            data.nama_transaksi = header.nama_transaksi
            data.status = 2
            data.detail_order = arrList

            if (headerId != null){
                referance.child(headerId).setValue(data).addOnCompleteListener { v ->
                    Log.i("CheckoutFrag","Success save order into firebase")
                }
            }

            updateHeaderLocalTransaksi(header)
        }
    }
    fun updateHeaderLocalTransaksi(data: HeaderTransaksi){
        //clear checkout list and create new header()
        var newData = HeaderTransaksiDataClass()
        newData.id_transaksi = data.id_transaksi
        newData.id_detail_transaksi = data.id_detail_transaksi
        newData.nama_transaksi = data.nama_transaksi
        newData.status = 2

        val statusHeader = sqLiteHelper.updateHeaderTransaksi(newData)
        if (statusHeader > -1){
            Log.i("CheckoutFrag","statusHeader: ${statusHeader}")
        }else{

        }

        createNewHeader()
    }

    fun createNewHeader(){
        //create new header
        val lastRecord = sqLiteHelper.getLastRecordTransaksiHeader()

        var systemCurrent = System.currentTimeMillis()
        var data = HeaderTransaksiDataClass()
        data.id_transaksi = null
        data.id_detail_transaksi = "${systemCurrent}"
        data.nama_transaksi = "Order ${(lastRecord.id_transaksi ?: 0) + 1}"
        data.status = 1
        sqLiteHelper.insertHeaderTransaksi(data)
        sharedPref.put(Constants.PREF_ID_DETAIL_TRANSAKSI, "${systemCurrent}")

        loadCheckoutList()
    }
}