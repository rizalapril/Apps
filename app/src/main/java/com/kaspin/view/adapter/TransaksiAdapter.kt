package com.kaspin.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.data.model.BarangDataClass
import com.kaspin.view.BarangActivity
import com.kaspin.view.fragment.TransaksiFragment
import kotlinx.android.synthetic.main.transaksi_list_item.view.*

class TransaksiAdapter(private val context: Context, val fragment: Fragment?): RecyclerView.Adapter<TransaksiAdapter.VH>() {

    private var listData: List<BarangDataClass>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaksi_list_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(listData?.get(position)!!, context, fragment)
    }

    override fun getItemCount(): Int {
        if (listData == null) return 0
        else return listData?.size!!
    }

    class VH(view: View): RecyclerView.ViewHolder(view){
        private var parent: TransaksiFragment? = null

        val root = view.rootLayout
        val barangName = view.barangName
        val kodeBarang = view.kodeBarang
        val btnAddToCart = view.addToCart

        fun bind(data: BarangDataClass, context: Context, fragment: Fragment?){
            barangName.text = "${(data.nama_barang).capitalize()}"
            kodeBarang.text = "${data.kode_barang}"

            btnAddToCart.setOnClickListener { v ->
                parent = fragment as TransaksiFragment
                parent?.addToCart(data)
            }
        }
    }

    fun swap(listData: List<BarangDataClass>?){
        this.listData = listData
    }
}