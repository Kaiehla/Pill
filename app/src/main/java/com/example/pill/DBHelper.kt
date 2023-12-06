package com.example.pill

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DBHelper(private val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "PillProject.db"
        private const val DATABASE_VERSION = 1

        //User Table
        private const val TABLE_USERS = "Users"
        private const val COLUMN_USER_ID = "UserId"
        private const val COLUMN_EMAIL = "Email"
        private const val COLUMN_PASSWORD = "Password"
        private const val COLUMN_FNAME = "FullName"

        // Pill table
        private const val TABLE_PILLS = "Pills"
        private const val COLUMN_PILL_ID = "PillId"
        private const val COLUMN_USER_ID_FK = "UserId" // Foreign key referencing _user_id in users table
        private const val COLUMN_PILL_TYPE = "PillType"
        private const val COLUMN_PILL_NAME = "PillName"
        private const val COLUMN_DOSAGE = "Dosage"
        private const val COLUMN_RECURRENCE = "Recurrence"
        private const val COLUMN_END_DATE = "EndDate"
        private const val COLUMN_TIMES_OF_DAY = "TimesofDay"
        private const val COLUMN_IS_TAKEN = "IsTaken"
        private const val COLUMN_MED_DATE = "MedDate"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY autoincrement, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_FNAME TEXT)")
        db?.execSQL(createTableQuery)

        val createPillsTableQuery = ("CREATE TABLE $TABLE_PILLS (" +
                "$COLUMN_PILL_ID INTEGER PRIMARY KEY autoincrement, " +
                "$COLUMN_USER_ID_FK INTEGER, " +
                "$COLUMN_PILL_TYPE INTEGER, " +
                "$COLUMN_PILL_NAME TEXT, " +
                "$COLUMN_DOSAGE TEXT, " +
                "$COLUMN_RECURRENCE TEXT, " +
                "$COLUMN_END_DATE STRING, " +
                "$COLUMN_TIMES_OF_DAY TEXT, " +
                "$COLUMN_IS_TAKEN INTEGER, " +
                "$COLUMN_MED_DATE STRING, " +
                "FOREIGN KEY($COLUMN_USER_ID_FK) REFERENCES $TABLE_USERS($COLUMN_USER_ID))")
        db?.execSQL(createPillsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropUserTableQuery = "DROP TABLE IF EXISTS $TABLE_USERS"
        val dropPillsTableQuery = "DROP TABLE IF EXISTS $TABLE_PILLS"
        db?.execSQL(dropUserTableQuery)
        db?.execSQL(dropPillsTableQuery)
        onCreate(db)
    }

    fun insertUser(user: UserClass): Long {
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, user.id)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_FNAME, user.fname)
        }
        val db = writableDatabase
        return db.insert(TABLE_USERS, null, values)
    }


    @SuppressLint("Range")
    fun readUser(data: UserClass): UserClass? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(data.email, data.password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)

        var user: UserClass? = null

        if (cursor.moveToFirst()) {
            // User found, retrieve details
            user = UserClass(
                cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                cursor.getString(cursor.getColumnIndex(COLUMN_FNAME))
            )
        }

        cursor.close()
        return user
    }

//    fun insertPill(pill: PillClass): Long {
//        val values = ContentValues().apply {
//            put(COLUMN_USER_ID_FK, pill.userId)
//            put(COLUMN_PILL_TYPE, pill.pillType)
//            put(COLUMN_PILL_NAME, pill.pillName)
//            put(COLUMN_DOSAGE, pill.dosage)
//            put(COLUMN_RECURRENCE, pill.recur) // Storing as String
//            put(COLUMN_END_DATE, pill.endDate)
//            put(COLUMN_TIMES_OF_DAY, pill.timesOfDay)
//            put(COLUMN_IS_TAKEN, if (pill.isTaken) 1 else 0)
//            put(COLUMN_MED_DATE, pill.pillDate)
//        }
//        val db = writableDatabase
//        return db.insert(TABLE_PILLS, null, values)
//    }


    @SuppressLint("Range")
    fun getAllPillsByUserId(userId: Int): List<PillClass> {
        val pillsList = mutableListOf<PillClass>()
        val selectQuery = "SELECT * FROM $TABLE_PILLS WHERE $COLUMN_USER_ID_FK = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val pill = PillClass(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PILL_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID_FK)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PILL_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PILL_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DOSAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RECURRENCE)),
                    cursor.getLong(cursor.getColumnIndex(COLUMN_END_DATE)),
                    // Convert the comma-separated string to a List<String>
                    cursor.getString(cursor.getColumnIndex(COLUMN_TIMES_OF_DAY)).split(", "),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_IS_TAKEN)) == 0,
                    cursor.getLong(cursor.getColumnIndex(COLUMN_MED_DATE))
                )
                pillsList.add(pill)
            } while (cursor.moveToNext())
        }

        cursor.close()
