package com.kaspin.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.kaspin.helper.SQLiteHelper

class MainViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var sqLiteHelper: SQLiteHelper

    fun init(context: Context){
        sqLiteHelper = SQLiteHelper(context)
    }
}