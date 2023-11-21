import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pill.R
import java.util.Calendar

class activity_add_dosage : AppCompatActivity() {

    private lateinit var medicationNameEditText: EditText
    private lateinit var dosageEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var recurrenceSpinner: Spinner
    private lateinit var morningCheckBox: CheckBox
    private lateinit var afternoonCheckBox: CheckBox
    private lateinit var eveningCheckBox: CheckBox
    private lateinit var nightCheckBox: CheckBox

    @SuppressLint("MissingInflatedId")
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dosage)

        medicationNameEditText = findViewById(R.id.MedicationName)
        dosageEditText = findViewById(R.id.editTextText2)
        endDateEditText = findViewById(R.id.editTextText3)
        recurrenceSpinner = findViewById(R.id.spinner)
        morningCheckBox = findViewById(R.id.checkBox5)
        afternoonCheckBox = findViewById(R.id.checkBox6)
        eveningCheckBox = findViewById(R.id.checkBox7)
        nightCheckBox = findViewById(R.id.checkBox4)

        setupRecurrenceSpinner()
        setupEndDateEditText()
    }

    private fun setupRecurrenceSpinner() {
        val recurrenceOptions = arrayOf("Daily", "Weekly", "Monthly")
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, recurrenceOptions)
        recurrenceSpinner.adapter = adapter
    }

    private fun setupEndDateEditText() {
        endDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                    endDateEditText.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun toConfirmation(view: View) {
        val i = Intent(this, Confirmation::class.java)
        startActivity(i)
    }
}