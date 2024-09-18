package com.example.brochillassignment.models

// Data class to hold the login request
data class LoginRequest(
    val email: String,
    val password: String
)

// Data class to hold the login response
data class LoginResponse(
    val first_name: String,
    val last_name: String,
    val _id: String,
    val email: String,
    val token: String
)