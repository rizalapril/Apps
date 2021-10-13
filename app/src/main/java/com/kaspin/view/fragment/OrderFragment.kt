package com.kaspin.view.fragment

import android.content.Context
import android.view.View
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.base.BaseFragment
import com.kaspin.data.network.Order
import com.kaspin.view.adapter.OrderAdapter
import com.kaspin.view.adapter.TransaksiAdapter
import com.kaspin.viewmodel.OrderFragViewModel
import org.greenrobot.eventbus.EventBus

class OrderFragment : BaseFragment() {
    private lateinit var orderAdapter: OrderAdapter
    lateinit var viewModel: OrderFragViewModel

    var vf: ViewFlipper? = null
    var btnBack: ConstraintLayout? = null
    var orderList: RecyclerView? = null

    override fun getLayoutResourceId(): Int = R.layout.fragment_order

    override fun initView(parent: View) {
        viewModel = ViewModelProvider(this).get(OrderFragViewModel::class.java)

        vf = parent.findViewById(R.id.vfOrderContent)
        btnBack = parent.findViewById(R.id.btnBackOrder)
        orderList = parent.findViewById(R.id.orderRecycleView)

        orderList?.layoutManager = LinearLayoutManager(activity as Context)
        orderAdapter = OrderAdapter(activity as Context, this)
        orderList?.adapter = orderAdapter

        viewModel.init(activity as Context)
        viewModel.loadData()
    }

    override fun initListener() {
        btnBack?.setOnClickListener { v ->
            var data = Order()
            data.isOpen = false
            EventBus.getDefault().post(data)
        }
    }

    override fun initObserver() {
        viewModel.resultOrderList.observe(this, Observer { result ->
            result?.let {
                if (it.size > 0) {
                    vf?.displayedChild = 1
                    orderAdapter.swap(it)
                    orderAdapter.notifyDataSetChanged()
                } else {
                    vf?.displayedChild = 0
                }
            }
        })
    }
}