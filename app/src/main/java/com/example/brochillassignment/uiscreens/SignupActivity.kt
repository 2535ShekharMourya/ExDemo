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
import com.example.brochillassignment.databinding.ActivitySignupBinding
import com.example.brochillassignment.models.RegisterRequest
import com.example.brochillassignment.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    private var token:String?=null
    private var userId:String?=null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("saveTokenAndIdLocally", Context.MODE_PRIVATE)


        binding.txtNext1.setOnClickListener {
            // Get user input from EditText fields
            val firstName = binding.edtFirstName.text.toString().trim()
            val lastName = binding.edtLastName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            // Validate input
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call the function to make the POST request
            registerUser(firstName, lastName, email, password)
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
                val response = RetrofitInstance.RetrofitClient.apiService.getWelcomeMessage(getToken())
                if (response.isSuccessful) {
                    val welcomeResponse = response.body()
                    // Update the UI on the main thread
                    Log.d("Welcome Screen","message retrieve successfully")
                    withContext(Dispatchers.Main) {
                        welcomeResponse?.let {
                           val intent=Intent(this@SignupActivity,WelcomeActivity::class.java)
                            intent.putExtra("message",it.message)
                            startActivity(intent)
                            finish()
                        }


                    }
                } else {
                    Log.d("Welcome Screen","Failed to retrieve message")
                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@SignupActivity,HomeScreenActivity::class.java))
                        finish()
                    }
                }
            } catch (e: Exception) {
                Log.d("Welcome Screen","Error ${e.message}")
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@SignupActivity,HomeScreenActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        // Create request object
        val registerRequest = RegisterRequest(
            first_name = firstName,
            last_name = lastName,
            email = email,
            password = password
        )

        // Make API call using coroutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    RetrofitInstance.RetrofitClient.apiService.registerUser(registerRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Handle success
                        val registerResponse = response.body()
                        saveTokenAndId(registerResponse?.token, registerResponse?._id)
                        token=registerResponse?.token
                        Log.d("Token", "request token is $token")
                        userId=registerResponse?._id
                        Log.d("Id", "request id is $userId")
                        Log.d("userRegister","user register successfully")
                        Toast.makeText(
                            this@SignupActivity,
                            "User registered: ${registerResponse?.first_name}",
                            Toast.LENGTH_LONG
                        ).show()
                        fetchWelcomeMessage()
                    } else {
                        Log.d("userRegister","user not registered ")
                        // Handle failure
                        Toast.makeText(
                            this@SignupActivity,
                            "Registration failed: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("userRegister","Error ${e.message}")
                    Toast.makeText(this@SignupActivity, "Error: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    // Save token and id to SharedPreferences
    private fun saveTokenAndId(token: String?, userId: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN", token)
        editor.putString("USER_ID", userId)
        editor.apply() // or editor.commit() for synchronous saving
    }

    // Retrieve token from SharedPreferences
    private fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    // Retrieve user id from SharedPreferences
    private fun getUserId(): String? {
        return sharedPreferences.getString("USER_ID", null)
    }

}