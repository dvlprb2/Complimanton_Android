package com.example.complimaton

import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var container: FrameLayout

    companion object {
        private const val RC_SIGN_IN = 123
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container = findViewById(R.id.fragment_container)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)


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


        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // This should be in your strings.xml
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }
}
interface BackgroundColorProvider {
    fun getBackgroundColor(): Int
}