package com.example.pill

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DoseConfirm : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dose_confirm)

        databaseHelper = DBHelper(this)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val doseDetail = findViewById<TextView>(R.id.doseDetail)

        val intent = intent
        // Retrieve data from intent extras
        val pillType = intent.getIntExtra("PillType", 0)
        val pillName = intent.getStringExtra("PillName")
        val dosage = intent.getStringExtra("Dosage")
        val recurrence = intent.getStringExtra("Recurrence")
        val endDate = intent.getStringExtra("EndDate")
        val timesOfDay = intent.getStringExtra("TimesOfDay")
        val epochTime = intent.getLongExtra("EpochTime", 0)

        // Retrieve data from shared prefs to get the userid for foreign key
        // Get SharedPreferences from the hosting activity
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        // Retrieve data from SharedPreferences
        val userId = sharedPreferences.getInt("USER_ID", 0)

        // Display the information
        val doseMessage =
            "You're now set to take $dosage dose of $pillName,\n$recurrence until $endDate. Every $timesOfDay"
        doseDetail.text = doseMessage

        btnConfirm.setOnClickListener {
            // Insert to the database
            insertPillDatabase(
                userId, pillType, pillName!!, dosage!!,
                recurrence!!, endDate!!, timesOfDay!!, epochTime.toString()
            )
            finish()
        }
    }

    private fun insertPillDatabase(
        userId: Int,
        pillType: Int,
        pillName: String,
        dosage: String,
        recurrence: String,
        endDate: String,
        timesOfDay: String,
        pillDate: String
    ) {
        val insertedRowId = databaseHelper.insertPill(
            PillClass(
                0,
                userId,
                pillType,
                pillName,
                dosage,
                recurrence,
                endDate,
                timesOfDay,
                isTaken = false,
                pillDate
            )
        )

        Log.i(
            "Inserted Row",
            "$insertedRowId - ${PillClass(0, userId, pillType, pillName, dosage, recurrence, endDate, timesOfDay, isTaken = false, pillDate)}"
        )

        if (insertedRowId != -1L) {
            Toast.makeText(this, "Pill Created Successful", Toast.LENGTH_SHORT).show()
            val homeActivity = Intent(this, Home::class.java)
            startActivity(homeActivity)
            finish()
        } else {
            // Validation if register process failed
            Toast.makeText(this, "Pill Failed, Please try again", Toast.LENGTH_SHORT).show()
        }
    }
}
