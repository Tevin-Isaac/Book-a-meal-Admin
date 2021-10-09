package com.tev.book_a_meal.Common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.format.DateFormat
import com.tev.book_a_meal.Model.Request
import com.tev.book_a_meal.Model.User
import com.tev.book_a_meal.Remote.APIService
import com.tev.book_a_meal.Remote.FCMRetrofitClient
import com.tev.book_a_meal.Remote.IGeoCoordinates
import com.tev.book_a_meal.Remote.RetrofitClient
import java.util.*

object Common {
    const val SHIPPER_TABLE = "Shippers"
    const val ORDER_NEED_SHIP_TABLE = "OrdersNeedShip"
    const val Staff_TABLE = "User"
    var currentUser: User? = null
    var currentRequest: Request? = null
    var DISTANCE = ""
    var DURATION = ""
    var ESTIMATED_TIME = ""
    var topicName = "News"
    var PHONE_TEXT = "userPhone"
    const val INTENT_ACCOUNT = "Account"
    const val INTENT_FOOD_ID = "FoodId"
    const val UPDATE = "Update"
    const val DELETE = "Delete"
    const val REMOVE = "Delete"
    const val PICK_IMAGE_REQUEST = 71
    const val baseURL = "https://maps.googleapis.com/"
    const val fcmURL = "https://fcm.googleapis.com/"
    fun convertCodeToStatus(code: String): String {
        return if (code == "0") "Placed" else if (code == "1") "Preparing Orders" else if (code == "2") "Shipping" else "Delivered"
    }

    fun convertRole(code: String?): String {
        return if (code == "true") "Role:Staff" else if (code == "false") "Role:User" else if (code == "yes") "Role:Admin" else if (code == "no") "Role:Not Admin" else if (code == null) "Role:User" else "Role:User"
    }

    val fCMClient: APIService
        get() = FCMRetrofitClient.getClient(fcmURL)!!.create(
            APIService::class.java
        )
    val geoCodeService: IGeoCoordinates
        get() = RetrofitClient.getClient(baseURL)!!.create(
            IGeoCoordinates::class.java
        )

    fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val info = connectivityManager.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) return true
                }
            }
        }
        return false
    }

    fun scaleBitmap(bitmap: Bitmap?, newWidth: Int, newHeight: Int): Bitmap {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val scaleX = newWidth / bitmap!!.width.toFloat()
        val scaleY = newHeight / bitmap.height.toFloat()
        val pivotX = 0f
        val pivotY = 0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY)
        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, Paint(Paint.FILTER_BITMAP_FLAG))
        return scaledBitmap
    }

    fun getDate(time: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        val date = StringBuilder(
            DateFormat.format(
                "dd-MM-yyyy HH:mm", calendar
            ).toString()
        )
        return date.toString()
    }
}