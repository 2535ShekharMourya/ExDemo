package com.example.brochillassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brochillassignment.models.Tweet

class TweetViewModel: ViewModel() {
    // Holds the reversed tweet list
    var tweetList: ArrayList<Tweet>? = null
    private val _tweetListItemCount = MutableLiveData<Int>()
    val tweetListItemCount: LiveData<Int> get() = _tweetListItemCount

    // Method to update the item count
    fun updateTweetListItemCount(count: Int) {
        _tweetListItemCount.value = count
    }

}