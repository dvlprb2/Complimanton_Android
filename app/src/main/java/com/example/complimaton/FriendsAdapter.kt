package com.example.complimaton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(private val friends: MutableList<String>, private val onButtonClick: (Int) -> Unit) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
        holder.btn.setOnClickListener {
            onButtonClick(position)
        }

    }

    override fun getItemCount(): Int {
        return friends.size
    }

    public fun removeItem(position: Int){
        friends.removeAt(position)
        notifyItemRemoved(position)
    }

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val friendName: TextView = itemView.findViewById(R.id.friendNameTextView)
        val btn : ImageButton = itemView.findViewById(R.id.removeFriendBtn)
        fun bind(friend: String) {


            friendName.text = friend


        }
    }
}
