package com.kaspin.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kaspin.R
import com.kaspin.data.model.BarangDataClass
import com.kaspin.data.model.DetailTransaksiDataClass
import com.kaspin.view.fragment.CheckoutFragment
import com.kaspin.view.fragment.TransaksiFragment
import kotlinx.android.synthetic.main.checkout_list_item.view.*

class CheckoutAdapter(private val context: Context, val fragment: Fragment?): RecyclerView.Adapter<CheckoutAdapter.VH>() {

    private var listData: List<DetailTransaksiDataClass>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_list_item, parent, false)
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
        private var parent: CheckoutFragment? = null

        val root = view.rootLayout
        val barangName = view.barangName
        val kodeBarang = view.kodeBarang
        val qtyBarang = view.stockBarang
        val txtInfo = view.infoStock
        val deleteBtn = view.deleteFromCheckout

        fun bind(data: DetailTransaksiDataClass, context: Context, fragment: Fragment?){
            barangName.text = "${(data.nama_barang).capitalize()}"
            kodeBarang.text = "${data.kode_barang}"
            qtyBarang.text = "Qty: ${data.stock}"

            deleteBtn.setOnClickListener { v ->
                parent = fragment as CheckoutFragment
                parent?.deleteFromCheckout(data)
            }

            if (data.flag){
                txtInfo.visibility = View.VISIBLE
                root.setBackgroundTintList(ContextCompat.getColorStateList(context.applicationContext, R.color.color_red))
            }else{
                txtInfo.visibility = View.GONE
                root.setBackgroundTintList(ContextCompat.getColorStateList(context.applicationContext, R.color.color_white))
            }
        }
    }

    fun swap(listData: List<DetailTransaksiDataClass>?){
        this.listData = listData
    }
}