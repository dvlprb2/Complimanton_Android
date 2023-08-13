package com.example.complimaton

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileFragment : Fragment() {

    lateinit var recyclerView: RecyclerView

    val compliments = mutableListOf(
        Compliment("You're amazing!"),
        Compliment("You make the world better!"),
        Compliment("You make the world better!"),
        Compliment("You make the world better!"),
        Compliment("You make the world better!"),
        // Add more compliments as needed
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val complimentsAdapter = ComplimentsAdapter(compliments)
        recyclerView.adapter = complimentsAdapter
        val friendsCount: TextView = view.findViewById(R.id.friendsCountTextView)
        friendsCount.setOnClickListener {

            showFriendsPage()
        }
        val complimentCount: TextView = view.findViewById(R.id.complimentsCountTextView)
        complimentCount.setOnClickListener {
            showComplimentPage()
        }

        val logoutBtn = view.findViewById<ImageButton>(R.id.logoutBtn)
        logoutBtn.setOnClickListener(View.OnClickListener {
            onLogoutButtonClick()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showFriendsPage() {
        val intent = Intent(activity, FriendsDetailActivity::class.java)
        startActivity(intent)
    }

    private fun showComplimentPage() {
        val intent = Intent(activity, ComplimentDetailActivity::class.java)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    fun onLogoutButtonClick() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        // Set the title and message
        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("Are you sure you want to Logout?")

        // Set negative button (Cancel) with click listener
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // Handle cancel button click
            dialog.dismiss()
        }

        // Set positive button (Unfriend) with click listener
        alertDialogBuilder.setPositiveButton("Logout") { dialog, which ->

            val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            preferences.edit().putBoolean("is_first_time_login", true).apply()
            val intent = Intent(activity, WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()

        }
        alertDialogBuilder.show()
    }
    fun onAddFriendButtonClick(view: View) {}


}