package com.kaspin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    var width = 0
    var height = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutResourceId(), container, false)

        width = resources.displayMetrics.widthPixels
        height = resources.displayMetrics.heightPixels

        initView(view)
        initListener()
        initObserver()

        return view
    }

    abstract fun getLayoutResourceId(): Int

    abstract fun initView(parent: View)

    abstract fun initListener()

    abstract fun initObserver()
}
