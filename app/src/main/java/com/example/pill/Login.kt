package com.example.pill

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.textfield.TextInputEditText


class Login : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister = findViewById<TextView>(R.id.tvRegister)
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
    }



    override fun onResume() {
        super.onResume()

        // Request notification permissions when the Login activity is resumed
        requestNotificationPermissions()
    }

    private fun loginDatabase(email: String, password: String) {
        val user = databaseHelper.readUser(UserClass(0, email, password, ""))
        if (user != null) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val homeActivity = Intent(this, Home::class.java)

            // Save userId to SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("USER_ID", user.id)
            editor.putString("USER_FNAME", user.fname)
            editor.apply()

            startActivity(homeActivity)
            finish()

        } else {
            Toast.makeText(
                this,
                "Email and Password do not match, Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun requestNotificationPermissions() {

        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            // Show a dialog asking the user for permission
            AlertDialog.Builder(this)
                .setTitle("Notification Permission")
                .setMessage("To receive notifications, please allow notifications for this app.")
                .setPositiveButton("Allow") { dialog, which ->
                    // Open the app notification settings
                    val intent = Intent().apply {
                        action = android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, packageName)
                    }

                    // Check if the intent can be resolved before starting the activity
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        // If the intent cannot be resolved, you may want to handle this case
                        Toast.makeText(
                            this,
                            "Cannot open notification settings",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .setNegativeButton("Don't Allow") { dialog, which ->
                    // Handle the case where the user denies notification permission
                    Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }
}
