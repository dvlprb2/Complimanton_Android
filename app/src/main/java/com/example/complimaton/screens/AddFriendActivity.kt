package com.example.complimaton.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R
import com.example.complimaton.adapters.AddFriendsAdapter
import com.example.complimaton.adapters.FriendsAdapter
import com.example.complimaton.managers.ProfileData
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn

class AddFriendActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddFriendsAdapter
    private lateinit var profileManager: ProfileManager
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        val currentUser = GoogleSignIn.getLastSignedInAccount(this)

        recyclerView = findViewById(R.id.friendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        profileManager = ProfileManager()

        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailQuery = s.toString()
                currentUser?.id?.let { currentUserId ->
                    profileManager.searchProfilesByEmail(emailQuery, currentUserId) { profiles ->
                        println(profiles)
                        updateRecyclerView(profiles)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed for this implementation
            }
        })
    }

    private fun updateRecyclerView(profiles: List<ProfileData>) {
        val currentUser = GoogleSignIn.getLastSignedInAccount(this)
        val adapter = currentUser?.id?.let {
            AddFriendsAdapter(this@AddFriendActivity, profiles,
                it, profileManager)
        }
        recyclerView.adapter = adapter
    }

}