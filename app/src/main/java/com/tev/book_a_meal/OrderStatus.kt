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
import android.util.Log
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class OrderStatus : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: FirebaseRecyclerAdapter<Request?, OrderViewHolder?>? = null
    var db: FirebaseDatabase? = null
    var requests: DatabaseReference? = null
    var spinner: MaterialSpinner? = null
    var shipperSpinner: MaterialSpinner? = null
    var mService: APIService? = null
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
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
        setContentView(R.layout.activity_order_status)

        //Firebase
        db = FirebaseDatabase.getInstance()
        requests = db!!.getReference("Requests")

        //Init service
        mService = Common.getFCMClient()

        //Init
        recyclerView = findViewById<View>(R.id.listOrders) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        loadOrders()
    }

    private fun loadOrders() {
        val options = FirebaseRecyclerOptions.Builder<Request>()
            .setQuery(requests!!, Request::class.java)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            override fun onBindViewHolder(
                viewHolder: OrderViewHolder,
                position: Int,
                model: Request
            ) {
                viewHolder.txtOrderId.text = adapter!!.getRef(position).key
                viewHolder.txtOrderPhone.text = model.phone
                viewHolder.txtOrderAddress.text = model.address
                viewHolder.txtOrderStatus.text = Common.convertCodeToStatus(model.status)
                viewHolder.txtOrderDate.text = Common.getDate(
                    adapter!!.getRef(position).key!!.toLong()
                )
                viewHolder.txtOrderName.text = model.name
                viewHolder.txtOrderPrice.text = model.total

                //New event Button
                viewHolder.btnEdit.setOnClickListener {
                    showUpdateDialog(
                        adapter!!.getRef(position).key,
                        adapter!!.getItem(position)
                    )
                }
                viewHolder.btnRemove.setOnClickListener {
                    ConfirmDeleteDialog(
                        adapter!!.getRef(
                            position
                        ).key
                    )
                }
                viewHolder.btnDetail.setOnClickListener {
                    val orderDetail = Intent(this@OrderStatus, OrderDetail::class.java)
                    Common.currentRequest = model
                    orderDetail.putExtra("OrderId", adapter!!.getRef(position).key)
                    startActivity(orderDetail)
                }
                viewHolder.btnDirection.setOnClickListener {
                    val trackingOrder = Intent(this@OrderStatus, TrackingOrder::class.java)
                    Common.currentRequest = model
                    startActivity(trackingOrder)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.order_layout, parent, false)
                return OrderViewHolder(itemView)
            }
        }
        adapter.startListening()
        adapter.notifyDataSetChanged()
        recyclerView!!.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    private fun showUpdateDialog(key: String?, item: Request) {
        val alertDialog =
            AlertDialog.Builder(this@OrderStatus, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Update Order")
        alertDialog.setMessage("Please Choose Status")
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.update_order_layout, null)
        spinner = view.findViewById<View>(R.id.statusSpinner) as MaterialSpinner
        spinner!!.setItems("Placed", "Preparing Orders", "Shipping", "Delivered")
        shipperSpinner = view.findViewById<View>(R.id.shipperSpinner) as MaterialSpinner

        //load all shipper to spinner
        val shipperList: MutableList<String?> = ArrayList()
        FirebaseDatabase.getInstance().getReference(Common.SHIPPER_TABLE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (shipperSnapshot in dataSnapshot.children) shipperList.add(shipperSnapshot.key)
                    shipperSpinner!!.setItems(shipperList)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        alertDialog.setView(view)
        alertDialog.setPositiveButton("YES") { dialog, which ->
            dialog.dismiss()
            item.status = spinner!!.selectedIndex.toString()
            if (item.status == "2") {
                //copy item to table "OrdersNeedShip"
                FirebaseDatabase.getInstance().getReference(Common.ORDER_NEED_SHIP_TABLE)
                    .child(shipperSpinner!!.getItems<Any>()[shipperSpinner!!.selectedIndex].toString())
                    .child(key!!)
                    .setValue(item)
                requests!!.child(key).setValue(item)
                adapter!!.notifyDataSetChanged() //add to update item size
                sendOrderStatusToUser(key, item)
                sendOrderShipRequestToShipper(
                    shipperSpinner!!.getItems<Any>()[shipperSpinner!!.selectedIndex].toString(),
                    item
                )
            } else {
                requests!!.child(key!!).setValue(item)
                adapter!!.notifyDataSetChanged() //add to update item size
                sendOrderStatusToUser(key, item)
            }
        }
        alertDialog.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun sendOrderShipRequestToShipper(shipperPhone: String, item: Request) {
        val tokens = db!!.getReference("Tokens")
        tokens.child(shipperPhone)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val token = dataSnapshot.getValue(
                            Token::class.java
                        )

                        //make raw payload
                        val notification =
                            Notification("iDeliveryServer", "You have new order need ship")
                        val content = Sender(token!!.getToken(), notification)
                        mService!!.sendNotification(content).enqueue(object : Callback<MyResponse> {
                            override fun onResponse(
                                call: Call<MyResponse>,
                                response: Response<MyResponse>
                            ) {
                                if (response.body()!!.success == 1) {
                                    Toast.makeText(
                                        this@OrderStatus,
                                        "Sent to Shippers!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@OrderStatus,
                                        "Failed to send notification!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                Log.e("ERROR", t.message!!)
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun ConfirmDeleteDialog(key: String?) {
        val alertDialog =
            AlertDialog.Builder(this@OrderStatus, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Confirm Delete?")
        val inflater = this.layoutInflater
        val confirm_delete_layout = inflater.inflate(R.layout.confirm_delete_layout, null)
        alertDialog.setView(confirm_delete_layout)
        alertDialog.setIcon(R.drawable.ic_delete_black_24dp)
        alertDialog.setPositiveButton("DELETE") { dialog, which ->
            dialog.dismiss()
            requests!!.child(key!!).removeValue()
            Toast.makeText(this@OrderStatus, "Order Deleted Successfully!", Toast.LENGTH_SHORT)
                .show()
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
        adapter!!.notifyDataSetChanged()
    }

    private fun sendOrderStatusToUser(key: String?, item: Request) {
        val tokens = db!!.getReference("Tokens")
        tokens.child(item.phone)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val token = dataSnapshot.getValue(
                            Token::class.java
                        )

                        //make raw payload
                        val notification =
                            Notification("iDeliveryServer", "Your order $key was updated")
                        val content = Sender(token!!.getToken(), notification)
                        mService!!.sendNotification(content).enqueue(object : Callback<MyResponse> {
                            override fun onResponse(
                                call: Call<MyResponse>,
                                response: Response<MyResponse>
                            ) {
                                if (response.body()!!.success == 1) {
                                    Toast.makeText(
                                        this@OrderStatus,
                                        "Order was updated!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@OrderStatus,
                                        "Order was updated but failed to send notification!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                Log.e("ERROR", t.message!!)
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
}