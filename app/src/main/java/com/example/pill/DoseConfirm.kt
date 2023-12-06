package com.example.pill

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
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
        createNotificationChannel()

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
        val timesOfDayString = intent.getStringExtra("TimesOfDay")
        val epochTimeEndDate = intent.getLongExtra("EpochEndDate", 0)

        val timesOfDayList = timesOfDayString?.split(", ") ?: emptyList()

        Log.d(
            "DoseConfirmData",
            "PillName: $pillName, Dosage: $dosage, Recurrence: $recurrence, EndDate: $endDate, TimesOfDay: $timesOfDayList, EpochEndDate: $epochTimeEndDate"
        )


        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getInt("USER_ID", 0)

        // Display the information
        val doseMessage =
            "You're now set to take $dosage dose(s) of $pillName, $recurrence until $endDate. Every ${timesOfDayList.joinToString(", ")}."
        doseDetail.text = doseMessage

        btnConfirm.setOnClickListener {
            // Insert to the database
            insertDataToDatabase(userId, pillType, pillName, dosage,
                recurrence, epochTimeEndDate, timesOfDayList, epochTimeEndDate)


            val homeActivity = Intent(this, Home::class.java)
            startActivity(homeActivity)
            finish()
        }
    }

    private fun insertDataToDatabase(
        userId: Int,
        pillType: Int,
        pillName: String?,
        dosage: String?,
        recurrence: String?,
        endDate: Long,
        timesOfDay: List<String>,
        epochTime: Long
    ) {

//        Log.d(
//            "InsertData",
//            "UserId: $userId, PillType: $pillType, PillName: $pillName, Dosage: $dosage, Recurrence: $recurrence, EndDate: $endDate, TimesOfDay: $timesOfDay, EpochTime: $epochTime"
//        )


        if (pillName != null && recurrence != null && endDate != null && timesOfDay.isNotEmpty()) {
            val insertedRowId = databaseHelper.insertPillNew(
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
                    endDate
                )
            )

            Log.i(
                "Inserted Row",
                "$insertedRowId - ${PillClass(0, userId, pillType, pillName, dosage.toString(), recurrence, endDate, timesOfDay, isTaken = false, endDate)}"
            )

            if (insertedRowId == 1L) {
                Toast.makeText(this, "Pill Created Successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Validation if register process failed
                Toast.makeText(this, "Pill Failed, Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
    @SuppressLint("NewApi")
    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
