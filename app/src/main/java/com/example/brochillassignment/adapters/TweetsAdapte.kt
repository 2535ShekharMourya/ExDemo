package com.example.brochillassignment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brochillassignment.models.Tweet
import android.widget.TextView
import com.example.brochillassignment.R
import com.example.brochillassignment.viewmodel.TweetViewModel

class TweetsAdapter(private val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.TweetViewHolder>() {
 fun deleteItem(i:Int){
     tweets.removeAt(i)
     notifyDataSetChanged()

 }
    fun addItem(i:Int,tweet:Tweet){
        tweets.add(i,tweet)
        notifyDataSetChanged()
    }
     class TweetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val textViewTweet: TextView = itemView.findViewById(R.id.txt_itemRv)

       /* fun bind(tweet: Tweet) {
            textViewTweet.text = tweet.tweet
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.tweet_items, parent, false)
        return TweetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        val current=tweets[position]
        holder.textViewTweet.text=current.tweet
        //holder.bind(tweets[position])
    }

    override fun getItemCount() = tweets.size
}
