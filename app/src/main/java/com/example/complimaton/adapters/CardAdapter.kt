package com.example.complimaton.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.CardItem
import com.example.complimaton.R
import com.example.complimaton.databinding.ItemInboxCardBinding


class CardAdapter(private val cardItems: List<CardItem>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    lateinit var binding : ItemInboxCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inbox_card, parent, false)
        binding = ItemInboxCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = cardItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = cardItems.size

    class CardViewHolder(var binding: ItemInboxCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardItem) {
            binding.messageTextView.text =  item.title
            binding.timeTextView.text = item.time
        }
    }
}
