package com.kaspin.view.fragment

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.base.BaseFragment
import com.kaspin.data.model.BarangDataClass
import com.kaspin.view.MainActivity
import com.kaspin.view.adapter.TransaksiAdapter
import com.kaspin.viewmodel.TransaksiFragViewModel

class TransaksiFragment : BaseFragment() {
    private lateinit var transaksiAdapter: TransaksiAdapter
    lateinit var viewModel: TransaksiFragViewModel

    var vf: ViewFlipper? = null
    var btnBack: ConstraintLayout? = null
    var transaksiList: RecyclerView? = null
    var btnOrder: ConstraintLayout? = null

    override fun getLayoutResourceId(): Int = R.layout.fragment_transaksi

    override fun initView(parent: View) {
        viewModel = ViewModelProvider(this).get(TransaksiFragViewModel::class.java)

        vf = parent.findViewById(R.id.vfTransaksiContent)
        btnBack = parent.findViewById(R.id.btnBackTransaksi)
        btnOrder = parent.findViewById(R.id.btnPendingOrder)
        transaksiList = parent.findViewById(R.id.transaksiRecycleView)
        transaksiList?.layoutManager = LinearLayoutManager(activity as Context)

        transaksiAdapter = TransaksiAdapter(activity as Context, this)
        transaksiList?.adapter = transaksiAdapter

        viewModel.init(activity as Context)
        viewModel.loadBarangList()
        viewModel.createParentTransaksi()
    }

    override fun initListener() {
        btnOrder?.setOnClickListener { v ->

        }

        btnBack?.setOnClickListener { v ->
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun initObserver() {
        viewModel.resultBarangList.observe(this, Observer { result ->
            result?.let {
                if (it.size > 0) {
                    vf?.displayedChild = 1
                    transaksiAdapter.swap(it)
                    transaksiAdapter.notifyDataSetChanged()
                } else {
                    vf?.displayedChild = 0
                }
            }
        })
    }

    fun addToCart(data: BarangDataClass){
        viewModel.addToCart(data)
    }
}