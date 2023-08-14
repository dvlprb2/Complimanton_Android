package com.example.complimaton.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.Compliment
import com.example.complimaton.adapters.ComplimentsAdapter
import com.example.complimaton.R

class ComplimentDetailActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    val compliments = mutableListOf("You're amazing!")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compliment_detail)

        recyclerView = findViewById(R.id.complimentDetailRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val complimentsAdapter = ComplimentsAdapter(compliments)
        recyclerView.adapter = complimentsAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager.VERTICAL
            )
        )
    }
}