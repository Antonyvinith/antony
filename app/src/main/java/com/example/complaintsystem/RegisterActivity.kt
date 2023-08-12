

package com.example.complaintsystem

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.os.Handler

import android.widget.Button
import android.widget.EditText


import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnRegister: Button

    private lateinit var databaseHelper: DatabaseHelper1

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        etUsername = findViewById(R.id.inputuser)
        etPassword = findViewById(R.id.inputpass)
        etPhone = findViewById(R.id.phone)
        btnRegister = findViewById(R.id.btncreate)

        this.databaseHelper = DatabaseHelper1(this)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty()) {

                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                databaseHelper.insertUser(username,password,phone)
                val delayInMillis: Long = 2000 // 2 seconds
                val handler = Handler()
                handler.postDelayed({
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                }, delayInMillis)
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }



}



class DatabaseHelper1(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME " +
                "($COL_USERNAME TEXT PRIMARY KEY, $COL_PASSWORD TEXT, $COL_PHONE TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(username: String, password: String, phone: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_USERNAME, username)
        contentValues.put(COL_PASSWORD, password)
        contentValues.put(COL_PHONE, phone)

        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }
    data class User(
        val username: String,
        val password: String,
        val phone: String
    )

    @SuppressLint("Range")
    fun getUser(username: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_USERNAME=?", arrayOf(username))

        return if (cursor.moveToFirst()) {
            val storedUsername = cursor.getString(cursor.getColumnIndex(COL_USERNAME))
            val storedPassword = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))
            val storedPhone = cursor.getString(cursor.getColumnIndex(COL_PHONE))
            User(storedUsername, storedPassword, storedPhone)
        } else {
            null
        }
    }


    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COL_USERNAME = "username"
        const val COL_PASSWORD = "password"
        private const val COL_PHONE = "phone"
    }
}

