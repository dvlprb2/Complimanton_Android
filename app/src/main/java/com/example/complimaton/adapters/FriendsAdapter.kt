package com.example.complimaton.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R
import com.example.complimaton.managers.ProfileData
import com.example.complimaton.managers.ProfileManager
import com.squareup.picasso.Picasso
import java.util.Locale

data class FriendData(
    val id: String, // Document ID of the friend
    val name: String,
    val profilePictureUrl: String
)

class FriendsAdapter(
    private val context: Context,
    private val friendDataList: List<FriendData>,
    private val userId: String,
    private val profileManager: ProfileManager
) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>(), Filterable {

    private var filteredFriendDataList: MutableList<FriendData> = friendDataList.toMutableList()

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
        val friend = filteredFriendDataList[position]
        holder.nameTextView.text = friend.name

        Picasso.get().load(friend.profilePictureUrl).into(holder.profilePictureImageView)

        holder.removeFriendButton.setOnClickListener {
            val friendToRemove = filteredFriendDataList[position]
            removeFriendFromDatabase(friendToRemove.id)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()

                // Filter the original friendDataList based on the search constraint
                filteredFriendDataList = friendDataList.filter {
                    it.name.toLowerCase(Locale.ROOT).contains(filterPattern)
                }.toMutableList()

                val results = FilterResults()
                results.values = filteredFriendDataList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // Notify the adapter about the filtered list change
                notifyDataSetChanged()

                // Add a debug log to check the filtered data
                Log.d("FilterDebug", "Filtered Data: $filteredFriendDataList")
            }
        }
    }


    private fun removeFriendFromDatabase(friendId: String) {
        profileManager.removeFriendFromProfile(userId, friendId) { success ->
            if (success) {
                // Remove the friend from the list
                val index = filteredFriendDataList.indexOfFirst { it.id == friendId }
                if (index != -1) {
                    filteredFriendDataList.removeAt(index)
                    notifyItemRemoved(index)
                }


                Toast.makeText(context, "Friend removed successfully", Toast.LENGTH_SHORT).show()
                // Update the UI, e.g., remove the friend from the list and notify the adapter
            } else {
                Toast.makeText(context, "Error removing friend", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredFriendDataList.size
    }
}
