package com.kaspin.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.data.model.HeaderOrderFirebaseDataClass
import com.kaspin.view.fragment.OrderFragment
import kotlinx.android.synthetic.main.order_list_item.view.*

class OrderAdapter(private val context: Context, val fragment: Fragment?): RecyclerView.Adapter<OrderAdapter.VH>() {

    private var listData: List<HeaderOrderFirebaseDataClass>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(listData?.get(position)!!, context, fragment)
    }

    override fun getItemCount(): Int {
        if (listData == null) return 0
        else return listData?.size!!
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private var parent: OrderFragment? = null

        val root = view.rootLayout
        val orderName = view.orderName
        val idTransaksi = view.idTransaksi
        val btnDelete = view.deleteOrder
        val btnLoad = view.loadOrder

        fun bind(data: HeaderOrderFirebaseDataClass, context: Context, fragment: Fragment?) {
            orderName.text = "${(data.nama_transaksi).capitalize()}"
            idTransaksi.text = "${data.id_detail_transaksi}"

            btnDelete.setOnClickListener { v ->
                parent = fragment as OrderFragment
                parent?.deleteOrder(data)
            }

            btnLoad.setOnClickListener { v ->
                parent = fragment as OrderFragment
                parent?.loadOrder(data)
            }
        }
    }

    fun swap(listData: List<HeaderOrderFirebaseDataClass>?) {
        this.listData = listData
    }
}