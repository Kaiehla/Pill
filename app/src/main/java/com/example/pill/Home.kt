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

        //get data from login
        val intent = intent
        val userId = intent.getIntExtra("id", 0)
        val userEmail = intent.getStringExtra("email")
        val userFName = intent.getStringExtra("fname")

        //assign the data to the bundle for passing of data to home and profile fragments
        //solution1
//        val bundle = Bundle()
//        bundle.putInt("userId", userId)
//        bundle.putString("userEmail", userEmail)
//        bundle.putString("userFullName", userFName)

        //nasa home.xml to eto yung mismong bottom nav natin
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.itemActiveIndicatorColor = getColorStateList(R.color.white)

        //menuItem is associated with itemId
        //switch statement lang din yung when
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    val hfragment = HomeFragment()
                    //pass data to home fragment
//                    hfragment.arguments = bundle
                    replaceFragment(hfragment)
                    true
                }
                R.id.bottom_profile -> {
                    val pfragment = ProfileFragment()
                    //pass data to profile fragment
//                    pfragment.arguments = bundle

                    replaceFragment(pfragment)
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
            val addDoseIntent = Intent(this, AddDose::class.java)
            startActivity(addDoseIntent)
        }



        //pass data to profilefragment
//            val fragment = ProfileFragment()
//            val bundle = Bundle()
//            bundle.putString("fullName", userFName)
//            fragment.arguments = bundle
//            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragment).commit()

    }

    //dito mangyayare yung magpapalit ng fragement based don sa clinick na icon, gumamit ng replace method para palitan yung fragment
    //yung frame_container yan yung current fragment id natin sa home.xml
    //fragment yun yung magiging new fragment based don sa ipapasang parameter
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}