package com.kaspin.view

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.kaspin.R
import com.kaspin.base.BaseActivity
import com.kaspin.data.network.Checkout
import com.kaspin.data.network.Home
import com.kaspin.data.network.Order
import com.kaspin.data.network.TransaksiSuccess
import com.kaspin.view.fragment.CheckoutFragment
import com.kaspin.view.fragment.OrderFragment
import com.kaspin.view.fragment.SuccessFragment
import com.kaspin.view.fragment.TransaksiFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class TransaksiActivity : BaseActivity(){
    val transaksi = TransaksiFragment()
    val order = OrderFragment()
    val checkout = CheckoutFragment()
    val success = SuccessFragment()

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

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    public fun onCheckout(data: Checkout) {
        if(data.isOpen){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, checkout, "checkout")
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, transaksi, "transaksi")
                .commit()
        }
    }

    @Subscribe
    public fun onTransaksiSuccess(data: TransaksiSuccess) {
        if(data.isOpen){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, success, "success")
                .commit()
        }
    }

    @Subscribe
    public fun onHome(data: Home) {
        if(!data.isHome){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, transaksi, "transaksi")
                .commit()
        }
    }

    @Subscribe
    public fun onOrder(data: Order) {
        if(data.isOpen){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, order, "order")
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentDetail, transaksi, "transaksi")
                .commit()
        }
    }
}