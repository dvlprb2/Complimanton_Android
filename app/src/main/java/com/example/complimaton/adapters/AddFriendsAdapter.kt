package com.example.complimaton.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R
import com.example.complimaton.managers.ProfileData
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.squareup.picasso.Picasso

data class AddFriendData(
    val id: String, // Document ID of the friend
    val name: String,
    val profilePictureUrl: String
)

class AddFriendsAdapter(
    private val context: Context,
    private val friendDataList: List<ProfileData>,
    private val profileManager: ProfileManager
) : RecyclerView.Adapter<AddFriendsAdapter.FriendViewHolder>() {

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePictureImageView: ImageView = itemView.findViewById(R.id.profilePictureImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val removeFriendButton: ImageButton = itemView.findViewById(R.id.removeFriendButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.removeFriendButton.setImageResource(R.drawable.ic_add_friend);

        val friend = friendDataList[position]
        holder.nameTextView.text = friend.name

        Picasso.get().load(friend.profilePictureUrl).into(holder.profilePictureImageView)

        holder.removeFriendButton.setOnClickListener {
            val currentUser = GoogleSignIn.getLastSignedInAccount(this.context)
            currentUser?.id?.let { it1 ->
                currentUser.displayName?.let { it2 ->
                    profileManager.addFriendAndUpdateInbox(it1, it2, friend.id) { success ->
                        if (success) {
                            Toast.makeText(context, "Added friend successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error adding friend", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return friendDataList.size
    }
}
