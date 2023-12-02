package com.example.pill

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class AddDose : AppCompatActivity() {

    private lateinit var fabHome: FloatingActionButton
    private lateinit var rbRound: RadioButton
    private lateinit var rbCapsule: RadioButton
    private lateinit var rbBottle: RadioButton
    private lateinit var rbInjection: RadioButton
    private lateinit var etPillName: TextInputEditText
    private lateinit var etDosage: TextInputEditText
    private lateinit var rbDaily: MaterialRadioButton
    private lateinit var rbWeekly: MaterialRadioButton
    private lateinit var etEndDate: TextInputEditText
    private lateinit var chipGroup: ChipGroup
    private lateinit var chipMorning: Chip
    private lateinit var chipAfternoon: Chip
    private lateinit var chipEvening: Chip
    private lateinit var chipDawn: Chip
    private lateinit var btnNext: Button
    private lateinit var tvTitleMedication: TextView
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dose)

        databaseHelper = DBHelper(this)

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
        chipGroup = findViewById(R.id.cgTimesOfDay)
        tvTitleMedication = findViewById(R.id.tvTitleMedication)


        fabHome.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)

        }

        // Date picker dialog for etEndDate
        etEndDate.setOnClickListener {
            showDatePickerDialog()
        }

        val intent = intent
        val editMode = intent.getBooleanExtra("EditMode", false)
        val id = intent.getIntExtra("PillId", 0)
        val type = intent.getIntExtra("PillType", 1)
        val name = intent.getStringExtra("PillName")
        val dose = intent.getStringExtra("PillDosage")
        val recur = intent.getStringExtra("PillRecur")
        val enddate = intent.getLongExtra("PillEndDate", 0)

        if (editMode) {
            tvTitleMedication.text = "Edit Medication"
            btnNext.text = "Update Pill"
            val editable = Editable.Factory.getInstance()
            etPillName.text = editable.newEditable(name)
            etDosage.text = editable.newEditable(dose)

            when (type) {
                1 -> rbRound.isChecked = true
                2 -> rbCapsule.isChecked = true
                3 -> rbBottle.isChecked = true
                4 -> rbInjection.isChecked = true
                else -> rbRound.isChecked = true
            }

            if (recur == "Daily")
                rbDaily.isChecked
            else
                rbWeekly.isChecked

            val convertedEndDate = convertEpochToDate(enddate)
            etEndDate.text = editable.newEditable(convertedEndDate)

            //disabled field para di na maeedit to
            rbDaily.isEnabled = false
            rbWeekly.isEnabled = false
            etEndDate.isEnabled = false
            chipMorning.isEnabled = false
            chipAfternoon.isEnabled = false
            chipEvening.isEnabled = false
            chipDawn.isEnabled = false

            btnNext.setOnClickListener {
                if (etPillName.text != null && etDosage.text != null){
                    val selectedType = when {
                        rbRound.isChecked -> 1
                        rbCapsule.isChecked -> 2
                        rbBottle.isChecked -> 3
                        rbInjection.isChecked -> 4
                        else -> 1
                    }
                    databaseHelper.updatePill(
                        id,
                        selectedType,
                        etPillName.text.toString(),
                        etDosage.text.toString()
                    )
                    Log.i("Laman ng DB", "$id $selectedType ${etPillName.text.toString()} ${etDosage.text.toString()}")
                    val i = Intent(this, Home::class.java)
                    startActivity(i)
                }
                else {
                    Toast.makeText(this, "Pill Name and Dosage is required", Toast.LENGTH_SHORT).show()
                }
            }



        }
        else{
            btnNext.setOnClickListener {

                val pillType = when {
                    rbRound.isChecked -> 1
                    rbCapsule.isChecked -> 2
                    rbBottle.isChecked -> 3
                    rbInjection.isChecked -> 4
                    else -> 1
                }

                val pillName = etPillName.text.toString()
                val dosage = etDosage.text.toString()
                val dosageNum = Integer.parseInt(dosage)
                // Check if the number of times of day matches the dosage
                val timesOfDayCount = listOf(
                    chipMorning.isChecked,
                    chipAfternoon.isChecked,
                    chipEvening.isChecked,
                    chipDawn.isChecked
                ).count { it }

                if (timesOfDayCount < 1 || timesOfDayCount > dosageNum){
                    Toast.makeText(
                        this,
                        "You're selecting $timesOfDayCount time(s) of day, which is not equal to the dosage of $dosage",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val recurrence = if (rbDaily.isChecked) "Daily" else "Weekly"
                val endDate = etEndDate.text.toString()
                val timesOfDayList = mutableListOf<String>()

                if (!(chipMorning.isChecked || chipAfternoon.isChecked || chipEvening.isChecked || chipDawn.isChecked)) {
                    Toast.makeText(
                        this,
                        "Please select at least one time of day",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (chipMorning.isChecked) {
                    timesOfDayList.add("Morning")
                }
                if (chipAfternoon.isChecked) {
                    timesOfDayList.add("Afternoon")
                }
                if (chipEvening.isChecked) {
                    timesOfDayList.add("Evening")
                }
                if (chipDawn.isChecked) {
                    timesOfDayList.add("Dawn")
                }

                if (timesOfDayList.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please select at least one time of day",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val timesOfDayString = timesOfDayList.joinToString(", ")

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
                    putExtra("TimesOfDay", timesOfDayString)
                    putExtra("EpochEndDate", epochTimeEndDate)
                }

                startActivity(intent)
            }

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

    fun convertEpochToDate(epoch: Long, pattern: String = "yyyy-MM-dd"): String {
        try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            val date = Date(epoch)
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid Date"
        }
    }


}


