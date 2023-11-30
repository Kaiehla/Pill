package com.example.pill

import android.content.Context
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
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            // Retrieve data from SharedPreferences
            val userId = sharedPreferences.getInt("USER_ID", 0)

            //if user has already logged in before
            if (userId == 0){
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
        }, 2000)
    }
}