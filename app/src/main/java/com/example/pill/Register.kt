package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class Register : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btnCreateAccount)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        databaseHelper = DBHelper(this)

        btnRegister.setOnClickListener{
            val userFName = findViewById<TextInputEditText>(R.id.etFName).text.toString()
            val userEmail = findViewById<TextInputEditText>(R.id.etEmail).text.toString()
            val userPassword = findViewById<TextInputEditText>(R.id.etPassword).text.toString()
            val userConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword).text.toString()

            //validation if user input no value
            //if may laman lahat and match si password and confirm password pasok boom
            if(userFName.trim()!="" && userEmail.trim()!="" && userPassword.trim()!="") {
                if (userPassword.trim() == userConfirmPassword.trim())
                    if (isValidEmail(userEmail)) {
                        signupDatabase(userFName, userEmail, userPassword)
                    } else {
                        Toast.makeText(this, "Try Again! Please enter valid email address", Toast.LENGTH_LONG).show()
                    }
                else
                    Toast.makeText(this, "Try Again! Password and Confirm Password do not match", Toast.LENGTH_LONG).show()
                }
            else
                Toast.makeText(this, "Try Again! Please complete all fields to continue", Toast.LENGTH_LONG).show()
            }

        tvLogin.setOnClickListener{
            val loginActivity = Intent(this, Login::class.java)
            startActivity(loginActivity)
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")
        return emailRegex.matches(email)
    }

    private fun signupDatabase(fname: String, email: String, password: String){
        val insertedRowId = databaseHelper.insertUser(UserClass(0, email, password, fname))
        if(insertedRowId != -1L){
            Toast.makeText(this, "Create an Account Successful", Toast.LENGTH_SHORT).show()
            val loginActivity = Intent(this, Login::class.java)
            startActivity(loginActivity)
            finish()
        } else{
            //validation if register process failed
            Toast.makeText(this, "Register Account Failed, Please try again", Toast.LENGTH_SHORT).show()
        }
    }
}