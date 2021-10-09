package com.tev.book_a_meal.ViewHolder


import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.Interface.ItemClickListener
import com.tev.book_a_meal.R

class ShipperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var shipper_name: TextView
    var shipper_phone: TextView
    var shipper_password: TextView
    var btn_edit: Button
    var btn_remove: Button
    private var itemClickListener: ItemClickListener? = null
    fun setItemClickListener(itemClickListener: ItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View) {
        itemClickListener!!.onClick(v, adapterPosition, false)
    }

    init {
        shipper_name = itemView.findViewById<View>(R.id.shipper_name) as TextView
        shipper_phone = itemView.findViewById<View>(R.id.shipper_phone) as TextView
        shipper_password = itemView.findViewById<View>(R.id.shipper_password) as TextView
        btn_edit = itemView.findViewById<View>(R.id.btnEditShipper) as Button
        btn_remove = itemView.findViewById<View>(R.id.btnDeleteShipper) as Button
    }
}