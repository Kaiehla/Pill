package com.example.pill


//data class PillClass(var dataImage:Int, var dataTitle:String)
data class PillClass(
    val id: Int,
    val userId: Int,
    val pillType: Int, //1=round , 2=capsule, 3=bottle, 4=injection
    val pillName: String,
    val dosage: String?,
    val recur: String,
    val endDate: Long, //Epoch datetime when user will end the medicine intake
    val timesOfDay: List<String>,
    var isTaken: Boolean,
    val pillDate: Long, //Epoch datetime when the user must take the medicine
)