//        db.close()

        return pillsList
    }

    @SuppressLint("Range")
    fun getAllPillsByUserIdANDToday(userId: Int): List<PillClass> {
        val pillsList = mutableListOf<PillClass>()

        val currentDateEpoch = System.currentTimeMillis()
        val selectQuery =
            "SELECT * FROM $TABLE_PILLS WHERE $COLUMN_USER_ID_FK = ?  AND date($COLUMN_MED_DATE / 1000, 'unixepoch') = date(?, 'unixepoch')"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(userId.toString(), (currentDateEpoch / 1000).toString()))

        if (cursor.moveToFirst()) {
            do {
                val pill = PillClass(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PILL_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID_FK)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PILL_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PILL_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DOSAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RECURRENCE)),
                    cursor.getLong(cursor.getColumnIndex(COLUMN_END_DATE)),
                    // Convert the comma-separated string to a List<String>
                    cursor.getString(cursor.getColumnIndex(COLUMN_TIMES_OF_DAY)).split(", "),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_IS_TAKEN)) == 0,
                    cursor.getLong(cursor.getColumnIndex(COLUMN_MED_DATE))
                )
                pillsList.add(pill)
            } while (cursor.moveToNext())
        }

        cursor.close()
//        db.close()

        return pillsList
    }


    //
    fun deletePill(pillId: Int): Int {
        return writableDatabase.use { db ->
            db.delete(
                TABLE_PILLS,
                "$COLUMN_PILL_ID = ?",
                arrayOf(pillId.toString())
            )
        }
    }

    fun deleteAllPillByUserId(userId: Int): Int {
        return writableDatabase.use { db ->
            db.delete(
                TABLE_PILLS,
                "$COLUMN_USER_ID_FK = ?",
                arrayOf(userId.toString())
            )
        }
    }

    // Function to get all pills from the database
