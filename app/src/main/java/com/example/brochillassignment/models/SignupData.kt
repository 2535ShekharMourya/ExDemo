package com.example.brochillassignment.models
data class RegisterRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String
)
data class RegisterResponse(
    val first_name: String,
    val last_name: String,
    val _id: String,
    val email: String,
    val token:String
)

