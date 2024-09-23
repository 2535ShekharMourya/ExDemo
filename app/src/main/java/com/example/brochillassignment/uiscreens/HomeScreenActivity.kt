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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brochillassignment.R
import com.example.brochillassignment.adapters.SwipeGesture
import com.example.brochillassignment.adapters.TweetsAdapter
import com.example.brochillassignment.databinding.ActivityHomeScreenBinding
import com.example.brochillassignment.models.Tweet
import com.example.brochillassignment.network.RetrofitInstance
import com.example.brochillassignment.viewmodel.TweetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var tweetViewModel: TweetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        tweetViewModel = ViewModelProvider(this).get(TweetViewModel::class.java)

        binding.logout.setOnClickListener {
            clearSharedPref()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        setupRecyclerView()
        fetchTweets()
        binding.addNewTweet.setOnClickListener {
            val intent=Intent(this,NewTweetActivity::class.java)
            startActivity(intent)
            finish()
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
    private fun fetchTweets() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("Request Token1","my Token is ${getToken()}")
                val response = RetrofitInstance.RetrofitClient.apiService.getTweets(getToken())
                if (response.isSuccessful) {

                    Log.d("Home Screen",response.body().toString())
                    withContext(Dispatchers.Main) {
                        if (tweetViewModel.tweetList == null) {
                            val tweetList: ArrayList<Tweet>? = response.body()
                            tweetList?.let {
                                // Reverse and store the list in ViewModel
                                tweetViewModel.tweetList = it.reversed() as ArrayList
                            }
                        }
                        // Use the list from ViewModel for the RecyclerView
                        tweetViewModel.tweetList?.let { reversedTweets ->
                            val adapter=TweetsAdapter(reversedTweets)
                            swipeGestureImp(adapter,reversedTweets)
                            binding.recyclerviewTweets.adapter = adapter
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

    private fun swipeGestureImp(adapter: TweetsAdapter,tweetList:ArrayList<Tweet>) {
      val swipegesture=object :SwipeGesture(this){
          override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
              when(direction){
                  ItemTouchHelper.LEFT ->{
                      adapter.deleteItem(viewHolder.adapterPosition)

                  }
                  ItemTouchHelper.RIGHT -> {
                      val archiveItem=tweetList[viewHolder.adapterPosition]
                      adapter.deleteItem(viewHolder.adapterPosition)
                      adapter.addItem(tweetList.size,archiveItem)
                  }
              }
          }
      }
        val touchHelper=ItemTouchHelper(swipegesture)
        touchHelper.attachToRecyclerView(binding.recyclerviewTweets)
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
    // Retrieve user id from SharedPreferences
    private fun clearSharedPref(){
        val sharedPreferences = getSharedPreferences("saveTokenAndIdLocally", Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


}