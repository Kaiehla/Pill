package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

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
            val loginEmail = findViewById<EditText>(R.id.etEmail).text.toString()
            val loginPassword = findViewById<EditText>(R.id.etPassword).text.toString()

            loginDatabase(loginEmail, loginPassword)
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
        val userExists = databaseHelper.readUser(email, password)
        if(userExists){
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
//            val HabitActivity = Intent(this, HabitActivity::class.java)
//            startActivity(HabitActivity)
//            finish()
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }

    }
}