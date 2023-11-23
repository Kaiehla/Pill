package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DoseConfirm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dose_confirm)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            val HomeActivity = Intent(this, Home::class.java)
            startActivity(HomeActivity)
        }
    }

}