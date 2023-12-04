package com.example.pill

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    private lateinit var databaseHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewFragment = inflater.inflate(R.layout.fragment_home, container, false)

        databaseHelper = DBHelper(requireContext())

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", 0)
        // Get all pills from the database
        val pillsList = databaseHelper.getAllPillsByUserIdANDToday(userId)
        if (pillsList.isEmpty()){
            val tvNoPills = viewFragment.findViewById<TextView>(R.id.tvNoPillsFound)
            tvNoPills.text = "No Pills found for today"
        }


        // Set up RecyclerView
        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = AdapterClass(pillsList) // Create your adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        //on itemclick magpapakita bottom modal sheet for dose details
        adapter.setOnItemClickListener(object : AdapterClass.onItemClickListener{
            override fun onItemClick(position: Int) {
                //dito pinapasa yung data from recyclerview to detail sheet dahil sa datalist parameter
                val detailModal = DetailSheet(pillsList[position])
                activity?.supportFragmentManager?.let{detailModal.show(it, "detailModalSheet")}
            }

            //pill
            override fun onImageButtonClick(position: Int) {
                val selectedPill = pillsList[position]

                // Toggle the status (1 to 0 or 0 to 1)
                val newStatus = !selectedPill.isTaken
                Log.d("HomeFragment", "New status value: $newStatus")


                // Update the status in the database
                databaseHelper.updatePillStatus(selectedPill.id, newStatus)

                // Refresh the RecyclerView
                pillsList[position].isTaken = newStatus
                // Notify the adapter that the data has changed
                adapter.notifyItemChanged(position)
            }
        })
        // Inflate the layout for this fragment
        return viewFragment
    }
}