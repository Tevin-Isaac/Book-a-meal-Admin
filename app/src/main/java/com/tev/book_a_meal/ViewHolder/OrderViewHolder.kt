package com.tev.book_a_meal.ViewHolder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.R

class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtOrderId: TextView
    var txtOrderStatus: TextView
    var txtOrderPhone: TextView
    var txtOrderAddress: TextView
    var txtOrderDate: TextView
    var txtOrderName: TextView
    var txtOrderPrice: TextView
    var btnEdit: Button
    var btnRemove: Button
    var btnDetail: Button
    var btnDirection: Button

    init {
        txtOrderId = itemView.findViewById<View>(R.id.order_id) as TextView
        txtOrderStatus = itemView.findViewById<View>(R.id.order_status) as TextView
        txtOrderPhone = itemView.findViewById<View>(R.id.order_phone) as TextView
        txtOrderAddress = itemView.findViewById<View>(R.id.order_address) as TextView
        txtOrderDate = itemView.findViewById<View>(R.id.order_date) as TextView
        txtOrderName = itemView.findViewById<View>(R.id.order_name) as TextView
        txtOrderPrice = itemView.findViewById<View>(R.id.order_price) as TextView
        btnEdit = itemView.findViewById<View>(R.id.btnEdit) as Button
        btnRemove = itemView.findViewById<View>(R.id.btnRemove) as Button
        btnDetail = itemView.findViewById<View>(R.id.btnDetail) as Button
        btnDirection = itemView.findViewById<View>(R.id.btnDirection) as Button
    }
}