//    @SuppressLint("Range")
//    fun getAllPills(): List<PillClass> {
//        val pillsList = mutableListOf<PillClass>()
//        val selectQuery = "SELECT * FROM $TABLE_PILLS"
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(selectQuery, null)
//
//        if (cursor.moveToFirst()) {
//            do {
//                val pill = PillClass(
//                    cursor.getInt(cursor.getColumnIndex(COLUMN_PILL_ID)),
//                    cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID_FK)),
//                    cursor.getInt(cursor.getColumnIndex(COLUMN_PILL_TYPE)),
//                    cursor.getString(cursor.getColumnIndex(COLUMN_PILL_NAME)),
//                    cursor.getString(cursor.getColumnIndex(COLUMN_DOSAGE)),
//                    cursor.getString(cursor.getColumnIndex(COLUMN_RECURRENCE)),
//                    cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE)),
//                    cursor.getString(cursor.getColumnIndex(COLUMN_TIMES_OF_DAY)),
//                    cursor.getInt(cursor.getColumnIndex(COLUMN_IS_TAKEN)) == 0,
//                    cursor.getString(cursor.getColumnIndex(COLUMN_MED_DATE))
//                )
//                pillsList.add(pill)
//            } while (cursor.moveToNext())
//        }
//
//        cursor.close()
////        db.close()
//
//        return pillsList
//    }

    fun updatePillStatus(pillId: Int, newStatus: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_IS_TAKEN, if (newStatus) 0 else 1)

        db.update(TABLE_PILLS, values, "$COLUMN_PILL_ID = ?", arrayOf(pillId.toString()))

        db.close()
    }

    fun insertPillNew(pill: PillClass): Long {
        val db = this.writableDatabase
        val currentDateEpoch = System.currentTimeMillis()


        when (pill.recur) {
            "Daily" -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = currentDateEpoch

                while (calendar.timeInMillis <= pill.endDate) {
                    for (timeOfDay in pill.timesOfDay) {
                        val medDate = calculateMedDate(calendar.timeInMillis, timeOfDay)

                        val contentValues = ContentValues().apply {
                            put(COLUMN_USER_ID_FK, pill.userId)
                            put(COLUMN_PILL_TYPE, pill.pillType)
                            put(COLUMN_PILL_NAME, pill.pillName)
                            put(COLUMN_DOSAGE, pill.dosage)
                            put(COLUMN_RECURRENCE, pill.recur)
                            put(COLUMN_END_DATE, pill.endDate)
                            put(COLUMN_TIMES_OF_DAY, timeOfDay)
                            put(COLUMN_IS_TAKEN, if (pill.isTaken) 1 else 0)
                            put(COLUMN_MED_DATE, medDate)
                        }

                        val insertedRowId = db.insert(TABLE_PILLS, null, contentValues)

                        if (insertedRowId != -1L) {
                            // Pass the insertedRowId as the notificationID
                            scheduleNotification(insertedRowId.toInt(), medDate, pill.pillName)
                        }
                    }

                    // Move to the next day
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            "Weekly" -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = currentDateEpoch

                val selectedDaysOfWeek = listOf(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)

                while (calendar.timeInMillis <= pill.endDate) {
                    if (calendar.get(Calendar.DAY_OF_WEEK) in selectedDaysOfWeek) {
                        for (timeOfDay in pill.timesOfDay) {
                            val medDate = calculateMedDate(calendar.timeInMillis, timeOfDay)

                            val contentValues = ContentValues().apply {
                                put(COLUMN_USER_ID_FK, pill.userId)
                                put(COLUMN_PILL_TYPE, pill.pillType)
                                put(COLUMN_PILL_NAME, pill.pillName)
                                put(COLUMN_DOSAGE, pill.dosage)
                                put(COLUMN_RECURRENCE, pill.recur)
                                put(COLUMN_END_DATE, pill.endDate)
                                put(COLUMN_TIMES_OF_DAY, timeOfDay)
                                put(COLUMN_IS_TAKEN, if (pill.isTaken) 1 else 0)
                                put(COLUMN_MED_DATE, medDate)
                            }

                            val insertedRowId = db.insert(TABLE_PILLS, null, contentValues)

                            if (insertedRowId != -1L) {
                                // Pass the insertedRowId as the notificationID
                                scheduleNotification(insertedRowId.toInt(), medDate, pill.pillName)
                            }

                        }
                    }

                    // Move to the next week
                    calendar.add(Calendar.WEEK_OF_YEAR, 1)
                }
            }
            // Add more cases for other recurrence options if needed
        }

        return 1L // Return -1 if no insertion is performed
    }
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(notificationID: Int, medDate:Long, pillName: String) {
        val intent = Intent(context, NotificationService::class.java)
        val title = pillName
        val message = formatDate(medDate)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = medDate
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    fun calculateMedDate(baseDate: Long, timesOfDay: String): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = baseDate

        // Adjust med_date based on the selected time of day
        when (timesOfDay) {
            "Morning" -> calendar.set(Calendar.HOUR_OF_DAY, 6)
            "Afternoon" -> calendar.set(Calendar.HOUR_OF_DAY, 12)
            "Evening" -> calendar.set(Calendar.HOUR_OF_DAY, 18)
            "Dawn" -> calendar.set(Calendar.HOUR_OF_DAY, 23)
            // Add more cases if needed
        }

        // Set minutes and seconds to 0 for precision
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return calendar.timeInMillis
    }

    fun formatDate(dateEpoch: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateEpoch
        return sdf.format(calendar.time)
    }

    fun updatePill(pillId: Int, pillType: Int, pillName: String, pillDosage: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
//            put(COLUMN_USER_ID_FK, pill.userId)
            put(COLUMN_PILL_TYPE, pillType)
            put(COLUMN_PILL_NAME, pillName)
            put(COLUMN_DOSAGE, pillDosage)
//            put(COLUMN_RECURRENCE, pill.recur)
//            put(COLUMN_END_DATE, pill.endDate)
//            put(COLUMN_TIMES_OF_DAY, pill.timesOfDay.joinToString(", "))
//            put(COLUMN_IS_TAKEN, if (pill.isTaken) 1 else 0)
//            put(COLUMN_MED_DATE, pill.pillDate)
        }

        val updateResult = db.update(TABLE_PILLS, contentValues, "$COLUMN_PILL_ID=?", arrayOf(pillId.toString()))

        db.close()
        return updateResult
    }


}