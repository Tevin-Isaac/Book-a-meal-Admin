package com.tev.book_a_meal.ViewHolder

import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.Interface.ItemClickListener
import com.tev.book_a_meal.R

class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
    OnCreateContextMenuListener {
    var txtMenuName: TextView
    var imageView: ImageView
    private var itemClickListener: ItemClickListener? = null
    fun setItemClickListener(itemClickListener: ItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(view: View) {
        itemClickListener!!.onClick(view, adapterPosition, false)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        menu.setHeaderTitle("Select the action")
        menu.add(0, 0, adapterPosition, Common.UPDATE)
        menu.add(0, 1, adapterPosition, Common.DELETE)
    }

    init {
        txtMenuName = itemView.findViewById<View>(R.id.menu_name) as TextView
        imageView = itemView.findViewById<View>(R.id.menu_image) as ImageView
        itemView.setOnCreateContextMenuListener(this)
        itemView.setOnClickListener(this)
    }
}