package com.example.complimaton.screens.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.complimaton.adapters.CardAdapter
import com.example.complimaton.CardItem
import com.example.complimaton.R

class InboxFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private val cardItems = listOf(
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "This is a long description for Person 1.", "5h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "This is a long description for Person 1.", "5h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "This is a long description for Person 1.", "5h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "This is a long description for Person 1.", "5h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "This is a long description for Person 1.", "5h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "This is a long description for Person 1.", "5h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "Another long description for Person 2.", "2h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "Another long description for Person 2.", "2h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "Another long description for Person 2.", "2h ago"),
        CardItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "Another long description for Person 2.", "2h ago"),
        // Add more card items as needed
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = CardAdapter(cardItems)
    }

}
