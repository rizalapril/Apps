package com.kaspin.view.fragment

import android.content.Context
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.base.BaseFragment
import com.kaspin.data.model.BarangDataClass
import com.kaspin.view.MainActivity
import com.kaspin.view.adapter.BarangAdapter
import com.kaspin.view.dialog.BarangDialog
import com.kaspin.viewmodel.BarangFragViewModel

class BarangFragment: BaseFragment() {

    private lateinit var barangAdapter: BarangAdapter
    lateinit var viewModel: BarangFragViewModel

    var vf: ViewFlipper? = null
    var barangDialog: BarangDialog? = null

    var btnBack: ConstraintLayout? = null
    var btnAdd: ConstraintLayout? = null
    var btnAddCircle: ConstraintLayout? = null
    var barangList: RecyclerView? = null

    override fun getLayoutResourceId(): Int = R.layout.barang_fragment

    override fun initView(parent: View) {
        viewModel = ViewModelProvider(this).get(BarangFragViewModel::class.java)

        vf = parent.findViewById(R.id.vfBarangContent)
        btnBack = parent.findViewById(R.id.btnBackBarang)
        btnAdd = parent.findViewById(R.id.btnAddMainBarang)
        btnAddCircle = parent.findViewById(R.id.btnAddBarangBlank)
        barangList = parent.findViewById(R.id.recyclerView)
        barangList?.layoutManager = LinearLayoutManager(activity as Context)

        barangAdapter = BarangAdapter(activity as Context, this)
        barangList?.adapter = barangAdapter

        viewModel.init(activity as Context)
        viewModel.loadBarangList()

    }

    override fun initListener() {
        btnAdd?.setOnClickListener { v ->
            openAddBarangDialog()
        }

        btnAddCircle?.setOnClickListener { v ->
            openAddBarangDialog()
        }
    }

    override fun initObserver() {
        viewModel.resultBarangList.observe(this, Observer { result ->
            result?.let {
                if (it.size > 0){
                    vf?.displayedChild = 1
                    btnAdd?.visibility = View.VISIBLE

                    barangAdapter.swap(it)
                    barangAdapter.notifyDataSetChanged()
                }else{
                    vf?.displayedChild = 0
                    btnAdd?.visibility = View.GONE
                }
            }
        })
    }

    fun openAddBarangDialog(){
        val activity = activity as MainActivity
        barangDialog = BarangDialog(activity, this as Fragment, (width*0.8).toInt(), (height*0.6).toInt(), false, null)
        barangDialog?.setCancelable(false)
        barangDialog?.show()
    }

    fun editBarang(data: BarangDataClass){
        val activity = activity as MainActivity
        barangDialog = BarangDialog(activity, this as Fragment, (width*0.8).toInt(), (height*0.6).toInt(), true, data)
        barangDialog?.setCancelable(false)
        barangDialog?.show()
    }

    fun addBarang(kdBarang: String, namaBarang: String, stockBarang: Int){
        viewModel.insertBarang(kdBarang, namaBarang, stockBarang)
        Toast.makeText(context, "success insert new barang", Toast.LENGTH_SHORT).show()
    }

    fun updateBarang(kdBarang: String, namaBarang: String, stockBarang: Int){
        viewModel.updateBarang(kdBarang, namaBarang, stockBarang)
        Toast.makeText(context, "success update barang", Toast.LENGTH_SHORT).show()
    }
}