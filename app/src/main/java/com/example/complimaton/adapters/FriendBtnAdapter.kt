package com.example.complimaton.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R
import com.squareup.picasso.Picasso

data class Friend(
    val id: String,
    val name: String,
    val profilePictureUrl: String
)

class FriendBtnAdapter(private val friendList: List<Friend>, private val onButtonClick: (String) -> Unit) :
    RecyclerView.Adapter<FriendBtnAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonImage: ImageView = itemView.findViewById(R.id.buttonImage)
        val buttonText: TextView = itemView.findViewById(R.id.buttonText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend_button, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friendList[position]
        Picasso.get().load(friend.profilePictureUrl).fit().into(holder.buttonImage)
        holder.buttonText.text = friend.name
        holder.itemView.setOnClickListener { onButtonClick(friend.id) }
    }

    override fun getItemCount(): Int = friendList.size
}
