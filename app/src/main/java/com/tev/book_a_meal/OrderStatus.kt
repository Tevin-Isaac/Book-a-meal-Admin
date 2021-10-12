package com.tev.book_a_meal

import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

import com.jaredrummler.materialspinner.MaterialSpinner
import com.tev.book_a_meal.Common.Common
import com.tev.book_a_meal.Model.*
import com.tev.book_a_meal.OrderStatus
import com.tev.book_a_meal.Remote.APIService
import com.tev.book_a_meal.TrackingOrder
import com.tev.book_a_meal.ViewHolder.OrderViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class OrderStatus : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
//    var adapter: RecyclerAdapter<Request?, OrderViewHolder?>? = null
//    var db: Database? = null
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
        setContentView(R.layout.activity_order_status)}}

        //database
//        db = Database.getInstance()
//        requests = db!!.getReference("Requests")
//
//        //Init service
//        mService = Common.getFCMClient()
//
//        //Init
//        recyclerView = findViewById<View>(R.id.listOrders) as RecyclerView
//        recyclerView!!.setHasFixedSize(true)
//        layoutManager = LinearLayoutManager(this)
//        recyclerView!!.layoutManager = layoutManager
//        loadOrders()
//    }
//
    private fun loadOrders() {
        val options = RecyclerOptions.Builder<Request>()
            .setQuery(requests!!, Request::class.java)
            .build()
        adapter = object : RecyclerAdapter<Request, OrderViewHolder>(options) {
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
        Database.getInstance().getReference(Common.SHIPPER_TABLE)
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
                Database.getInstance().getReference(Common.ORDER_NEED_SHIP_TABLE)
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
//                                if (response.body()!!.success == 1) {
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