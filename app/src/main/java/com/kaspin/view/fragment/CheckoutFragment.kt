package com.kaspin.view.fragment

import android.content.Context
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.base.BaseFragment
import com.kaspin.data.model.DetailTransaksiDataClass
import com.kaspin.data.network.Checkout
import com.kaspin.data.network.TransaksiSuccess
import com.kaspin.view.adapter.CheckoutAdapter
import com.kaspin.viewmodel.CheckoutFragViewModel
import org.greenrobot.eventbus.EventBus

class CheckoutFragment : BaseFragment() {
    private lateinit var checkoutAdapter: CheckoutAdapter
    lateinit var viewModel: CheckoutFragViewModel

    var vf: ViewFlipper? = null
    var btnBack: ConstraintLayout? = null
    var btnSubmit: ConstraintLayout? = null
    var btnSubmitLayout: ConstraintLayout? = null
    var checkoutList: RecyclerView? = null

    override fun getLayoutResourceId(): Int = R.layout.fragment_checkout

    override fun initView(parent: View) {
        viewModel = ViewModelProvider(this).get(CheckoutFragViewModel::class.java)

        vf = parent.findViewById(R.id.vfCheckoutContent)
        btnBack = parent.findViewById(R.id.btnBackCheckout)
        btnSubmit = parent.findViewById(R.id.btnSubmitCheckout)
        btnSubmitLayout = parent.findViewById(R.id.btnSubmitLayout)
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

        btnSubmit?.setOnClickListener { v ->
            viewModel.submitCheckout()
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

        viewModel.resultCheckoutSubmit.observe(this, Observer { result ->
            result?.let {
                if (it){
                    activity?.let { actvy ->
                        btnSubmitLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_tint))
                    }
                }else{
                    activity?.let { actvy ->
                        btnSubmitLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_green))
                    }
                }
            }
        })

        viewModel.resultSubmit.observe(this, Observer { result ->
            result?.let {
                if (it){
                    var data = TransaksiSuccess()
                    data.isOpen = true
                    EventBus.getDefault().post(data)
                }else{
                    showNotification("failed submit")
                }
            }
        })
    }

    fun deleteFromCheckout(data: DetailTransaksiDataClass){
        viewModel.deleteFromCheckout(data)
    }

    fun showNotification(text: String){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}