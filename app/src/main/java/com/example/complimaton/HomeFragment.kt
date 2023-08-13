package com.example.complimaton

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.QuoteSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var complimentTextView: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var skipButton: Button
    private lateinit var constraintLayout: ConstraintLayout
    private val backgroundColors = listOf(
        Color.parseColor("#FF5733"),
        Color.parseColor("#FFC300"),
        Color.parseColor("#33FF57"),
        Color.parseColor("#339CFF"),
        Color.parseColor("#B833FF")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        complimentTextView = rootView.findViewById(R.id.complimentTextView)
        button1 = rootView.findViewById(R.id.button1)
        button2 = rootView.findViewById(R.id.button2)
        button3 = rootView.findViewById(R.id.button3)
        button4 = rootView.findViewById(R.id.button4)
        skipButton = rootView.findViewById(R.id.skipButton)
        constraintLayout = rootView.findViewById(R.id.homeconstraintLayout)

        button1.setOnClickListener { onButtonClick(button1) }
        button2.setOnClickListener { onButtonClick(button2) }
        button3.setOnClickListener { onButtonClick(button3) }
        button4.setOnClickListener { onButtonClick(button4) }
        skipButton.setOnClickListener { onSkipClick() }

        val randomColor = backgroundColors.random()
        constraintLayout.setBackgroundColor(randomColor)

        return rootView
    }


    private fun onButtonClick(clickedButton: Button) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.colors = intArrayOf(
            Color.parseColor("#FF5733"),
            Color.parseColor("#03A9F4"),
            Color.parseColor("#FFC107"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#E91E63")
        )

        gradientDrawable.cornerRadius = 30.dpToPx(requireContext()).toFloat()

        val originalBackground = clickedButton.background
        clickedButton.background = gradientDrawable

        // Disable other buttons and enable only the clicked button
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false

        clickedButton.isEnabled = true

        Handler(Looper.getMainLooper()).postDelayed({
            clickedButton.background = originalBackground
            // Enable all buttons after delay
            button1.isEnabled = true
            button2.isEnabled = true
            button3.isEnabled = true
            button4.isEnabled = true
        }, 2000)
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
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    fun getBackgroundColor(): Int {
        return ContextCompat.getColor(requireContext(), R.color.color3)
    }
    private fun onSkipClick() {
        val randomColor = backgroundColors.random()
        constraintLayout.setBackgroundColor(randomColor)

    }

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }
}

