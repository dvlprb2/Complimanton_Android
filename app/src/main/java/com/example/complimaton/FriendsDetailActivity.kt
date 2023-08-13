package com.example.complimaton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class FriendsDetailActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    private val friends = listOf(
        "Micheal Scott",
        "Jim Herlpert",
        "Pam Besealy",
        "Andy",
        "Angela John"
        // Add more card items as needed
    )

    private lateinit var tempList : MutableList<String>
    private lateinit var adapter: FriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_detail)

        tempList = mutableListOf()

        recyclerView = findViewById(R.id.complimentDetailRV)
        val searchView = findViewById<EditText>(R.id.searchFriendText)
        setupRecyclerView(friends.toMutableList())


        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed
                val newText = s.toString()
                tempList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    friends.forEach{
                        if(it.lowercase(Locale.getDefault()).contains(searchText)){
                            tempList.add(it)
                        }
                    }
                    setupRecyclerView(tempList)
                }else{
                    tempList.clear()
                    setupRecyclerView(friends.toMutableList())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed
            }
        })

    }

    private fun onButtonClicked(position: Int) {
        // Create an AlertDialog.Builder instance
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Set the title and message
        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("Are you sure you want to remove this friend?")

        // Set negative button (Cancel) with click listener
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // Handle cancel button click
            dialog.dismiss()
        }

        // Set positive button (Unfriend) with click listener
        alertDialogBuilder.setPositiveButton("Unfriend") { dialog, which ->
            // Handle unfriend button click
            // Remove friend from the list

            adapter.removeItem(position)
            friends.toMutableList().removeAt(position)
            // Notify the adapter about the data change

            dialog.dismiss()

        }

        // Create and show the AlertDialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun setupRecyclerView(input: MutableList<String>) {

        adapter = FriendsAdapter(input) { position ->
            onButtonClicked(position)
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager.VERTICAL
            )
        )
    }
}