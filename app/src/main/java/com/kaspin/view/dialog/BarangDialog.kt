package com.kaspin.view.dialog

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.kaspin.R
import com.kaspin.base.BaseDialog
import com.kaspin.data.model.BarangDataClass
import com.kaspin.util.CommonUtil
import com.kaspin.view.BarangActivity

class BarangDialog(var context: Activity, val width: Int, val height: Int, val isEdit: Boolean, val data: BarangDataClass?): BaseDialog(context) {

    var kdBarang: EditText? = null
    var namaBarang: EditText? = null
    var stockBarang: EditText? = null
    var title: TextView? = null

    var btnCancel: LinearLayout? = null
    var btnSave: LinearLayout? = null

    var parent: Activity? = null

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
            parent = context as BarangActivity
            (parent as BarangActivity)?.dismissDialog()
            dismiss()
        }

        btnSave?.setOnClickListener {
            parent = context as BarangActivity
            var kdBarang = kdBarang?.text?.toString() ?: ""
            var namaBarang = namaBarang?.text?.toString() ?: ""
            var stockBarang = stockBarang?.text?.toString() ?: ""

            if (!namaBarang.isNullOrBlank()){
                if (!kdBarang.isNullOrEmpty() || !stockBarang.isNullOrEmpty()){
                    val isInt = CommonUtil.tryParseInt(stockBarang)
                    if (isInt){
                        if (!isEdit){
                            (parent as BarangActivity)?.addBarang(kdBarang, namaBarang, stockBarang.toInt())
                        }else{
                            data?.let {
                                if (data.nama_barang.equals(namaBarang) && data.stock.equals(stockBarang.toInt())){
                                    Toast.makeText(context, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show()
                                }else{
                                    (parent as BarangActivity)?.updateBarang(data.id_barang, kdBarang,  namaBarang, stockBarang.toInt())
                                }
                            }
                        }
                        (parent as BarangActivity)?.dismissDialog()
                        dismiss()
                    }else{
                        Toast.makeText(context, "Stock harus angka", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context, "Mohon periksa kembali inputan", Toast.LENGTH_SHORT).show()
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