package com.kaspin.view

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.base.BaseActivity
import com.kaspin.data.model.BarangDataClass
import com.kaspin.view.adapter.BarangAdapter
import com.kaspin.view.dialog.BarangDialog
import com.kaspin.viewmodel.BarangViewModel

class BarangActivity : BaseActivity(){
    private lateinit var barangAdapter: BarangAdapter
    lateinit var viewModel: BarangViewModel

    var vf: ViewFlipper? = null
    var barangDialog: BarangDialog? = null

    var btnBack: ConstraintLayout? = null
    var btnAdd: ConstraintLayout? = null
    var btnAddCircle: ConstraintLayout? = null
    var barangList: RecyclerView? = null
    var layoutDialog: ConstraintLayout? = null

    private var mLastClickTime: Long = 0

    override fun getLayoutResourceId(): Int = R.layout.activity_barang

    override fun initView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(BarangViewModel::class.java)

        vf = findViewById<ViewFlipper>(R.id.vfBarangContent)
        btnBack = findViewById<ConstraintLayout>(R.id.btnBackBarang)
        btnAdd = findViewById<ConstraintLayout>(R.id.btnAddMainBarang)
        btnAddCircle = findViewById<ConstraintLayout>(R.id.btnAddBarangBlank)
        layoutDialog = findViewById<ConstraintLayout>(R.id.layoutDialog)
        barangList = findViewById<RecyclerView>(R.id.recyclerView)
        barangList?.layoutManager = LinearLayoutManager(this)

        barangAdapter = BarangAdapter(this)
        barangList?.adapter = barangAdapter

        viewModel.init(this)
        viewModel.loadBarangList()
    }

    override fun initListener() {
        btnAdd?.setOnClickListener { v ->
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            openAddBarangDialog()
        }

        btnAddCircle?.setOnClickListener { v ->
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            openAddBarangDialog()
        }

        btnBack?.setOnClickListener { v ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun initObserver() {
        viewModel.resultBarangList.observe(this, Observer { result ->
            result?.let {
                if (it.size > 0) {
                    vf?.displayedChild = 1
                    btnAdd?.visibility = View.VISIBLE

                    barangAdapter.swap(it)
                    barangAdapter.notifyDataSetChanged()
                } else {
                    vf?.displayedChild = 0
                    btnAdd?.visibility = View.GONE
                }
            }
        })

        viewModel.resultInsert.observe(this, Observer { result ->
            result?.let {
                showNotification(it)
            }
        })

        viewModel.resultUpdate.observe(this, Observer { result ->
            result?.let {
                showNotification(it)
            }
        })

        viewModel.resultDelete.observe(this, Observer { result ->
            result?.let {
                showNotification(it)
            }
        })
    }

    fun dismissDialog(){
        layoutDialog?.visibility = View.GONE
    }

    fun openAddBarangDialog(){
        barangDialog = BarangDialog(this, (width * 0.8).toInt(), (height * 0.6).toInt(), false, null)
        barangDialog?.setCancelable(false)
        barangDialog?.show()
        layoutDialog?.visibility = View.VISIBLE
    }

    fun editBarang(data: BarangDataClass){
        barangDialog = BarangDialog(this, (width * 0.8).toInt(), (height * 0.6).toInt(), true, data)
        barangDialog?.setCancelable(false)
        barangDialog?.show()
        layoutDialog?.visibility = View.VISIBLE
    }

    fun addBarang(kdBarang: String, namaBarang: String, stockBarang: Int){
        viewModel.insertBarang(kdBarang, namaBarang, stockBarang)
    }

    fun updateBarang(idBarang: Int, kdBarang: String, namaBarang: String, stockBarang: Int){
        viewModel.updateBarang(idBarang, kdBarang, namaBarang, stockBarang)
    }

    fun deleteBarang(idBarang: Int){
        viewModel.deleteBarang(idBarang)
    }

    fun showNotification(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}