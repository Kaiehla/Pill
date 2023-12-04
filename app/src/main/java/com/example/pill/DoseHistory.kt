package com.example.pill

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.ContactsContract.Data
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DoseHistory : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dose_history)

        //fab button back to home
        val fab = findViewById<FloatingActionButton>(R.id.fabBack)
        fab.setOnClickListener {
            val i = Intent(this, Home::class.java)
            startActivity(i)
        }

        databaseHelper = DBHelper(this)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        // Retrieve data from SharedPreferences
        val userId = sharedPreferences.getInt("USER_ID", 0)

        // Get all pills from the database
        val pillsList = databaseHelper.getAllPillsByUserId(userId)
        if (pillsList.isEmpty()){
            val tvNoPills = findViewById<TextView>(R.id.tvNoPillsFound)
            tvNoPills.text = "No Past Pill Record"
            tvNoPills.setTextSize(24f)
        }
        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.historyRecycler)
        val adapter = AdapterClass(pillsList) // Create your adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //on itemclick magpapakita bottom modal sheet for dose details
        adapter.setOnItemClickListener(object : AdapterClass.onItemClickListener {
            override fun onItemClick(position: Int) {
                //dito pinapasa yung data from recyclerview to detail sheet dahil sa datalist parameter
                val detailModal = DetailSheet(pillsList[position])
                detailModal.show(supportFragmentManager, "detailModalSheet")
            }

            override fun onImageButtonClick(position: Int) {
                val selectedPill = pillsList[position]

                // Toggle the status (1 to 0 or 0 to 1)
                val newStatus = !selectedPill.isTaken

                // Update the status in the database
                databaseHelper.updatePillStatus(selectedPill.id, newStatus)

                // Refresh the RecyclerView
                pillsList[position].isTaken = newStatus
                // Notify the adapter that the data has changed
                adapter.notifyItemChanged(position)
            }
        })
    }

}