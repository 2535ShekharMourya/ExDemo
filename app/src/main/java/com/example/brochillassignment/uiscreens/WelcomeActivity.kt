package com.example.brochillassignment.uiscreens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brochillassignment.R
import com.example.brochillassignment.databinding.ActivityWelcomeBinding
import com.example.brochillassignment.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWelcomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Set click listener for the button
        binding.txtStartButton.setOnClickListener {
            fetchWelcomeMessage()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchWelcomeMessage() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Make the network request
                val response = RetrofitInstance.RetrofitClient.apiService.getWelcomeMessage(getToken(),getUserId())
                if (response.isSuccessful) {
                    val welcomeResponse = response.body()
                    // Update the UI on the main thread
                    Log.d("Welcome Screen","message retrieve successfully")
                    withContext(Dispatchers.Main) {
                        welcomeResponse?.let {
                            binding.txtWelcome.text = it.message
                        }
                        startActivity(Intent(this@WelcomeActivity,HomeScreenActivity::class.java))
                        finish()

                    }
                } else {
                    Log.d("Welcome Screen","Failed to retrieve message")
                    withContext(Dispatchers.Main) {
                        binding.txtWelcome.text = "Failed to retrieve message: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                Log.d("Welcome Screen","Error ${e.message}")
                withContext(Dispatchers.Main) {
                    binding.txtWelcome.text = "Error: ${e.message}"
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