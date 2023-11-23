package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class addDose : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dose)

        val fabHome = findViewById<FloatingActionButton>(R.id.fabHome)
        fabHome.setOnClickListener {
            val HomeActivity = Intent(this, Home::class.java)
            startActivity(HomeActivity)
        }

        val btnNext = findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            val DoseConfirmActivity = Intent(this, DoseConfirm::class.java)
            startActivity(DoseConfirmActivity)
        }
    }



}