package com.kaspin.view.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.data.model.BarangDataClass
import com.kaspin.view.BarangActivity
import com.kaspin.view.fragment.BarangFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.barang_list_item.view.*

class BarangAdapter(private val context: Activity): RecyclerView.Adapter<BarangAdapter.VH>() {

    private var listData: List<BarangDataClass>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.barang_list_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(listData?.get(position)!!, context)
    }

    override fun getItemCount(): Int {
        if (listData == null) return 0
        else return listData?.size!!
    }

    class VH(view: View): RecyclerView.ViewHolder(view){
        private var parent: Activity? = null

        val root = view.rootLayout
        val barangName = view.barangName
        val kodeBarang = view.kodeBarang
        val stockBarang = view.stockBarang
        val btnEdit = view.editBarang
        val btnDelete = view.deleteBarang

        fun bind(data: BarangDataClass, context: Activity){
            barangName.text = "${(data.nama_barang).capitalize()}"
            kodeBarang.text = "${data.kode_barang}"
            stockBarang.text = "Stock ${data.stock}"

            btnEdit.setOnClickListener { v ->
                parent = context as BarangActivity
                (parent as BarangActivity)?.editBarang(data)
            }

            btnDelete.setOnClickListener { v ->
                parent = context as BarangActivity
                (parent as BarangActivity)?.deleteBarang(data.id_barang)
            }
        }
    }

    fun swap(listData: List<BarangDataClass>?){
        this.listData = listData
    }
}