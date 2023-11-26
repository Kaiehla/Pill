package com.example.pill

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        private const val COLUMN_MED_DATE = "Med Date"

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

    fun insertPill(pill: PillClass): Long {
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_FK, pill.userId)
            put(COLUMN_PILL_TYPE, pill.pillType)
            put(COLUMN_PILL_NAME, pill.pillName)
            put(COLUMN_DOSAGE, pill.dosage)
            put(COLUMN_RECURRENCE, pill.recur) // Storing as String
            put(COLUMN_END_DATE, pill.endDate)
            put(COLUMN_TIMES_OF_DAY, pill.timesOfDay)
            put(COLUMN_IS_TAKEN, if (pill.isTaken) 1 else 0)
            put(COLUMN_MED_DATE, pill.pillDate)
        }
        val db = writableDatabase
        return db.insert(TABLE_PILLS, null, values)
    }
//
//    fun getPillsByUserId(userId: Int): List<PillClass> {
//        val pills = mutableListOf<PillClass>()
//        readableDatabase.use { db ->
//            val selection = "$COLUMN_USER_ID_FK = ?"
//            val selectionArgs = arrayOf(userId.toString())
//            db.query(TABLE_PILLS, null, selection, selectionArgs, null, null, null).use { cursor ->
//                while (cursor.moveToNext()) {
//                    pills.add(cursor.toPill())
//                }
//            }
//        }
//        return pills
//    }
//
//    private fun Cursor.toPill(): PillClass {
//        return PillClass(
//            getInt(getColumnIndexOrThrow(COLUMN_PILL_ID)),
//            getInt(getColumnIndexOrThrow(COLUMN_USER_ID_FK)),
//            getInt(getColumnIndexOrThrow(COLUMN_PILL_TYPE)),
//            getString(getColumnIndexOrThrow(COLUMN_PILL_NAME)),
//            getString(getColumnIndexOrThrow(COLUMN_DOSAGE)),
//            getString(getColumnIndexOrThrow(COLUMN_RECURRENCE)),
//            getString(getColumnIndexOrThrow(COLUMN_END_DATE)),
//            getString(getColumnIndexOrThrow(COLUMN_TIMES_OF_DAY)),
//            getInt(getColumnIndexOrThrow(COLUMN_IS_TAKEN)) == 1,
//            getString(getColumnIndexOrThrow(COLUMN_MED_DATE))
//        )
//    }
//
//    fun deletePill(pillId: Long): Int {
//        return writableDatabase.use { db ->
//            db.delete(
//                TABLE_PILLS,
//                "$COLUMN_PILL_ID = ?",
//                arrayOf(pillId.toString())
//            )
//        }
//    }


}