package com.example.complimaton.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R

class ComplimentsAdapter(private val compliments: MutableList<String>) : RecyclerView.Adapter<ComplimentsAdapter.ComplimentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplimentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_compliment, parent, false)
        return ComplimentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComplimentViewHolder, position: Int) {
        val compliment = compliments[position]
        holder.bind(compliment)
    }

    override fun getItemCount(): Int {
        return compliments.size
    }

    class ComplimentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val awesomeIcon: ImageView = itemView.findViewById(R.id.awsomeIconImageView)
        private val complimentText: TextView = itemView.findViewById(R.id.complimentTextView)

        fun bind(compliment: String) {
            complimentText.text = compliment
        }
    }
}
