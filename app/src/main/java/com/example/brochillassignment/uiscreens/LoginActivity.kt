package com.example.brochillassignment.uiscreens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brochillassignment.R
import com.example.brochillassignment.databinding.ActivityLoginBinding
import com.example.brochillassignment.models.LoginRequest
import com.example.brochillassignment.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var token:String?=null
    private var userId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Submit button click listener
        binding.txt12Next.setOnClickListener {
            val email = binding.edt12Email.text.toString()
            val password = binding.edt12Password.text.toString()

            // Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                // Call the login function
                loginUser(email, password)
            }
        }
        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser(email: String, password: String) {
        // Use Coroutines to make the network call
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.RetrofitClient.apiService.loginUser(getToken(),LoginRequest(email, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    token=loginResponse?.token
                    Log.d("Token", "request token is $token")
                    userId=loginResponse?._id

                    Log.d("userLogin","user log in successfully")
                    // Handle success: Move to next screen or show success message
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Welcome ${loginResponse?.first_name}", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity,HomeScreenActivity::class.java))
                        finish()
                    }
                } else {
                    // Handle API error
                    Log.d("userLogin","user log in failed")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Handle network or other errors
                Log.d("userLogin","user log in error occurred ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
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