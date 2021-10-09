package com.tev.book_a_meal.Remote

import com.tev.book_a_meal.Model.MyResponse
import com.tev.book_a_meal.Model.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers("Content-Type:application/json", "Authorization:key=yourKey")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender?): Call<MyResponse?>
}