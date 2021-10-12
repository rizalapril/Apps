package com.kaspin.base

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window

abstract class BaseDialog(var activity: Activity) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutResourceId())
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)

        initView(savedInstanceState)
        initListener()
    }

    abstract fun getLayoutResourceId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun initListener()
}