package com.tev.book_a_meal

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.rengwuxian.materialedittext.MaterialEditText
import com.tev.book_a_meal.Model.*
import com.tev.book_a_meal.Remote.APIService
import com.tev.book_a_meal.SendMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SendMessage : AppCompatActivity() {
    var edtTitle: MaterialEditText? = null
    var edtMessage: MaterialEditText? = null
    var btnSubmit: FButton? = null
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
        setContentView(R.layout.activity_send_message)
        mService = Common.getFCMClient()
        edtTitle = findViewById<View>(R.id.edtTitle) as MaterialEditText
        edtMessage = findViewById<View>(R.id.edtMessage) as MaterialEditText
        btnSubmit = findViewById<View>(R.id.btnSubmit) as FButton
        btnSubmit!!.setOnClickListener { verifyTitleAndMessage() }
    }

    private fun verifyTitleAndMessage() {
        if (TextUtils.isEmpty(edtTitle!!.text.toString())) {
            Toast.makeText(this, "Title is empty", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(edtMessage!!.text.toString())) {
            Toast.makeText(this, "Message is empty!", Toast.LENGTH_SHORT).show()
        } else sendNotification()
    }

    private fun sendNotification() {
        val notification = Notification(
            edtTitle!!.text.toString(), edtMessage!!.text.toString()
        )
        val toTopic = Sender()
        toTopic.to = StringBuilder("/topics/").append(Common.topicName).toString()
        toTopic.notification = notification
        mService!!.sendNotification(toTopic).enqueue(object : Callback<MyResponse?> {
            override fun onResponse(call: Call<MyResponse?>, response: Response<MyResponse?>) {
                if (response.isSuccessful) Toast.makeText(
                    this@SendMessage,
                    "Message Sent!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onFailure(call: Call<MyResponse?>, t: Throwable) {
                Toast.makeText(this@SendMessage, "" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}