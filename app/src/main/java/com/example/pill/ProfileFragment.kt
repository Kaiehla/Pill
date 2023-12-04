package com.example.pill

import android.R.attr.defaultValue
import android.R.attr.key
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


class ProfileFragment : Fragment() {

    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnHistory = view.findViewById<Button>(R.id.btnHistory)
        btnHistory.setOnClickListener{
            val i = Intent(activity, DoseHistory::class.java)
            startActivity(i)
        }

        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener{
            // Assuming "myPreferences" is the name of your SharedPreferences file
            val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Clear all the data in the SharedPreferences file
            editor.clear()
            editor.apply()

            //back to Login
            val backToLogin = Intent(activity, Login::class.java)
            startActivity(backToLogin)
        }

        //get data from home activity
        //solution 1
//        val result = arguments?.getString("userFullName")
//        val tvFullName = view.findViewById<TextView>(R.id.tvFullName)
//        tvFullName.text = result.orEmpty()

        //pangcheck sa logcat
//        Log.i("ProfileFragment", "Full Name: $result")

        //solution 2 get data from login activity direct from shared pref
        // Get SharedPreferences from the hosting activity
        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Retrieve data from SharedPreferences
        val userId: Int = sharedPreferences!!.getInt("USER_ID", 0)
        val userFname: String? = sharedPreferences?.getString("USER_FNAME", "")
        val tvFullName = view.findViewById<TextView>(R.id.tvFullName)
        tvFullName.text = "${userFname.orEmpty()}"

        databaseHelper = DBHelper(requireContext())

        val btnResetAll = view.findViewById<Button>(R.id.btnResetAll)
        btnResetAll.setOnClickListener {
            // Call the deletePill function with the pillId
            val deletedRows = databaseHelper.deleteAllPillByUserId(userId)

            // Check if the deletion was successful
            if (deletedRows > 0) {
                Toast.makeText(requireContext(), "Reset All Pills Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, Home::class.java)
                startActivity(intent)
            } else {
                // Deletion was not successful, show an error message or take appropriate action
                // For example, display a toast message
                Toast.makeText(requireContext(), "Reset operation failed", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


}