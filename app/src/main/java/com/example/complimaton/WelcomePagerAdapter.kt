package com.example.complimaton

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.databinding.ItemWelcomePageBinding

class WelcomePagerAdapter(private val context: Context) :
    RecyclerView.Adapter<WelcomePagerAdapter.WelcomeViewHolder>() {

    private val images = arrayOf(
        R.drawable.crazypoll,
        R.drawable.friends_compliment,
        R.drawable.getcompliment
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_welcome_page, parent, false)
        return WelcomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class WelcomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(imageResId: Int) {

            // Bind your image resource to ImageView in your item layout
            // For example:
             val imageView = itemView.findViewById<ImageView>(R.id.imageView)
             imageView.setImageResource(imageResId)



        }
    }
}