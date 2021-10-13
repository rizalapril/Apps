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
import com.kaspin.data.model.DetailTransaksiDataClass
import com.kaspin.data.network.Checkout
import com.kaspin.view.adapter.CheckoutAdapter
import com.kaspin.viewmodel.CheckoutFragViewModel
import org.greenrobot.eventbus.EventBus

class CheckoutFragment : BaseFragment() {
    private lateinit var checkoutAdapter: CheckoutAdapter
    lateinit var viewModel: CheckoutFragViewModel

    var vf: ViewFlipper? = null
    var btnBack: ConstraintLayout? = null
    var checkoutList: RecyclerView? = null

    override fun getLayoutResourceId(): Int = R.layout.fragment_checkout

    override fun initView(parent: View) {
        viewModel = ViewModelProvider(this).get(CheckoutFragViewModel::class.java)

        vf = parent.findViewById(R.id.vfCheckoutContent)
        btnBack = parent.findViewById(R.id.btnBackCheckout)
        checkoutList = parent.findViewById(R.id.checkoutRecycleView)
        checkoutList?.layoutManager = LinearLayoutManager(activity as Context)

        checkoutAdapter = CheckoutAdapter(activity as Context, this)
        checkoutList?.adapter = checkoutAdapter

        viewModel.init(activity as Context)
        viewModel.loadCheckoutList()
    }

    override fun initListener() {
        btnBack?.setOnClickListener { v ->
            var data = Checkout()
            data.isOpen = false
            EventBus.getDefault().post(data)
        }
    }

    override fun initObserver() {
        viewModel.resultCheckoutList.observe(this, Observer { result ->
            result?.let {
                if (it.size > 0) {
                    vf?.displayedChild = 1
                    checkoutAdapter.swap(it)
                    checkoutAdapter.notifyDataSetChanged()
                } else {
                    vf?.displayedChild = 0
                }
            }
        })
    }

    fun deleteFromCheckout(data: DetailTransaksiDataClass){
        viewModel.deleteFromCheckout(data)
    }
}