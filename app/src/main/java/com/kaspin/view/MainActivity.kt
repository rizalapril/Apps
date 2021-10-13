package com.kaspin.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kaspin.R
import com.kaspin.base.BaseActivity
import com.kaspin.view.fragment.BarangFragment
import com.kaspin.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    lateinit var viewModel: MainViewModel

    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun initListener() {
        btnBarang.setOnClickListener { v ->
            val intent = Intent(this, BarangActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnTransaksi.setOnClickListener { v ->
            val intent = Intent(this, BarangActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun initObserver() {

    }
}