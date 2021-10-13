package com.kaspin.view.dialog

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kaspin.R
import com.kaspin.base.BaseDialog
import com.kaspin.data.model.BarangDataClass
import com.kaspin.util.CommonUtil
import com.kaspin.view.fragment.BarangFragment

class BarangDialog(var context: Activity, val fragment: Fragment?, val width: Int, val height: Int, val isEdit: Boolean, val data: BarangDataClass?): BaseDialog(context) {

    var kdBarang: EditText? = null
    var namaBarang: EditText? = null
    var stockBarang: EditText? = null
    var title: TextView? = null

    var btnCancel: LinearLayout? = null
    var btnSave: LinearLayout? = null

    override fun getLayoutResourceId(): Int = R.layout.barang_dialog

    override fun initView(savedInstanceState: Bundle?) {
        window?.setLayout(width, height)

        kdBarang = findViewById(R.id.kdbarangEdittext)
        namaBarang = findViewById(R.id.namabarangEdittext)
        stockBarang = findViewById(R.id.stockEdittext)
        title = findViewById(R.id.txtTitle)

        btnCancel = findViewById(R.id.btnCancelBarangDialog)
        btnSave = findViewById(R.id.btnSaveBarangDialog)

        clearTxt()
        if (!isEdit){
            title?.text = "Tambah Barang"
            kdBarang?.isEnabled = true
        }else{
            title?.text = "Update Barang"
            isEditMode()
        }
    }

    override fun initListener() {
        btnCancel?.setOnClickListener { v ->
            val parent = fragment as BarangFragment
            parent?.dismissDialog()
            dismiss()
        }

        btnSave?.setOnClickListener {
            val parent = fragment as BarangFragment
            var kdBarang = kdBarang?.text?.toString() ?: ""
            var namaBarang = namaBarang?.text?.toString() ?: ""
            var stockBarang = stockBarang?.text?.toString() ?: ""
            if (!kdBarang.isNullOrEmpty() || !namaBarang.isNullOrEmpty() || !stockBarang.isNullOrEmpty()){
                val isInt = CommonUtil.tryParseInt(stockBarang)
                if (isInt){
                    if (!isEdit){
                        parent?.addBarang(kdBarang, namaBarang, stockBarang.toInt())
                    }else{
                        data?.let {
                            if (data.nama_barang.equals(namaBarang) && data.stock.equals(stockBarang.toInt())){
                                Toast.makeText(context, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show()
                            }else{
                                parent?.updateBarang(data.id_barang, kdBarang, namaBarang, stockBarang.toInt())
                            }
                        }
                    }
                    parent?.dismissDialog()
                    dismiss()
                }else{
                    Toast.makeText(context, "Stock harus angka", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Mohon periksa kembali inputan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun clearTxt(){
        kdBarang?.setText("")
        namaBarang?.setText("")
        stockBarang?.setText("")
    }

    fun isEditMode(){
        kdBarang?.setText("${data?.kode_barang}")
        kdBarang?.isEnabled = false
        namaBarang?.setText("${data?.nama_barang}")
        stockBarang?.setText("${data?.stock}")
    }
}