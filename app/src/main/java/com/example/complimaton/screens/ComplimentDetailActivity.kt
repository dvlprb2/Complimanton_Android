package com.example.complimaton.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.Compliment
import com.example.complimaton.adapters.ComplimentsAdapter
import com.example.complimaton.R
import com.example.complimaton.adapters.FriendsAdapter
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn

class ComplimentDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var complimentsAdapter: ComplimentsAdapter

    private val profileManager = ProfileManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compliment_detail)

        recyclerView = findViewById(R.id.complimentDetailRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        complimentsAdapter = ComplimentsAdapter(mutableListOf())
        recyclerView.adapter = complimentsAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager.VERTICAL
            )
        )

        // Fetch all compliments from the profile and update the adapter
        fetchCompliments()
    }

    private fun fetchCompliments() {
        val currentUser = GoogleSignIn.getLastSignedInAccount(this)

        currentUser?.id?.let {
            profileManager.getAllCompliments(it) { complimentsList ->
                runOnUiThread {
                    // Update the adapter with the fetched compliments
                    complimentsAdapter.updateCompliments(complimentsList.toMutableList())
                }
            }
        }
    }
}