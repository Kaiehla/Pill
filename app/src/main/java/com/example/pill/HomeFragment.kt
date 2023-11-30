package com.example.pill

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewFragment = inflater.inflate(R.layout.fragment_home, container, false)

        databaseHelper = DBHelper(requireContext())

        // Get all pills from the database
        val pillsList = databaseHelper.getAllPills()

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
        })
        // Inflate the layout for this fragment
        return viewFragment
    }
}