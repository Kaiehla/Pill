package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Register : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btnCreateAccount)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        databaseHelper = DBHelper(this)

        btnRegister.setOnClickListener{
            val userFName = findViewById<EditText>(R.id.etFName).text.toString()
            val userEmail = findViewById<EditText>(R.id.etEmail).text.toString()
            val userPassword = findViewById<EditText>(R.id.etPassword).text.toString()

            signupDatabase(userFName, userEmail, userPassword)
        }

        tvLogin.setOnClickListener{
            val loginActivity = Intent(this, Login::class.java)
            startActivity(loginActivity)
            finish()
        }
    }

    private fun signupDatabase(fname: String, email: String, password: String){
        val insertedRowId = databaseHelper.insertUser(fname, email, password)
        if(insertedRowId != -1L){
            Toast.makeText(this, "Create an Account Successful", Toast.LENGTH_SHORT).show()
            val loginActivity = Intent(this, Login::class.java)
            startActivity(loginActivity)
            finish()
        } else{
            Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
        }
    }
}