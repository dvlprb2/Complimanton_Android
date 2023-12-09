package com.example.complimaton

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import com.example.complimaton.managers.ProfileManager
import com.example.complimaton.screens.tabs.HomeFragment
import com.example.complimaton.screens.tabs.InboxFragment
import com.example.complimaton.screens.tabs.ProfileFragment
import com.example.complimaton.screens.welcome.WelcomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val profileManager = ProfileManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)


        val acct = GoogleSignIn.getLastSignedInAccount(this)
        Log.d(TAG, "currentUserEmail:" + acct?.id!!)

        val auth = Firebase.auth
        val currentUser = auth.currentUser

        Log.d(TAG, "currentUser ${currentUser?.displayName}")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result
                // Save fcmToken to your server along with user information
                profileManager.saveFcmTokenToProfile(acct.id!!, fcmToken)
            }
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    bottomNavigationView.setBackgroundColor(
                        (item as? BackgroundColorProvider)?.getBackgroundColor() ?: Color.WHITE
                    )
                    true
                }

                R.id.action_inbox -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, InboxFragment())
                        .commit()
                    true
                }

                R.id.action_profile -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, ProfileFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }


        // Set the default fragment
        bottomNavigationView.selectedItemId = R.id.action_home
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

interface BackgroundColorProvider {
    fun getBackgroundColor(): Int
}