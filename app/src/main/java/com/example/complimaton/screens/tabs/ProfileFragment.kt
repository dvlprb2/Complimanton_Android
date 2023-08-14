package com.example.complimaton.screens.tabs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.Compliment
import com.example.complimaton.R
import com.example.complimaton.adapters.ComplimentsAdapter
import com.example.complimaton.managers.ProfileManager
import com.example.complimaton.screens.ComplimentDetailActivity
import com.example.complimaton.screens.FriendsDetailActivity
import com.example.complimaton.screens.welcome.WelcomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var googleSignInClient: GoogleSignInClient
    private val profileManager = ProfileManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = GoogleSignIn.getLastSignedInAccount(requireActivity())

        val profileImageView = getView()?.findViewById<ImageView>(R.id.profileImageView)
        val usernameTextView = getView()?.findViewById<TextView>(R.id.usernameTextView)
        val emailTextView = getView()?.findViewById<TextView>(R.id.emailTextView)
        val complimentsCountTextView = getView()?.findViewById<TextView>(R.id.complimentsCountTextView)
        val friendsCountTextView = getView()?.findViewById<TextView>(R.id.friendsCountTextView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        Picasso.get().load(currentUser?.photoUrl).fit().into(profileImageView)
        usernameTextView?.text = currentUser?.displayName
        emailTextView?.text = currentUser?.email

        currentUser?.let {
            profileManager.getComplimentsCount(it.id!!) { count ->
                complimentsCountTextView?.text = count.toString()
            }

            profileManager.getFriendsCount(it.id!!) { count ->
                friendsCountTextView?.text = count.toString()
            }

            profileManager.getTopCompliments(it.id!!) { topCompliments ->
                Log.d(TAG, topCompliments.toString())
                val complimentsAdapter = ComplimentsAdapter(topCompliments.toMutableList())
                recyclerView.adapter = complimentsAdapter
            }
        }




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
        private const val TAG = "ProfileFragment"
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
            Firebase.auth.signOut()
            googleSignInClient.signOut()
            val intent = Intent(activity, WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()

        }
        alertDialogBuilder.show()
    }
    fun onAddFriendButtonClick(view: View) {}

}