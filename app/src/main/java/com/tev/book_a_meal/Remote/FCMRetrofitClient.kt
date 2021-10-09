package com.tev.book_a_meal.Remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FCMRetrofitClient {
    private var retrofit: Retrofit? = null
    fun getClient(baseURL: String?): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}