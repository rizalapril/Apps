package com.kaspin.view.fragment

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.kaspin.R
import com.kaspin.base.BaseFragment
import com.kaspin.data.network.Checkout
import com.kaspin.data.network.Home
import com.kaspin.view.MainActivity
import org.greenrobot.eventbus.EventBus

class SuccessFragment: BaseFragment() {

    var btnHome: LinearLayout? = null
    var btnNewTransaksi: LinearLayout? = null

    override fun getLayoutResourceId(): Int = R.layout.fragment_success

    override fun initView(parent: View) {

        btnHome = parent.findViewById(R.id.btnToHome)
        btnNewTransaksi = parent.findViewById(R.id.btnNewTransaction)
    }

    override fun initListener() {
        btnHome?.setOnClickListener { v ->
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        btnNewTransaksi?.setOnClickListener { v ->
            var data = Home()
            data.isHome = false
            EventBus.getDefault().post(data)
        }
    }

    override fun initObserver() {

    }
}