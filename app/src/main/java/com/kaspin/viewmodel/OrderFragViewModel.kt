package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.kaspin.data.model.BarangDataClass
import com.kaspin.data.model.DetailOrderFirebaseDataClass
import com.kaspin.data.model.HeaderOrderFirebaseDataClass
import com.kaspin.helper.SQLiteHelper

class OrderFragViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var dbFirebase: FirebaseDatabase
    private lateinit var referance: DatabaseReference

    val resultOrderList = MutableLiveData<List<HeaderOrderFirebaseDataClass>>()

    fun init(context: Context){
        sqLiteHelper = SQLiteHelper(context)
        dbFirebase = FirebaseDatabase.getInstance()
        referance = dbFirebase.getReference("Transaksi")
    }

    fun loadData(){
        referance.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var dataList = ArrayList<HeaderOrderFirebaseDataClass>()
                for (i in snapshot.children){
                    val data = i.getValue(HeaderOrderFirebaseDataClass::class.java)
                    dataList.add(data as HeaderOrderFirebaseDataClass)
                }

                resultOrderList.postValue(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel","${error}")
            }
        })
    }

    fun deleteOrder(data: HeaderOrderFirebaseDataClass){
        val deleteData = referance.child(data.id)
        deleteData.removeValue()
    }

}