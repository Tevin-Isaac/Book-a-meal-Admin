package com.tev.book_a_meal

import android.content.Context
import android.os.Bundle

import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tev.book_a_meal.Common.Common
import com.tev.book_a_meal.ViewHolder.OrderDetailAdapter
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class OrderDetail : AppCompatActivity() {
    var order_id: TextView? = null
    var order_phone: TextView? = null
    var order_address: TextView? = null
    var order_total: TextView? = null
    var order_comment: TextView? = null
    var order_id_value: String? = ""
    var lstFoods: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
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
        setContentView(R.layout.activity_order_detail)
        order_id = findViewById<View>(R.id.order_id) as TextView
        order_phone = findViewById<View>(R.id.order_phone) as TextView
        order_address = findViewById<View>(R.id.order_address) as TextView
        order_total = findViewById<View>(R.id.order_total) as TextView
        order_comment = findViewById<View>(R.id.order_comment) as TextView
        lstFoods = findViewById<View>(R.id.lstFoods) as RecyclerView
        lstFoods!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        lstFoods!!.layoutManager = layoutManager
        if (intent != null) {
            order_id_value = intent.getStringExtra("OrderId")

            //set value
            order_id!!.text = order_id_value
            order_phone!!.text = Common.currentRequest!!.phone
            order_total!!.text = Common.currentRequest!!.total
            order_address!!.text = Common.currentRequest!!.address
            order_comment!!.text = Common.currentRequest!!.comment
            val adapter = OrderDetailAdapter(Common.currentRequest!!.foods)
            adapter.notifyDataSetChanged()
            lstFoods!!.adapter = adapter
        }
    }
}