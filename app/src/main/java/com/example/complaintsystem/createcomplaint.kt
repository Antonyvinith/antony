@file:Suppress("DEPRECATION")

package com.example.complaintsystem

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Properties
import javax.mail.*
import android.graphics.BitmapFactory
import java.io.*
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage






class Createcomplaint:AppCompatActivity() {


    lateinit var name: EditText
    lateinit var number: EditText
    lateinit var pole: EditText
    lateinit var summary: EditText
    lateinit var upload: Button
    lateinit var session: Session
    lateinit var p:ProgressBar

    lateinit var database:DatabaseReference
    private var CAMERA_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val division = findViewById<Spinner>(R.id.Division)
        val adapter = ArrayAdapter.createFromResource(this, R.array.division, R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        division.adapter = adapter
        division.prompt = "DIVISION"
        division.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDivision = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }




        val sub = findViewById<Spinner>(R.id.SUB)
        val subadp =
            ArrayAdapter.createFromResource(this, R.array.sub, R.layout.simple_spinner_item)
        subadp.setDropDownViewResource(R.layout.simple_spinner_item)
        sub.adapter = subadp
        sub.prompt = "SUB-DIVISION"
        sub.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val subdivision = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val cat = findViewById<Spinner>(R.id.CATEGORY)
        val catadp =
            ArrayAdapter.createFromResource(this, R.array.CATEGORY, R.layout.simple_spinner_item)
        subadp.setDropDownViewResource(R.layout.simple_spinner_item)
        cat.adapter = catadp
        cat.prompt = "CATEGORY"
        cat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val cat1 = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }




        name = findViewById(R.id.name)
        number = findViewById(R.id.number)
        pole = findViewById(R.id.pole)
        summary = findViewById(R.id.summary)





        upload = findViewById(R.id.btnUploadImage)
        upload.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)


        }
        val nameEditText = name.text.toString()
        val numberEditText = number.text.toString()
        val poleEditText = pole.text.toString()
        val summaryEditText = summary.text.toString()

        database    = FirebaseDatabase.getInstance().getReference("Complaint")
        var compid=database.push().key!!



        p=findViewById(R.id.progressBar3)
        p.visibility=View.INVISIBLE
        val reg: Button = findViewById(R.id.btnSend)
        reg.setOnClickListener {
            p.visibility = View.VISIBLE

            Handler().postDelayed({
                p.visibility = View.INVISIBLE
                    val complaint=complaint("Antony")
                    database.child(compid).setValue(complaint)

                                  }, 3000)
        }







    }

    data class complaint(
        var name:String?=null
    )

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {
            val imageBitmap: Bitmap? = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {

            }
        }
    }


}



