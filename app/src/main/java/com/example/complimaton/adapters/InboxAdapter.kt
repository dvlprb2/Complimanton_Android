package com.example.complimaton.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R
import com.google.firebase.Timestamp

data class InboxItem(
    val type: String,
    val message: String,
    val timestamp: Timestamp
) {
    fun convertTimestampToTimeSince(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val timestampMillis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000

        val timeDifference = currentTimeMillis - timestampMillis

        return when {
            timeDifference < DateUtils.MINUTE_IN_MILLIS -> "Just now"
            timeDifference < DateUtils.HOUR_IN_MILLIS -> {
                val minutesAgo = (timeDifference / DateUtils.MINUTE_IN_MILLIS).toInt()
                "$minutesAgo minutes ago"
            }

            timeDifference < DateUtils.DAY_IN_MILLIS -> {
                val hoursAgo = (timeDifference / DateUtils.HOUR_IN_MILLIS).toInt()
                "$hoursAgo hours ago"
            }

            else -> DateUtils.getRelativeTimeSpanString(
                timestampMillis,
                currentTimeMillis,
                DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL
            ).toString()
        }
    }
}


class InboxAdapter(private var inboxItems: List<InboxItem>) :
    RecyclerView.Adapter<InboxAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val inboxItem = inboxItems[position]

        // Update UI elements with inboxItem data
        if (inboxItem.type == "compliment") {
            holder.avatarImageView.setImageResource(R.drawable.bolt)
        } else {
            holder.avatarImageView.setImageResource(R.drawable.ic_person)
        }
        holder.messageTextView.text = inboxItem.message
        holder.timestampTextView.text =
            inboxItem.convertTimestampToTimeSince() // You need to define this method in your InboxItem class
    }

    override fun getItemCount(): Int {
        return inboxItems.size
    }

    fun updateData(newData: List<InboxItem>) {
        inboxItems = newData
        notifyDataSetChanged()
    }
}
