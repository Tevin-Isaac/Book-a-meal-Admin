package com.tev.book_a_meal.Remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IGeoCoordinates {
    @GET("maps/api/geocode/json?key=yourkey")
    fun getGeoCode(@Query("address") address: String?): Call<String?>

    @GET("maps/api/directions/json?key=yourkey")
    fun getDirections(
        @Query("origin") origin: String?,
        @Query("destination") destination: String?
    ): Call<String?>
}