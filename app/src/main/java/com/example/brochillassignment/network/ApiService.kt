package com.example.brochillassignment.network

import com.example.brochillassignment.models.LoginRequest
import com.example.brochillassignment.models.LoginResponse
import com.example.brochillassignment.models.RegisterRequest
import com.example.brochillassignment.models.RegisterResponse
import com.example.brochillassignment.models.TweetRequest
import com.example.brochillassignment.models.TweetResponse
import com.example.brochillassignment.models.TweetsResponse
import com.example.brochillassignment.models.WelcomeResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService{
    @POST("register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun loginUser(@Header("Authorization",) token: String?, @Body request: LoginRequest): Response<LoginResponse>

    @POST("tweets")
    suspend fun postTweet(@Header("Authorization",) token: String?, @Body request: TweetRequest): Response<TweetResponse>

    @GET("tweets")
    suspend fun getTweets(@Header("Authorization",) token: String?, @Query("user_id") userId: String?): Response<TweetsResponse>

    @GET("welcome")
    suspend fun getWelcomeMessage(@Header("Authorization") token: String?, @Query("user_id") userId: String?): Response<WelcomeResponse>

}

