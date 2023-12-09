package com.example.complimaton.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import com.example.complimaton.R
import com.example.complimaton.adapters.AddFriendsAdapter
import com.example.complimaton.managers.ProfileData
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.zxing.integration.android.IntentIntegrator

class AddFriendActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
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

        val qrCodeScannerBtn: ImageButton = findViewById(R.id.qrCodeScannerBtn)

        qrCodeScannerBtn.setOnClickListener {
            // Check for camera permission
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Open QR code scanner
                openQRCodeScanner()
            } else {
                // Request camera permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun updateRecyclerView(profiles: List<ProfileData>) {
        val currentUser = GoogleSignIn.getLastSignedInAccount(this)
        val adapter = AddFriendsAdapter(this@AddFriendActivity, profiles, profileManager)
        recyclerView.adapter = adapter
    }

    private fun openQRCodeScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(false)
        integrator.setPrompt("Scan a QR Code")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                searchEditText.setText(result.contents)
            } else {
                showToast("Scan canceled")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, "Scanned Value: $message", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

}