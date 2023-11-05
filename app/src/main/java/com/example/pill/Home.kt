package com.example.pill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //nasa home.xml to eto yung mismong bottom nav natin
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //menuItem is associated with itemId
        //switch statement lang din yung when
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_cal -> {
                    replaceFragment(CalendarFragment())
                    true
                }
                //in case na gusto magkaron ng add button sa nav instead of fab
//                R.id.bottom_add -> {
//                    Toast.makeText(this, "Add Medication", Toast.LENGTH_SHORT).show()
//                    true
//                }
                else -> false
            }
        }
        //default fragment
        replaceFragment(HomeFragment())

    }

    //dito mangyayare yung magpapalit ng fragement based don sa clinick na icon, gumamit ng replace method para palitan yung fragment
    //yung frame_container yan yung current fragment id natin sa home.xml
    //fragment yun yung magiging new fragment based don sa ipapasang parameter
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}