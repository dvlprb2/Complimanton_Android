package com.example.complimaton.managers

import android.app.Notification
import android.net.Uri
import com.example.complimaton.adapters.Friend
import com.example.complimaton.adapters.FriendData
import com.example.complimaton.adapters.InboxItem
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

data class ProfileData(
    val id: String,
    val name: String,
    val profilePictureUrl: String,
)

class ProfileManager {

    private val db = FirebaseFirestore.getInstance()
    private var inboxListener: ListenerRegistration? = null


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

    fun getRandomFriends(userId: String, completionHandler: (List<Friend>) -> Unit) {
        db.collection("profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val friends = documentSnapshot["friends"] as? List<DocumentReference>

                if (friends.isNullOrEmpty()) {
                    completionHandler(emptyList()) // No friends found, return an empty list
                    return@addOnSuccessListener
                }

                val randomFriendRefs = friends.shuffled().take(4) // Shuffle and select 4 random friend references

                val friendList = mutableListOf<Friend>()
                val fetchFriendDataTasks = mutableListOf<Task<DocumentSnapshot>>()

                for (friendRef in randomFriendRefs) {
                    val fetchTask = friendRef.get()
                    fetchFriendDataTasks.add(fetchTask)

                    fetchTask.addOnSuccessListener { friendDocument ->
                        val friendData = friendDocument.data
                        if (friendData != null) {
                            println(friendData)
                            val friend = Friend(
                                id = friendRef.id,
                                name = friendData["name"] as? String ?: "",
                                profilePictureUrl = friendData["profilePictureURL"] as? String ?: ""
                            )
                            friendList.add(friend)
                            println(friendList)
                        }
                    }
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(fetchFriendDataTasks)
                    .addOnSuccessListener {
                        completionHandler(friendList)
                    }
                    .addOnFailureListener { e ->
                        // Handle error here
                        println("Error fetching friend data: $e")
                        completionHandler(emptyList()) // Return an empty list in case of failure
                    }
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error getting friends list: $e")
                completionHandler(emptyList()) // Return an empty list in case of failure
            }
    }

    fun addComplimentToProfile(userId: String, compliment: String, completionHandler: (Boolean) -> Unit) {
        val userDocRef = db.collection("profiles").document(userId)

        userDocRef.update("compliments", FieldValue.arrayUnion(compliment))
            .addOnSuccessListener {
                // Now add the compliment as a message to friend's inbox
                val message = "You received a compliment: $compliment"
                val messageType = "compliment"
                val timestamp = Timestamp.now()

                addMessageToFriendInbox(userId, messageType, message, timestamp) { success ->
                    if (success) {
                        completionHandler(true) // Compliment and message added successfully
                    } else {
                        completionHandler(false) // Message addition failed
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error adding compliment: $e")
                completionHandler(false) // Return false in case of failure
            }
    }


    fun startListeningForInboxUpdates(userId: String, callback: (List<InboxItem>) -> Unit) {
        val profileRef = db.collection("profiles").document(userId)

        inboxListener = profileRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                // Handle the error
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val inboxArray = documentSnapshot["inbox"] as? List<Map<String, Any>>
                println(documentSnapshot)
                if (inboxArray != null) {
                    println(inboxArray)
                    val inboxItems = inboxArray.map { itemMap ->
                        val type = itemMap["type"] as? String ?: ""
                        val message = itemMap["msg"] as? String ?: ""
                        val timestamp = itemMap["created_at"] as? Timestamp ?: Timestamp.now()

                        InboxItem(type, message, timestamp)
                    }
                    callback(inboxItems)
                }
            }
        }
    }

    fun stopListeningForInboxUpdates() {
        inboxListener?.remove()
    }

    private fun addMessageToFriendInbox(friendId: String, messageType: String, message: String, timestamp: Timestamp, completionHandler: (Boolean) -> Unit) {
        val friendDocRef = db.collection("profiles").document(friendId)

        val messageData = hashMapOf(
            "type" to messageType,
            "msg" to message,
            "timestamp" to timestamp
        )

        friendDocRef.update("inbox", FieldValue.arrayUnion(messageData))
            .addOnSuccessListener {
                completionHandler(true) // Message added successfully
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error adding message to friend's inbox: $e")
                completionHandler(false) // Return false in case of failure
            }
    }

    fun getAllFriends(userId: String, completionHandler: (List<FriendData>) -> Unit) {
        db.collection("profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val friends = documentSnapshot["friends"] as? List<DocumentReference>

                if (friends.isNullOrEmpty()) {
                    completionHandler(emptyList()) // No friends found, return an empty list
                    return@addOnSuccessListener
                }

                val friendDataList = mutableListOf<FriendData>()
                val fetchFriendDataTasks = mutableListOf<Task<DocumentSnapshot>>()

                for (friendRef in friends) {
                    val fetchTask = friendRef.get()
                    fetchFriendDataTasks.add(fetchTask)

                    fetchTask.addOnSuccessListener { friendDocument ->
                        val friendId = friendDocument.id
                        val friendName = friendDocument["name"] as? String
                        val friendProfilePictureUrl = friendDocument["profilePictureURL"] as? String

                        if (friendName != null && friendProfilePictureUrl != null) {
                            friendDataList.add(FriendData(friendId, friendName, friendProfilePictureUrl))
                        }
                    }
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(fetchFriendDataTasks)
                    .addOnSuccessListener {
                        completionHandler(friendDataList)
                    }
                    .addOnFailureListener { e ->
                        // Handle error here
                        println("Error fetching friend data: $e")
                        completionHandler(emptyList()) // Return an empty list in case of failure
                    }
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error getting friends list: $e")
                completionHandler(emptyList()) // Return an empty list in case of failure
            }
    }


    fun removeFriendFromProfile(userId: String, friendId: String, completionHandler: (Boolean) -> Unit) {
        val userDocRef = db.collection("profiles").document(userId)
        val friendDocRef = db.collection("profiles").document(friendId)

        // Remove friendId from the user's friends array
        userDocRef.update("friends", FieldValue.arrayRemove(db.document("profiles/$friendId")))
            .addOnSuccessListener {
                // After removing from user's profile, remove user's userId from friend's friends array
                friendDocRef.update("friends", FieldValue.arrayRemove(db.document("profiles/$userId")))
                    .addOnSuccessListener {
                        completionHandler(true) // Friend removed successfully from both profiles
                    }
                    .addOnFailureListener { e ->
                        println("Error removing user from friend's profile: $e")
                        completionHandler(false) // Return false in case of failure
                    }
            }
            .addOnFailureListener { e ->
                println("Error removing friend from user's profile: $e")
                completionHandler(false) // Return false in case of failure
            }
    }

    fun getAllCompliments(userId: String, completionHandler: (List<String>) -> Unit) {
        db.collection("profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val compliments = documentSnapshot["compliments"] as? List<String>
                completionHandler(compliments ?: emptyList())
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error getting all compliments: $e")
                completionHandler(emptyList()) // Return an empty list in case of failure
            }
    }

    fun searchProfilesByEmail(
        email: String,
        currentUserId: String,
        completionHandler: (List<ProfileData>) -> Unit
    ) {
        println(email)

        db.collection("profiles")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                println(currentUserId)



                val profiles = querySnapshot.documents.mapNotNull { document ->
                    println(document)
                    val id = document.id
                    val name = document["name"] as? String
                    val profilePictureUrl = document["profilePictureURL"] as? String
                    println(name)
                    println(profilePictureUrl)
                    if (id != currentUserId && name != null && profilePictureUrl != null) {
                        ProfileData(id, name, profilePictureUrl)
                    } else {
                        null
                    }
                }
                println(profiles)
                completionHandler(profiles)
            }
            .addOnFailureListener { e ->
                // Handle error here
                println("Error searching profiles by email: $e")
                completionHandler(emptyList()) // Return an empty list in case of failure
            }
    }

    fun addFriendAndUpdateInbox(
        currentUserId: String,
        currentUserName: String,
        friendId: String,
        completionHandler: (Boolean) -> Unit
    ) {
        val userDocRef = db.collection("profiles").document(currentUserId)
        val friendDocRef = db.collection("profiles").document(friendId)

        // Add friend's document reference to the current user's "friends" field
        userDocRef.update("friends", FieldValue.arrayUnion(friendDocRef))
            .addOnSuccessListener {
                // Add user's document reference to the friend's "friends" field
                friendDocRef.update("friends", FieldValue.arrayUnion(userDocRef))
                    .addOnSuccessListener {
                        // Update the inbox message for both the current user and the friend
                        val message = "$currentUserName added you as a friend."
                        addMessageToFriendInbox(friendId, "FR", message, Timestamp.now()) { success ->
                            if (success) {
                                completionHandler(true)
                            } else {
                                completionHandler(false)
                            }
                        }
                    }
                    .addOnFailureListener { friendError ->
                        println("Error adding user document reference to friend's profile: $friendError")
                        completionHandler(false)
                    }
            }
            .addOnFailureListener { userError ->
                println("Error adding friend document reference to user's profile: $userError")
                completionHandler(false)
            }
    }

    fun saveFcmTokenToProfile(userId: String, token: String) {
        val userDocRef = db.collection("profiles").document(userId)

        userDocRef.update("fcmToken", token)
            .addOnSuccessListener {
                println("FCM token saved to profile for user: $userId")
            }
            .addOnFailureListener { e ->
                println("Error saving FCM token to profile: $e")
            }
    }
}

