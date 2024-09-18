package com.example.brochillassignment.models

// Data class to represent the tweet request
data class TweetRequest(
    val tweet: String
)

// Data class to represent the tweet response
data class TweetResponse(
    val tweet: String,
    val _id: String,
    val __v: String
)
