package com.example.complimaton.screens.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.R
import com.example.complimaton.adapters.InboxAdapter
import com.example.complimaton.managers.ProfileManager
import com.google.android.gms.auth.api.signin.GoogleSignIn

class InboxFragment : Fragment() {

    private lateinit var rootView: View
    private var profileManager = ProfileManager()
    private lateinit var inboxAdapter: InboxAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_inbox, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        inboxAdapter = InboxAdapter(emptyList())
        recyclerView.adapter = inboxAdapter

        val currentUser = GoogleSignIn.getLastSignedInAccount(requireActivity())
        currentUser?.id?.let {
            profileManager.startListeningForInboxUpdates(it) { inboxItems ->
                println(inboxItems)
                inboxAdapter.updateData(inboxItems)
            }
        }

        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}
