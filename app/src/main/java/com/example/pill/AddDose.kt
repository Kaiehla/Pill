package com.example.pill

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.radiobutton.MaterialRadioButton
import java.text.SimpleDateFormat
import java.util.*

class AddDose : AppCompatActivity() {

    private lateinit var fabHome: FloatingActionButton
    private lateinit var rbRound: RadioButton
    private lateinit var rbCapsule: RadioButton
    private lateinit var rbBottle: RadioButton
    private lateinit var rbInjection: RadioButton
    private lateinit var etPillName: EditText
    private lateinit var etDosage: EditText
    private lateinit var rbDaily: MaterialRadioButton
    private lateinit var rbWeekly: MaterialRadioButton
    private lateinit var etEndDate: EditText
    private lateinit var chipMorning: Chip
    private lateinit var chipAfternoon: Chip
    private lateinit var chipEvening: Chip
    private lateinit var chipDawn: Chip
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dose)

        fabHome = findViewById(R.id.fabHome)
        rbRound = findViewById(R.id.rbRound)
        rbCapsule = findViewById(R.id.rbCapsule)
        rbBottle = findViewById(R.id.rbBottle)
        rbInjection = findViewById(R.id.rbInjection)
        etPillName = findViewById(R.id.etPillName)
        etDosage = findViewById(R.id.etDosage)
        rbDaily = findViewById(R.id.rbDaily)
        rbWeekly = findViewById(R.id.rbWeekly)
        etEndDate = findViewById(R.id.etEndDate)
        chipMorning = findViewById(R.id.chipMorning)
        chipAfternoon = findViewById(R.id.chipAfternoon)
        chipEvening = findViewById(R.id.chipEvening)
        chipDawn = findViewById(R.id.chipDawn)
        btnNext = findViewById(R.id.btnNext)

        fabHome.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)

        }

        // Date picker dialog for etEndDate
        etEndDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnNext.setOnClickListener {
            // Retrieve data from input fields
            val pillType = when {
                rbRound.isChecked -> 1
                rbCapsule.isChecked -> 2
                rbBottle.isChecked -> 3
                rbInjection.isChecked -> 4
                else -> 1
            }

            val pillName = etPillName.text.toString()
            val dosage = etDosage.text.toString()
            val recurrence = if (rbDaily.isChecked) "Daily" else "Weekly"
            val endDate = etEndDate.text.toString()
            val timesOfDay = StringBuilder()

            if (pillName.isEmpty() || dosage.isEmpty()) {
                Toast.makeText(this, "Pill name and dosage cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (chipMorning.isChecked) {
                timesOfDay.append("Morning, ")
            }
            if (chipAfternoon.isChecked) {
                timesOfDay.append("Afternoon, ")
            }
            if (chipEvening.isChecked) {
                timesOfDay.append("Evening, ")
            }
            if (chipDawn.isChecked) {
                timesOfDay.append("Dawn, ")
            }

            if (timesOfDay.isNotEmpty()) {
                timesOfDay.setLength(timesOfDay.length - 2)
            }

            // Ensure that an end date is selected
            if (endDate.isEmpty()) {
                Toast.makeText(this, "Please select an end date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val epochTimeEndDate = convertDateToEpoch(endDate)



            val intent = Intent(this, DoseConfirm::class.java).apply {
                putExtra("PillType", pillType)
                putExtra("PillName", pillName)
                putExtra("Dosage", dosage)
                putExtra("Recurrence", recurrence)
                putExtra("EndDate", endDate)
                putExtra("TimesOfDay", timesOfDay.toString())
                putExtra("EpochEndDate", epochTimeEndDate)
            }

            startActivity(intent)
        }


    }
    private fun convertDateToEpoch(dateString: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val date = sdf.parse(dateString)
            return date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
            etEndDate.setText("$year-${month + 1}-$day")
        }, year, month, day)

        datePickerDialog.show()
    }


}


