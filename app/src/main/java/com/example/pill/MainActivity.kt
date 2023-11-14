package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Splash screen
        Handler().postDelayed({
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }, 2000)
    }
}