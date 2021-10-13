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
import com.kaspin.view.fragment.BarangFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.barang_list_item.view.*

class BarangAdapter(private val context: Context, val fragment: Fragment?): RecyclerView.Adapter<BarangAdapter.VH>() {

    private var listData: List<BarangDataClass>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.barang_list_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(listData?.get(position)!!, fragment)
//        val data = listData?.get(position)!!
//
//        holder.barangName.text = "${(data.nama_barang).capitalize()}"
//        holder.kodeBarang.text = "${data.kode_barang}"
//        holder.stockBarang.text = "Stock ${data.stock}"
//
//        holder.btnEdit.setOnClickListener { v ->
//            barangParent = fragment as BarangFragment
//            barangParent?.editBarang(data)
//        }
//
//        holder.btnDelete.setOnClickListener { v ->
//            barangParent = fragment as BarangFragment
//            barangParent?.deleteBarang(data.kode_barang)
//        }
    }

    override fun getItemCount(): Int {
        if (listData == null) return 0
        else return listData?.size!!
    }

    class VH(view: View): RecyclerView.ViewHolder(view){
        private var barangParent: BarangFragment? = null

        val root = view.rootLayout
        val barangName = view.barangName
        val kodeBarang = view.kodeBarang
        val stockBarang = view.stockBarang
        val btnEdit = view.editBarang
        val btnDelete = view.deleteBarang

        fun bind(data: BarangDataClass, fragment: Fragment?){
            barangName.text = "${(data.nama_barang).capitalize()}"
            kodeBarang.text = "${data.kode_barang}"
            stockBarang.text = "Stock ${data.stock}"

            btnEdit.setOnClickListener { v ->
                barangParent = fragment as BarangFragment
                barangParent?.editBarang(data)
            }

            btnDelete.setOnClickListener { v ->
                barangParent = fragment as BarangFragment
                barangParent?.deleteBarang(data.id_barang)
            }
        }
    }

    fun swap(listData: List<BarangDataClass>?){
        this.listData = listData
    }
}