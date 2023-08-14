package com.example.complimaton.managers

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore

class ProfileManager {

    private val db = FirebaseFirestore.getInstance()

    fun createProfileDocument(
        userId: String,
        name: String,
        email: String,
        profilePictureUrl: Uri
    ) {
        val profileData = hashMapOf(
            "name" to name,
            "email" to email,
            "profilePictureUrl" to profilePictureUrl.toString(), // Convert Uri to String
            "inbox" to emptyList<String>(),
            "friends" to emptyList<String>(),
            "compliments" to emptyList<String>()
        )

        val userDocRef = db.collection("profiles").document(userId)

        userDocRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        // User document with the provided ID already exists
                        println("User document with ID '$userId' already exists.")
                    } else {
                        // User document doesn't exist, create it
                        userDocRef.set(profileData)
                            .addOnSuccessListener {
                                println("Profile document added with ID: $userId")
                            }
                            .addOnFailureListener { e ->
                                println("Error adding profile document: $e")
                            }
                    }
                } else {
                    println("Error checking for user document: ${task.exception}")
                }
            }
    }

    fun getComplimentsCount(userId: String, completionHandler: (Int) -> Unit) {
        db.collection("profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val compliments = documentSnapshot["compliments"] as? List<*>
                val complimentsCount = compliments?.size ?: 0
                completionHandler(complimentsCount)
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error getting compliments count: $e")
                completionHandler(0) // Return 0 in case of failure
            }
    }

    fun getFriendsCount(userId: String, completionHandler: (Int) -> Unit) {
        db.collection("profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val friends = documentSnapshot["friends"] as? List<*>
                val friendsCount = friends?.size ?: 0
                completionHandler(friendsCount)
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error getting friends count: $e")
                completionHandler(0) // Return 0 in case of failure
            }
    }

    fun getTopCompliments(userId: String, completionHandler: (List<String>) -> Unit) {
        db.collection("profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val compliments = documentSnapshot["compliments"] as? List<String>
                val topCompliments = compliments?.take(5) ?: emptyList()
                completionHandler(topCompliments)
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error getting top compliments: $e")
                completionHandler(emptyList()) // Return an empty list in case of failure
            }
    }
}

