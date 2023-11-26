package com.example.pill

import android.R.attr.key
import android.R.attr.value
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class Login : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        databaseHelper = DBHelper(this)

        btnLogin.setOnClickListener{
            val loginEmail = findViewById<TextInputEditText>(R.id.etEmail).text.toString()
            val loginPassword = findViewById<TextInputEditText>(R.id.etPassword).text.toString()

            //validation if user input no value
            if(loginEmail.trim()!="" && loginPassword.trim()!="")
                loginDatabase(loginEmail, loginPassword)
            else
                Toast.makeText(this, "Please complete all fields to login", Toast.LENGTH_SHORT).show()

        }

        tvRegister.setOnClickListener{
            val registerActivity = Intent(this, Register::class.java)
            startActivity(registerActivity)
            finish()
        }

        tvForgotPassword.setOnClickListener{
            val ForgotPassActivity = Intent(this, ForgotPassword::class.java)
            startActivity(ForgotPassActivity)
            finish()
        }
    }

    private fun loginDatabase(email: String, password: String){
        val user = databaseHelper.readUser(UserClass(0, email, password, ""))
        if(user != null){
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val homeActivity = Intent(this, Home::class.java)

//            // Pass user details to Home activity solution 1
//            homeActivity.putExtra("id", user.id)
//            homeActivity.putExtra("email", user.email)
//            homeActivity.putExtra("password", user.password)
//            homeActivity.putExtra("fname", user.fname)
//
//            // Pass user details to Dose Confirm activity
//            val DoseConfirm = Intent(this, DoseConfirm::class.java)
//            DoseConfirm.putExtra("user_id", user.id)
//
//            //pangcheck nakukuha naman niya yung data from userclass
//            //Log.i("Login", "Full Name: ${user.fname}")

            //PERSEVERE: solution2 pass data throughout the whole app using shared preference
            // Save userId to SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("USER_ID", user.id)
            editor.putString("USER_FNAME", user.fname)
            editor.apply()

            startActivity(homeActivity)
            finish()

        } else {
            Toast.makeText(this, "Email and Password do not match, Please try again.", Toast.LENGTH_SHORT).show()
        }

    }
}