package com.example.brochillassignment.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    object RetrofitClient {
        private const val BASE_URL = "https://wern-api.brochill.app/"
       /* private val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNjZlYTEyNjFmOTI4NmQwM2Q3N2VlNjdkIiwiZW1haWwiOiJzdXJhajM1NjVAZ21haWwuY29tIiwiaWF0IjoxNzI2NjE5NDQ2LCJleHAiOjE3MjY2MjY2NDZ9.ZIvbvWC7dRPZTGZBnDeuAYkv3oGwUJmbbd9DlS6tuv8")
                    .build()
                chain.proceed(request)
            }
            .build()*/

        val apiService: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}