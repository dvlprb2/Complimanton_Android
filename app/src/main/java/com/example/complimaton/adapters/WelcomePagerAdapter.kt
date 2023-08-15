package com.example.complimaton.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R

class WelcomePagerAdapter(private val context: Context) :
    RecyclerView.Adapter<WelcomePagerAdapter.WelcomeViewHolder>() {

    private val images = arrayOf(
        R.drawable.crazypoll,
        R.drawable.friends_compliment,
        R.drawable.getcompliment
    )

    private val text = arrayOf(
        "Answer crazy polls about friends",
        "Get Compliments when you picked in a poll",
        "Find out what your friends are getting picked for"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_welcome_page, parent, false)
        return WelcomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        holder.bind(images[position], text[position])
    }

    override fun getItemCount(): Int = images.size

    inner class WelcomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(imageResId: Int, imageDesc: String) {

            // Bind your image resource to ImageView in your item layout
            // For example:
             val imageView = itemView.findViewById<ImageView>(R.id.imageView)
             val textView = itemView.findViewById<TextView>(R.id.imageDescription)
             imageView.setImageResource(imageResId)
            textView.text = imageDesc
        }
    }
}