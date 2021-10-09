package com.tev.book_a_meal.ViewHolder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.R


class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var staffName: TextView
    var staffPassword: TextView
    var staffRole: TextView
    var btnDeleteAccount: Button
    var btnEditAccount: Button

    init {
        staffName = itemView.findViewById<View>(R.id.staff_name) as TextView
        staffPassword = itemView.findViewById<View>(R.id.staff_password) as TextView
        staffRole = itemView.findViewById<View>(R.id.staff_role) as TextView
        btnEditAccount = itemView.findViewById<View>(R.id.btnEditStaff) as Button
        btnDeleteAccount = itemView.findViewById<View>(R.id.btnDeleteStaff) as Button
    }
}