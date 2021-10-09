package com.tev.book_a_meal.ViewHolder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.R

class ShowCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtUserPhone: TextView
    var txtComment: TextView
    var txtFoodName: TextView
    var ratingBar: RatingBar
    var commentImage: ImageView
    var btnDeleteComment: Button

    init {
        txtComment = itemView.findViewById<View>(R.id.comment) as TextView
        txtUserPhone = itemView.findViewById<View>(R.id.comment_user_phone) as TextView
        txtFoodName = itemView.findViewById<View>(R.id.comment_item_name) as TextView
        ratingBar = itemView.findViewById<View>(R.id.ratingBar) as RatingBar
        commentImage = itemView.findViewById<View>(R.id.comment_image) as ImageView
        btnDeleteComment = itemView.findViewById<View>(R.id.btnDeleteComment) as Button
    }
}