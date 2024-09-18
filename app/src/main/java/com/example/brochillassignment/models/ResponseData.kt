package com.example.brochillassignment.models

data class Tweet(
    val tweet: String,
    val _id: String,
    val __v: String
)

data class TweetsResponse(
    val tweets: List<Tweet>
)