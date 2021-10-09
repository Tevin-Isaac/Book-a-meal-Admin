package com.tev.book_a_meal.ViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.Model.Order
import com.tev.book_a_meal.R

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView
    var quantity: TextView
    var price: TextView

    init {
        name = itemView.findViewById<View>(R.id.product_name) as TextView
        quantity = itemView.findViewById<View>(R.id.product_quantity) as TextView
        price = itemView.findViewById<View>(R.id.product_price) as TextView
    }
}

class OrderDetailAdapter(var myOrders: List<Order>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_detail_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = myOrders[position]
        holder.name.text = String.format("Name : %s", order.productName)
        holder.quantity.text = String.format("Quantity : %s", order.quantity)
        holder.price.text = String.format("Price : %s", order.price)
    }

    override fun getItemCount(): Int {
        return myOrders.size
    }
}