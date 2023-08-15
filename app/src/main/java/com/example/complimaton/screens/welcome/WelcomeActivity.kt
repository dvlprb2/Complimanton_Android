package com.example.complimaton.screens.welcome

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.complimaton.MainActivity
import com.example.complimaton.R
import com.example.complimaton.adapters.WelcomePagerAdapter
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import java.util.concurrent.TimeUnit


class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: WelcomePagerAdapter
    private lateinit var autoScrollTimer: CountDownTimer
    private lateinit var pageIndicator: WormDotsIndicator

    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var profileManager: ProfileManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth

        pageIndicator = findViewById(R.id.pageIndicator)
        viewPager = findViewById(R.id.viewPager)
        adapter = WelcomePagerAdapter(this)
        viewPager.adapter = adapter
        pageIndicator.setViewPager2(viewPager)

        startAutoScrollTimer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful
                val account = task.getResult(ApiException::class.java)!!

                val userId = account.id!!
                val name = account.displayName!!
                val email = account.email!!
                val profilePictureUrl = account.photoUrl!!

                profileManager = ProfileManager()
                profileManager.createProfileDocument(userId, name, email, profilePictureUrl)

                account.idToken?.let { updateUI(it) }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = GoogleSignIn.getLastSignedInAccount(this)
        currentUser?.idToken?.let { updateUI(it) }
    }

    private fun startAutoScrollTimer() {
        autoScrollTimer =
            object : CountDownTimer(TimeUnit.SECONDS.toMillis(2), TimeUnit.SECONDS.toMillis(2)) {
                override fun onTick(millisUntilFinished: Long) {
                    // Not used in this case
                }

                override fun onFinish() {
                    val currentItem = viewPager.currentItem
                    val nextItem = (currentItem + 1) % adapter.itemCount
                    viewPager.setCurrentItem(nextItem, true)
                    startAutoScrollTimer()
                }
            }
        autoScrollTimer.start()
    }

    override fun onStop() {
        super.onStop()
        Log.d("ON STOP", "on stop called")
        autoScrollTimer.cancel()
    }

    fun signIn(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(token: String?) {
        if (token != null) {
          val intent = Intent(this, MainActivity::class.java)
          startActivity(intent)
          finish()
        }
    }

    companion object {
        private const val TAG = "WelcomeActivity"
        private const val RC_SIGN_IN = 9001
    }
}