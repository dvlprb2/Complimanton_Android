package com.example.complimaton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ComplimentDetailActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    val compliments = mutableListOf(
        Compliment("You're amazing!"),
        Compliment("You make the world better!"),
        Compliment("You make the world better!"),
        Compliment("You make the world better!"),
        Compliment("You make the world better!"),
        // Add more compliments as needed
    )


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