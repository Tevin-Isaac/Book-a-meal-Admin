package com.tev.book_a_meal

import android.content.Context
import android.icu.number.NumberFormatter.with
import android.media.Rating
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.tev.book_a_meal.Common.NumberOfFood
import com.tev.book_a_meal.ViewComment
import com.tev.book_a_meal.ViewHolder.ShowCommentViewHolder
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ViewComment : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var database: FirebaseDatabase? = null
    var ratingDb: DatabaseReference? = null
    var foodId = ""
    var adapter: FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) adapter!!.stopListening()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //add calligraphy
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/restaurant_font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        setContentView(R.layout.activity_show_comment)

        //Init SwipeRefreshLayout view
        swipeRefreshLayout = findViewById<View>(R.id.swipe_layout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (Common.isConnectedToInternet(baseContext)) loadComment(foodId) else {
                Toast.makeText(
                    baseContext,
                    "Please check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnRefreshListener
            }
        })

        //Default, load for first time
        swipeRefreshLayout!!.post(Runnable {
            if (Common.isConnectedToInternet(baseContext)) loadComment(foodId) else {
                Toast.makeText(
                    baseContext,
                    "Please check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
                return@Runnable
            }
        })

        //Firebase
        database = FirebaseDatabase.getInstance()
        ratingDb = database!!.getReference("Rating")
        recyclerView = findViewById<View>(R.id.recycler_comment) as RecyclerView
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        val options = FirebaseRecyclerOptions.Builder<Rating>()
            .setQuery(ratingDb!!, Rating::class.java)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
            override fun onBindViewHolder(
                holder: ShowCommentViewHolder,
                position: Int,
                model: Rating
            ) {
                holder.ratingBar.rating = model.rateValue.toFloat()
                holder.txtComment.text = model.comment
                holder.txtUserPhone.text = model.userPhone
                holder.txtFoodName.text = NumberOfFood.convertIdToName(model.foodId)
                Picasso.with(baseContext).load(model.image).into(holder.commentImage)
                holder.btnDeleteComment.setOnClickListener {
                    ConfirmDeleteDialog(
                        adapter!!.getRef(
                            position
                        ).key
                    )
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ShowCommentViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.show_comment_layout, parent, false)
                return ShowCommentViewHolder(view)
            }
        }
        loadComment(foodId)
    }

    private fun loadComment(foodId: String) {
        adapter!!.startListening()
        recyclerView!!.adapter = adapter
        swipeRefreshLayout!!.isRefreshing = false
    }

    private fun ConfirmDeleteDialog(key: String?) {
        val alertDialog =
            AlertDialog.Builder(this@ViewComment, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Confirm Delete?")
        val inflater = this.layoutInflater
        val confirm_delete_layout = inflater.inflate(R.layout.confirm_delete_layout, null)
        alertDialog.setView(confirm_delete_layout)
        alertDialog.setIcon(R.drawable.ic_delete_black_24dp)
        alertDialog.setPositiveButton("DELETE") { dialog, which ->
            dialog.dismiss()
            ratingDb!!.child(key!!).removeValue()
            Toast.makeText(this@ViewComment, "Comment Delete Successfully!", Toast.LENGTH_SHORT)
                .show()
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }
}