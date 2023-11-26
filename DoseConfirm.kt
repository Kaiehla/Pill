package com.example.pill

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DoseConfirm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dose_confirm)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val doseDetail = findViewById<TextView>(R.id.doseDetail)


        val pillName = intent.getStringExtra("PillName")
        val dosage = intent.getStringExtra("Dosage")
        val recurrence = intent.getStringExtra("Recurrence")
        val endDate = intent.getStringExtra("EndDate")
        val timesOfDay = intent.getStringExtra("TimesOfDay")
        


        // Display the information
        val doseMessage = "You're now set to take $dosage dose of $pillName,\n$recurrence until $endDate. Times of the Day: $timesOfDay"
        doseDetail.text = doseMessage

        btnConfirm.setOnClickListener {

            finish()
        }
    }
}
