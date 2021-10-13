package com.kaspin.view.fragment

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.base.BaseFragment
import com.kaspin.data.model.BarangDataClass
import com.kaspin.view.MainActivity
import com.kaspin.view.adapter.BarangAdapter
import com.kaspin.view.dialog.BarangDialog
import com.kaspin.viewmodel.BarangFragViewModel

class BarangFragment: BaseFragment() {

    override fun getLayoutResourceId(): Int = R.layout.barang_fragment

    override fun initView(parent: View) {


    }

    override fun initListener() {

    }

    override fun initObserver() {

    }

}