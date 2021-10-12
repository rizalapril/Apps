package com.kaspin.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kaspin.R
import com.kaspin.base.BaseActivity
import com.kaspin.view.fragment.BarangFragment
import com.kaspin.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.beranda_layout.*

class MainActivity : BaseActivity() {

    lateinit var viewModel: MainViewModel

    val barang = BarangFragment()


    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        defaultView(true)

        //default fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.contentDetail, barang, "barang")
                .commit()
        }
    }

    override fun initListener() {
        btnBarang.setOnClickListener { v ->
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, barang, "barang")
                .commit()
            defaultView(false)
        }

        btnTransaksi.setOnClickListener { v ->
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, barang, "barang")
                .commit()
            defaultView(false)
        }
    }

    override fun initObserver() {

    }

    fun defaultView(isDefault: Boolean){
        if (isDefault){
            //default
            vfBeranda.displayedChild = 0
        }else{
            vfBeranda.displayedChild = 1
        }
    }
}