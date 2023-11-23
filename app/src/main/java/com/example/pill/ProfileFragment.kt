package com.example.pill

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton


class ProfileFragment : Fragment() {
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

        val btnEditUser = view.findViewById<Button>(R.id.btnEditUser)
        btnEditUser.setOnClickListener{
            val i = Intent(activity, Register::class.java)
            startActivity(i)
        }

        return view
    }

}