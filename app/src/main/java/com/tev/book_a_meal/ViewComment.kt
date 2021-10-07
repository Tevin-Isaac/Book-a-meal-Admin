package com.tev.book_a_meal

import com.tev.book_a_meal.Model.Order
import com.tev.book_a_meal.Remote.APIService
import com.tev.book_a_meal.Remote.FCMRetrofitClient
import com.tev.book_a_meal.Remote.IGeoCoordinates
import com.tev.book_a_meal.Remote.RetrofitClient
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.graphics.Bitmap
import org.json.JSONObject
import org.json.JSONArray
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import android.content.ContextWrapper
import android.app.NotificationManager
import android.annotation.TargetApi
import android.os.Build
import android.app.NotificationChannel
import com.tev.book_a_meal.Helper.NotificationHelper
import android.app.PendingIntent
import com.tev.book_a_meal.R
import retrofit2.http.POST
import com.tev.book_a_meal.Model.Sender
import com.tev.book_a_meal.Model.MyResponse
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Intent
import com.tev.book_a_meal.OrderStatus
import android.media.RingtoneManager
import com.tev.book_a_meal.MainActivity
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.NavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.tev.book_a_meal.ViewHolder.MenuViewHolder
import android.support.v7.widget.RecyclerView
import com.rengwuxian.materialedittext.MaterialEditText
import info.hoang8f.widget.FButton
import android.support.v4.widget.DrawerLayout
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.os.Bundle
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.content.DialogInterface
import android.support.design.widget.Snackbar
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.OnProgressListener
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import com.tev.book_a_meal.Interface.ItemClickListener
import com.tev.book_a_meal.FoodList
import android.view.ViewGroup
import android.support.v4.view.GravityCompat
import com.tev.book_a_meal.BannerActivity
import com.tev.book_a_meal.SendMessage
import com.tev.book_a_meal.ScrollingActivity
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.view.View.OnCreateContextMenuListener
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import com.tev.book_a_meal.ViewHolder.MyViewHolder
import com.tev.book_a_meal.Model.Food
import com.tev.book_a_meal.ViewHolder.FoodViewHolder
import com.tev.book_a_meal.ViewHolder.OrderDetailAdapter
import com.tev.book_a_meal.ViewHolder.OrderViewHolder
import com.jaredrummler.materialspinner.MaterialSpinner
import com.tev.book_a_meal.OrderDetail
import com.tev.book_a_meal.TrackingOrder
import android.text.TextUtils
import com.tev.book_a_meal.ViewHolder.ShowCommentViewHolder
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import com.tev.book_a_meal.Common.NumberOfFood
import com.tev.book_a_meal.SignInAsStaff
import com.tev.book_a_meal.SignInAsAdmin
import com.tev.book_a_meal.ViewHolder.UserViewHolder
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.tev.book_a_meal.MainAdminActivity
import com.tev.book_a_meal.Home
import android.support.v4.app.FragmentActivity
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.common.api.GoogleApiClient
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import android.graphics.BitmapFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.tev.book_a_meal.TrackingOrder.ParserTask
import kotlin.jvm.Synchronized
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.ConnectionResult
import android.os.AsyncTask
import android.view.View
import android.widget.*
import com.com.tev.book_a_meal.Common.DirectionJSONParser
import com.google.android.gms.maps.model.PolylineOptions
import com.com.tev.book_a_meal.Model.Banner
import com.tev.book_a_meal.ViewHolder.BannerViewHolder
import com.google.android.gms.tasks.OnCompleteListener
import com.tev.book_a_meal.ManageAccount
import com.tev.book_a_meal.ViewComment
import com.tev.book_a_meal.ShipperManagement
import com.tev.book_a_meal.AdminScrollingActivity
import com.tev.book_a_meal.Model.Shipper
import com.tev.book_a_meal.ViewHolder.ShipperViewHolder

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
        swipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener {
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