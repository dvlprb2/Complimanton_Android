package com.example.complimaton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import java.util.concurrent.TimeUnit

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: WelcomePagerAdapter
    private lateinit var autoScrollTimer: CountDownTimer
    private lateinit var pageIndicator: WormDotsIndicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstTimeLogin = preferences.getBoolean("is_first_time_login", true)
        setContentView(R.layout.activity_welcome)

        pageIndicator = findViewById(R.id.pageIndicator)
        if (isFirstTimeLogin) {
            viewPager = findViewById(R.id.viewPager)
            adapter = WelcomePagerAdapter(this)
            viewPager.adapter = adapter
            pageIndicator.setViewPager2(viewPager)

            startAutoScrollTimer()
        } else {

            navigateToMain()
        }
    }


    private fun startAutoScrollTimer() {
        autoScrollTimer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(2), TimeUnit.SECONDS.toMillis(2)) {
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

    fun onLoginButtonClick(view: View) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.edit().putBoolean("is_first_time_login", false).apply()
        navigateToMain()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}