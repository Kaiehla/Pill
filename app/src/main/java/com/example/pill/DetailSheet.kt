package com.example.pill

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailSheet(private val pill: PillClass) : BottomSheetDialogFragment() {
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_sheet, container, false)

        databaseHelper = DBHelper(requireContext())

        //getting ids from detailsheet xml
        val DoseName: TextView = view.findViewById(R.id.sheetDoseName)
        val DoseDetail: TextView = view.findViewById(R.id.sheetDoseDetail)
        val DoseImage: ImageView = view.findViewById(R.id.sheetDoseImage)
        val DoseTime: TextView = view.findViewById(R.id.sheetTime)
        val DoseEndDate: TextView = view.findViewById(R.id.sheetEndDate)
        val DoseTimesOfDay: TextView = view.findViewById(R.id.sheetTimesOfDay)


        //ids of btns
        val btnTake : Button = view.findViewById(R.id.btnTake)
        val btnEdit : Button = view.findViewById(R.id.btnEditDose)
        val btnDelete : Button = view.findViewById(R.id.btnDeleteDose)
        val btnExitDetail : ImageButton = view.findViewById(R.id.btnExitDetail)

        if (!pill.isTaken){
            btnTake.text = "Taken"
            btnTake.isEnabled = false
            btnTake.setBackgroundColor(resources.getColor(R.color.Gray))
        }

        //change the values based on pill class
        DoseName.text = pill.pillName
        DoseDetail.text = "${pill.dosage} pill, ${pill.recur}"

        val imagePillType = when (pill.pillType){
            1 -> R.drawable.icon_pill
            2 -> R.drawable.icon_capsule
            3 -> R.drawable.icon_bottle_trans
            4 -> R.drawable.icon_injection
            else -> R.drawable.icon_pill
        }
        DoseImage.setImageResource(imagePillType)

        DoseTime.text = pill.timesOfDay.toString().replace("[", "").replace("]", "")

        val epochToNormalDate = convertEpochToDate(pill.endDate)
        DoseEndDate.text = "Your medication will end until ${epochToNormalDate}"
        DoseTimesOfDay.text = "Your medication is scheduled at ${pill.timesOfDay.toString().replace("[", "").replace("]", "")} on ${convertEpochToDateWithAMPM(pill.pillDate)}"

        //buttons
        btnTake.setOnClickListener {
            // Toggle the status (1 to 0 or 0 to 1)
            val newStatus = !pill.isTaken
            Log.d("HomeFragment", "New status value: $newStatus")

            // Update the status in the database
            databaseHelper.updatePillStatus(pill.id, newStatus)
            Toast.makeText(requireContext(), "Taken Pill Successful", Toast.LENGTH_SHORT).show()
            dismiss()
            val intent = Intent(activity, Home::class.java)
            startActivity(intent)

        }
        btnDelete.setOnClickListener{
            // Call the deletePill function with the pillId
            val deletedRows = databaseHelper.deletePill(pill.id)

            // Check if the deletion was successful
            if (deletedRows > 0) {
                Toast.makeText(requireContext(), "Deleted Pill Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, Home::class.java)
                startActivity(intent)
            } else {
                // Deletion was not successful, show an error message or take appropriate action
                // For example, display a toast message
                Toast.makeText(requireContext(), "Delete operation failed", Toast.LENGTH_SHORT).show()
            }
        }

        btnExitDetail.setOnClickListener {
            dismiss()
        }

        btnEdit.setOnClickListener {

        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet : FrameLayout = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!

        // Height of the view
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        // Behavior of the bottom sheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.apply {
            peekHeight = resources.displayMetrics.heightPixels // Pop-up height
            state = BottomSheetBehavior.STATE_EXPANDED // Expanded state

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    fun convertEpochToDate(epoch: Long, pattern: String = "yyyy-MM-dd"): String {
        try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            val date = Date(epoch)
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid Date"
        }
    }

    fun convertEpochToDateWithAMPM(epoch: Long, pattern: String = "yyyy-MM-dd hh:mm a"): String {
        try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            val date = Date(epoch)
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid Date"
        }
    }


}