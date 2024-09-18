package com.example.brochillassignment.uiscreens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brochillassignment.R
import com.example.brochillassignment.databinding.ActivityNewTweetBinding
import com.example.brochillassignment.models.TweetRequest
import com.example.brochillassignment.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewTweetActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewTweetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNewTweetBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val token = intent.getStringExtra("TOKENX")

        // Set up the button click listener
        binding.txtPost.setOnClickListener {
            val tweetText = binding.edtTweet.text.toString()

            // Validate input
            if (tweetText.isEmpty()) {
                Toast.makeText(this, "Tweet cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                // Call the function to post the tweet
                postTweet(token,tweetText)
            }
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun postTweet(token:String?,tweetText: String) {
        // Use Coroutines to make the network call
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("Request Token2","my Token is ${getToken()}")
                val response = RetrofitInstance.RetrofitClient.apiService.postTweet(getToken(),TweetRequest(tweetText))
                if (response.isSuccessful) {
                    val tweetResponse = response.body()
                    Log.d("New tweet","id -> ${tweetResponse?.tweet}, tweet -> ${tweetResponse?.tweet}, version -> ${tweetResponse?.__v}")

                    Log.d("New tweet","tweet posted successfully")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@NewTweetActivity, "Tweet posted successfully: ${tweetResponse?.tweet}", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@NewTweetActivity,HomeScreenActivity::class.java))
                        finish()
                    }
                } else {
                    Log.d("New tweet","Failed to post tweet")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@NewTweetActivity, "Failed to post tweet", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.d("New tweet","Error ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@NewTweetActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Retrieve token from SharedPreferences
    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("saveTokenAndIdLocally", Context.MODE_PRIVATE)
        return sharedPreferences.getString("TOKEN", null)
    }
    // Retrieve user id from SharedPreferences
    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("saveTokenAndIdLocally", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_ID", null)
    }
}