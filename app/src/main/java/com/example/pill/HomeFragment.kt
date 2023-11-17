package com.example.pill

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class HomeFragment : Fragment() {

    //recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>
    private lateinit var myAdapter: AdapterClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewFragment = inflater.inflate(R.layout.fragment_home, container, false)


        //recycler view
        imageList = arrayOf(
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_pill
        )

        titleList = arrayOf(
            "Trixera",
            "Avene",
            "Hyabak",
            "Biogesic",
            "Allerta",
            "Zykast",
            "Montelukast",
            "Vitamin A",
            "Vitamin B",
            "Vitamin C",
            "Vitamin D",
            "Vitamin E",
        )

        recyclerView = viewFragment.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)


        dataList = arrayListOf<DataClass>()
        getData()

        //Forda onclick listener ng recycler view cards
        myAdapter = AdapterClass(dataList)
        recyclerView.adapter = myAdapter

        myAdapter.onItemClick = {
            //fragment to fragment intent
//            val fragmentB = CalendarFragment()
//            activity?.getSupportFragmentManager()?.beginTransaction()
//                ?.replace(R.id.frame_container, fragmentB, "fragmnetId")
//                ?.commit();
//            Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show()

//            activity?.supportFragmentManager?.beginTransaction()?.let {
//                    it1 -> DetailSheet().show(it1, "bottomSheet")
//            }

            val detailModal = DetailSheet()
            activity?.supportFragmentManager?.let{detailModal.show(it, "detailModalSheet")}

        }


        // Inflate the layout for this fragment
        return viewFragment
    }

    //recycler view
    private fun getData(){
        for (i in imageList.indices){
            val dataClass = DataClass(imageList[i], titleList[i])
            dataList.add(dataClass)
        }

        recyclerView.adapter = AdapterClass(dataList)
    }

}