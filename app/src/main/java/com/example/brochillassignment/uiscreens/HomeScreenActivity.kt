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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brochillassignment.R
import com.example.brochillassignment.adapters.TweetsAdapter
import com.example.brochillassignment.databinding.ActivityHomeScreenBinding
import com.example.brochillassignment.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Get the token from the Intent
        val token = intent.getStringExtra("TOKEN")


        setupRecyclerView()
        fetchTweets(token)
        binding.addNewTweet.setOnClickListener {
            val intent=Intent(this,NewTweetActivity::class.java)
            intent.putExtra("TOKENX",token)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerviewTweets.apply {
            layoutManager = LinearLayoutManager(this@HomeScreenActivity)

        }
    }
    private fun fetchTweets(token:String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("Request Token1","my Token is ${getToken()}")
                val response = RetrofitInstance.RetrofitClient.apiService.getTweets(getToken())
                if (response.isSuccessful) {

                    val tweetsResponse = response.body()

                    Log.d("Home Screen",response.body().toString())
                    withContext(Dispatchers.Main) {
                        tweetsResponse?.let {
                            binding.recyclerviewTweets.adapter = TweetsAdapter(it)
                        }
                    }
                } else {
                    Log.d("Home Screen","failed user tweets retrieve")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeScreenActivity, "tweets fetch failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.d("Home Screen","Error ${e.message}")
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    // Handle exception
                    Toast.makeText(this@HomeScreenActivity, "Error ${e.message}", Toast.LENGTH_SHORT).show()
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