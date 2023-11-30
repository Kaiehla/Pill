package com.example.pill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailSheet(private val pill: PillClass) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_sheet, container, false)
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

        DoseTime.text = pill.recur
        DoseEndDate.text = "Your medication will end until ${pill.endDate}"
        DoseTimesOfDay.text = "Your medication is scheduled at ${pill.timesOfDay}"

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


}