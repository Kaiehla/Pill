import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.example.pill.Home
import com.example.pill.R

class Confirmation : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override

    fun

            onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val medicationName = findViewById<EditText>(R.id.MedicationName)
        val dosage = findViewById<EditText>(R.id.editTextText2)
        val recurrence = findViewById<EditText>(R.id.spinner)
        val endDate = findViewById<EditText>(R.id.editTextText3)

        val confirmButton: Button = findViewById(R.id.confirmbutton)
        val backButton: Button = findViewById(R.id.backbutton)
        val confirmationMessage: TextView = findViewById(R.id.textView4)


        confirmButton.setOnClickListener {
            val medicationNameInput = medicationName.text.toString()
            val dosageInput = dosage.text.toString()
            val recurrenceInput = recurrence.text.toString()
            val endDateInput = endDate.text.toString()

            if (medicationNameInput.isEmpty() || dosageInput.isEmpty() || recurrenceInput.isEmpty() || endDateInput.isEmpty()) {
                // Show error message
            } else {
                confirmationMessage.text = "Medication Name: $medicationNameInput\nDosage: $dosageInput\nRecurrence: $recurrenceInput\nEnd Date: $endDateInput\nConfirmed!"
            }
        }

        fun toHome(view: View) {
            val i = Intent(this, Home::class.java)
            startActivity(i)
        }
    }
}