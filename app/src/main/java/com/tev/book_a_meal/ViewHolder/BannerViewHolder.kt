package com.tev.book_a_meal.ViewHolder

import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.R

class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    OnCreateContextMenuListener {
    var banner_name: TextView
    var banner_image: ImageView
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        menu.setHeaderTitle("Select the action")
        menu.add(0, 0, adapterPosition, Common.UPDATE)
        menu.add(0, 1, adapterPosition, Common.DELETE)
    }

    init {
        banner_name = itemView.findViewById<View>(R.id.banner_name) as TextView
        banner_image = itemView.findViewById<View>(R.id.banner_image) as ImageView
        itemView.setOnCreateContextMenuListener(this)
    }
}