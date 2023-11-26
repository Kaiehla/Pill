package com.example.pill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.ContactsContract.Data
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DoseHistory : AppCompatActivity() {

    //recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>
    private lateinit var myAdapter: AdapterClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dose_history)

        //fab button back to home
        val fab = findViewById<FloatingActionButton>(R.id.fabBack)
        fab.setOnClickListener {
            val i = Intent(this, Home::class.java)
            startActivity(i)
        }

        //recycler view
        imageList = arrayOf(
            R.drawable.icon_pill,
            R.drawable.icon_cal_filled,
            R.drawable.icon_pill,
            R.drawable.icon_pill,
            R.drawable.icon_uncheck,
            R.drawable.icon_home_filled,
            R.drawable.icon_pill,
            R.drawable.icon_uncheck,
            R.drawable.icon_pill,
            R.drawable.icon_home_filled,
            R.drawable.icon_uncheck,
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


        recyclerView = findViewById(R.id.historyRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


        dataList = arrayListOf<DataClass>()
        getData()
    }


    //recycler view
    private fun getData(){
        for (i in imageList.indices){
            val dataClass = DataClass(imageList[i], titleList[i])
            dataList.add(dataClass)
        }

        //onitem click listener ni recycler view
        val adapter = AdapterClass(dataList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : AdapterClass.onItemClickListener{
            override fun onItemClick(position: Int) {
                //dito pinapasa yung data from recyclerview to detail sheet dahil sa datalist parameter
                val detailModal = DetailSheet(dataList[position])
                detailModal.show(supportFragmentManager, "detailModalSheet")
            }
        })


    }

}