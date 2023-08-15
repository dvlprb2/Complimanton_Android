package com.example.complimaton.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.adapters.FriendsAdapter
import com.example.complimaton.R
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import java.util.Locale

class FriendsDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FriendsAdapter
    private lateinit var profileManager: ProfileManager
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_detail)

        recyclerView = findViewById(R.id.friendsRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchEditText = findViewById(R.id.searchEditText)

        profileManager = ProfileManager()
        val currentUser = GoogleSignIn.getLastSignedInAccount(this)

        // Fetch friend data and set up the adapter
        currentUser?.id?.let {
            profileManager.getAllFriends(it) { friendDataList ->
                adapter = FriendsAdapter(this@FriendsDetailActivity, friendDataList, it, profileManager)
                recyclerView.adapter = adapter

                // Add text change listener to the search EditText
                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // Not needed in this context
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // Add a debug log to check if text changes trigger filtering
                        Log.d("TextWatcherDebug", "Search Text Changed: $s")

                        // Filter friend list based on search text
                        adapter.filter.filter(s)
                    }

                    override fun afterTextChanged(s: Editable?) {
                        // Not needed in this context
                    }
                })

            }
        }
    }
}