package com.kaspin.view.dialog

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kaspin.R
import com.kaspin.base.BaseDialog
import com.kaspin.view.fragment.CheckoutFragment

class NotifDialog(var context: Activity, var fragment: Fragment?, val width: Int, val height: Int): BaseDialog(context) {

    var txtInfo: TextView? = null
    var btnCancel: LinearLayout? = null
    var btnConfirm: LinearLayout? = null

    var parentFrag: CheckoutFragment? = null

    override fun getLayoutResourceId(): Int = R.layout.notification_dialog

    override fun initView(savedInstanceState: Bundle?) {
        window?.setLayout(width, height)

        txtInfo = findViewById(R.id.txtInformation)

        btnCancel = findViewById(R.id.btnCancelDialog)
        btnConfirm = findViewById(R.id.btnConfirmDialog)
    }

    override fun initListener() {
        btnCancel?.setOnClickListener { v ->
            parentFrag = fragment as CheckoutFragment
            parentFrag?.cancelBtnDialog()
            dismiss()
        }
        btnConfirm?.setOnClickListener { v ->
            parentFrag = fragment as CheckoutFragment
            parentFrag?.confirmBtnDialog()
            dismiss()
        }
    }

    fun setTextNotif(txt: String){
        txtInfo?.setText("${txt}")
    }
}