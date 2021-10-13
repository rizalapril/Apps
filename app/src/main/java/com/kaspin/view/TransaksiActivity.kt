package com.kaspin.view

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.kaspin.R
import com.kaspin.base.BaseActivity
import com.kaspin.data.network.Checkout
import com.kaspin.view.fragment.CheckoutFragment
import com.kaspin.view.fragment.TransaksiFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class TransaksiActivity : BaseActivity(){
    val transaksi = TransaksiFragment()
    val checkout = CheckoutFragment()

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
}