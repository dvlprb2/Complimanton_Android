package com.example.complimaton.screens.tabs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R
import com.example.complimaton.adapters.FriendBtnAdapter
import com.example.complimaton.managers.ComplimentManager
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.Timestamp

class HomeFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var complimentTextView: TextView
    private lateinit var skipButton: Button

    private lateinit var constraintLayout: ConstraintLayout
    private val backgroundColors = listOf(
        Color.parseColor("#FF5733"),
        Color.parseColor("#FFC300"),
        Color.parseColor("#33FF57"),
        Color.parseColor("#339CFF"),
        Color.parseColor("#B833FF")
    )

    private val profileManager = ProfileManager()
    private val complimentManager = ComplimentManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        complimentTextView = rootView.findViewById(R.id.complimentTextView)
        skipButton = rootView.findViewById(R.id.skipButton)

        updateComplimentAndFriends()

        skipButton.setOnClickListener { updateComplimentAndFriends() }

        return rootView
    }


    private fun onButtonClick(friendId: String) {
        Log.d(TAG, "friend button clicked: $friendId")

        profileManager.addComplimentToProfile(
            friendId,
            complimentTextView.text.toString()
        ) { success ->
            if (success) {
                println("Compliment added and message sent to friend's inbox successfully")
            } else {
                println("Failed to add compliment and message to friend's inbox")
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.homeBackgroundColor)
        val actionBarTextColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = "Home"

        val actionBarTitleTextViewId = resources.getIdentifier("action_bar_title", "id", "android")
        val actionBarTitleTextView = activity?.findViewById<TextView>(actionBarTitleTextViewId)
        actionBarTitleTextView?.setTextColor(actionBarTextColor)

        val complimentText: TextView = view.findViewById(R.id.complimentTextView)
        complimentText.text = "\" Your compliment Goes here \""
    }

    private fun isColorDark(color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    private fun onSkipClick() {
        val randomColor = backgroundColors.random()
        constraintLayout.setBackgroundColor(randomColor)
    }

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }

    private fun fetchCompliment() {
        complimentManager.fetchCompliment { compliment ->
            complimentTextView.text = compliment
        }
    }

    private fun updateComplimentAndFriends() {
        constraintLayout = rootView.findViewById(R.id.homeconstraintLayout)
        val randomColor = backgroundColors.random()
        constraintLayout.setBackgroundColor(randomColor)

        fetchCompliment()

        val recyclerView: RecyclerView = rootView.findViewById(R.id.friendBtnRV)
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        val currentUser = GoogleSignIn.getLastSignedInAccount(requireActivity())

        currentUser?.id?.let {
            profileManager.getRandomFriends(it) { friendsData ->
                // Handle the list of friend data here
                val adapter = FriendBtnAdapter(friendsData) { friendId ->
                    onButtonClick(friendId)
                }
                recyclerView.adapter = adapter

            }
        }

    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}

