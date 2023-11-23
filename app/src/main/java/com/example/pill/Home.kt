package com.example.pill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pill.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //nasa home.xml to eto yung mismong bottom nav natin
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.itemActiveIndicatorColor = getColorStateList(R.color.white)

        //menuItem is associated with itemId
        //switch statement lang din yung when
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        //default fragment
        replaceFragment(HomeFragment())

        //fab onclicklistener to addDosage Activity
        val btnaddDosage = findViewById<FloatingActionButton>(R.id.fabAddDosage)
        btnaddDosage.setOnClickListener {
            val addDoseIntent = Intent(this, addDose::class.java)
            startActivity(addDoseIntent)
        }

    }

    //dito mangyayare yung magpapalit ng fragement based don sa clinick na icon, gumamit ng replace method para palitan yung fragment
    //yung frame_container yan yung current fragment id natin sa home.xml
    //fragment yun yung magiging new fragment based don sa ipapasang parameter
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}