package com.kaspin.view

import android.os.Bundle
import com.kaspin.R
import com.kaspin.base.BaseActivity
import com.kaspin.view.fragment.TransaksiFragment

class TransaksiActivity : BaseActivity(){
    val transaksi = TransaksiFragment()

    override fun getLayoutResourceId(): Int = R.layout.main_fragment

    override fun initView(savedInstanceState: Bundle?) {

        //default fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.contentDetail, transaksi, "detail")
                .commit()
        }
    }

    override fun initListener() {

    }

    override fun initObserver() {

    }
}