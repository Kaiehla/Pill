package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.example.pill.databinding.ActivityAddDoseBinding
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText

class AddDose : AppCompatActivity() {
    private lateinit var binding: ActivityAddDoseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddDoseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fabHome = findViewById<FloatingActionButton>(R.id.fabHome)
        fabHome.setOnClickListener {
            val HomeActivity = Intent(this, Home::class.java)
            startActivity(HomeActivity)
        }

        val btnNext = binding.btnNext
        // Add your logic here as needed
        btnNext.setOnClickListener {
            // Retrieve data from input fields
            val pillType = when {
                binding.rbRound.isChecked -> 1
                binding.rbCapsule.isChecked -> 2
                binding.rbBottle.isChecked -> 3
                binding.rbInjection.isChecked -> 4
                else -> 1
            }
            val pillName = binding.etPillName.text.toString()
            val dosage = binding.etDosage.text.toString()
            val recurrence = if (binding.rbDaily.isChecked) "Daily" else "Weekly"
            val endDate = binding.etEndDate.text.toString()
            val timesOfDay = StringBuilder()


            // Check which chips are selected
            if (binding.chipMorning.isChecked) {
                timesOfDay.append("Morning, ")
            }
            if (binding.chipAfternoon.isChecked) {
                timesOfDay.append("Afternoon, ")
            }
            if (binding.chipEvening.isChecked) {
                timesOfDay.append("Evening, ")
            }
            if (binding.chipDawn.isChecked) {
                timesOfDay.append("Dawn, ")
            }


            if (timesOfDay.isNotEmpty()) {
                timesOfDay.setLength(timesOfDay.length - 2)
            }

            // Create an Intent to pass data to DoseConfirm
            val intent = Intent(this, DoseConfirm::class.java).apply {
                putExtra("PillType", pillType)
                putExtra("PillName", pillName)
                putExtra("Dosage", dosage)
                putExtra("Recurrence", recurrence)
                putExtra("EndDate", endDate)
                putExtra("TimesOfDay", timesOfDay.toString())
            }

            startActivity(intent)
        }
    }
}