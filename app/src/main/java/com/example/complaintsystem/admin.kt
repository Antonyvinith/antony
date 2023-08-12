package com.example.complaintsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Admin : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin)

        val comp:ImageView=findViewById(R.id.Complaint)
        comp.setOnClickListener {
            val i=Intent(this,MyComplaints::class.java)
            startActivity(i)
        }


    }
}
