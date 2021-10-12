package com.tev.book_a_meal.Helper

//import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build
import com.tev.book_a_meal.R
//
//class NotificationHelper(base: Context?) : ContextWrapper(base) {
//    var manager: NotificationManager? = null
//        get() {
//            if (field == null) field = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            return field
//        }
//        private set
//
////    @TargetApi(Build.VERSION_CODES.O)
//    private fun createChannel() {
//        val iDeliveryChannel = NotificationChannel(
//            iDelivery_ID,
//            iDelivery_Name,
//            NotificationManager.IMPORTANCE_DEFAULT
//        )
//        iDeliveryChannel.enableLights(false)
//        iDeliveryChannel.enableVibration(true)
//        iDeliveryChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        manager!!.createNotificationChannel(iDeliveryChannel)
//    }
//
//    @TargetApi(Build.VERSION_CODES.O)
//    fun getiDeliveryChannelNotification(
//        title: String?,
//        body: String?,
//        contentIntent: PendingIntent?,
//        soundUri: Uri?
//    ): Notification.Builder {
//        return Notification.Builder(applicationContext, iDelivery_ID)
//            .setContentIntent(contentIntent)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setSmallIcon(R.mipmap.ic_launcher_round)
//            .setSound(soundUri)
//            .setAutoCancel(false)
//    }

//    companion object {
//        private const val iDelivery_ID = "com.tev.book_a_meal"
//        private const val iDelivery_Name = "ibookameal"
//    }
//
//    init {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //only working this if api is 26 or higher
//            createChannel()
//    }
//}