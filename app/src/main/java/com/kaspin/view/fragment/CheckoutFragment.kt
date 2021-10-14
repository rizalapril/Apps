package com.kaspin.view.fragment

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.base.BaseFragment
import com.kaspin.data.model.DetailTransaksiDataClass
import com.kaspin.data.model.HeaderOrderFirebaseDataClass
import com.kaspin.data.network.Checkout
import com.kaspin.data.network.TransaksiSuccess
import com.kaspin.view.adapter.CheckoutAdapter
import com.kaspin.view.dialog.BarangDialog
import com.kaspin.view.dialog.NotifDialog
import com.kaspin.viewmodel.CheckoutFragViewModel
import org.greenrobot.eventbus.EventBus

class CheckoutFragment : BaseFragment() {
    private lateinit var checkoutAdapter: CheckoutAdapter
    lateinit var viewModel: CheckoutFragViewModel

    var vf: ViewFlipper? = null
    var btnBack: ConstraintLayout? = null
    var btnSubmit: ConstraintLayout? = null
    var btnSubmitLayout: ConstraintLayout? = null
    var btnSaveOrder: ConstraintLayout? = null
    var btnSaveOrderLayout: ConstraintLayout? = null
    var checkoutList: RecyclerView? = null
    var layoutDialog: ConstraintLayout? = null

    var notifDialog: NotifDialog? = null

    var isLoadFromFirebase = false
    var dataFromFirebase: HeaderOrderFirebaseDataClass? = null

    override fun getLayoutResourceId(): Int = R.layout.fragment_checkout

    override fun initView(parent: View) {
        viewModel = ViewModelProvider(this).get(CheckoutFragViewModel::class.java)

        vf = parent.findViewById(R.id.vfCheckoutContent)
        btnBack = parent.findViewById(R.id.btnBackCheckout)
        btnSubmit = parent.findViewById(R.id.btnSubmitCheckout)
        btnSubmitLayout = parent.findViewById(R.id.btnSubmitLayout)
        btnSaveOrder = parent.findViewById(R.id.btnSaveOrder)
        btnSaveOrderLayout = parent.findViewById(R.id.btnSaveOrderLayout)
        layoutDialog = parent.findViewById(R.id.layoutDialog)
        checkoutList = parent.findViewById(R.id.checkoutRecycleView)
        checkoutList?.layoutManager = LinearLayoutManager(activity as Context)

        notifDialog = NotifDialog(activity as Activity, this as Fragment, (width * 0.8).toInt(), (height * 0.4).toInt())
        notifDialog?.setCancelable(false)

        checkoutAdapter = CheckoutAdapter(activity as Context, this)
        checkoutList?.adapter = checkoutAdapter

        viewModel.init(activity as Context)
        if (!isLoadFromFirebase){
            viewModel.loadCheckoutList()
        }else{
            viewModel.loadFromFirebase(dataFromFirebase)
        }
    }

    override fun initListener() {
        btnBack?.setOnClickListener { v ->
            if (!isLoadFromFirebase){
                var data = Checkout()
                data.isOpen = false
                EventBus.getDefault().post(data)
            }else{
                notifDialog?.show()
                notifDialog?.setTextNotif("Anda saat ini sedang menggunakan data dari firebase.\nData akan hilang jika anda kembali.\nApakah anda yakin?")
                layoutDialog?.visibility = View.VISIBLE
            }
        }

        btnSubmit?.setOnClickListener { v ->
            if (!isLoadFromFirebase){
                viewModel.submitCheckout()
            }else{
                viewModel.submitCheckoutFirebase(dataFromFirebase)
            }
        }

        btnSaveOrder?.setOnClickListener { v ->
            if (!isLoadFromFirebase){
                viewModel.saveOrder()
            }else{
                showNotification("Data sudah tersimpan di firebase sebelumnya.")
            }
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
                        btnSaveOrderLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_tint))
                    }
                }else{
                    activity?.let { actvy ->
                        btnSubmitLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_green))
                        btnSaveOrderLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_green))
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

        viewModel.resultCheckoutFirebaseSubmit.observe(this, Observer { result ->
            result?.let {
                if (it){
                    activity?.let { actvy ->
                        btnSubmitLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_tint))
                        btnSaveOrderLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_tint))
                    }
                }else{
                    activity?.let { actvy ->
                        btnSubmitLayout?.setBackgroundTintList(ContextCompat.getColorStateList(actvy.applicationContext, R.color.color_green))
                    }
                }
            }
        })
    }

    fun deleteFromCheckout(data: DetailTransaksiDataClass){
        if (!isLoadFromFirebase){
            viewModel.deleteFromCheckout(data)
        }else{
            viewModel.deleteFromCheckoutFirebase(data)
        }
    }

    fun showNotification(text: String){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    fun cancelBtnDialog(){
        layoutDialog?.visibility = View.GONE
    }

    fun confirmBtnDialog(){
        layoutDialog?.visibility = View.GONE
        isLoadFromFirebase = false

        var data = Checkout()
        data.isOpen = false
        EventBus.getDefault().post(data)
    }
}