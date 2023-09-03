@file:Suppress("DEPRECATION")

package com.example.complaintsystem

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.telephony.SmsManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.*
import java.text.SimpleDateFormat
import android.util.Base64

import java.util.Calendar
import java.util.Locale


class Createcomplaint:AppCompatActivity() {


    lateinit var name: EditText
    lateinit var number: EditText
    lateinit var pole: EditText
    lateinit var summary: EditText
    lateinit var upload: Button
    lateinit var p: ProgressBar
    lateinit var UserId: String
    lateinit var Address: EditText
    lateinit var selectedDivision: String
    lateinit var cat1: String
    lateinit var subdivision: String
    lateinit var nameEditText: String
    lateinit var poleEditText: String
    lateinit var AddressEditText: String
    lateinit var numberEditText: String
    lateinit var summaryEditText: String
    lateinit var phonenumberedittext:String
    var complaintCounter: Int = 0
     var ImageData: String?=null
    lateinit var complaintId: String
    lateinit var selecteddate:String
    lateinit var sharedPreferences:SharedPreferences
    lateinit var paddedCounter:String
    lateinit var phonenumber:TextInputEditText
     lateinit var selectedImageUri:Uri
    private val handler = Handler(Looper.getMainLooper())

    lateinit var database: DatabaseReference
    private var CAMERA_REQUEST = 1
    private var GALLERY_REQUEST=2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val division = findViewById<Spinner>(R.id.Division)
        val adapter =
            ArrayAdapter.createFromResource(this, R.array.division, R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        division.adapter = adapter
        division.prompt = "DIVISION"
        division.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDivision = parent?.getItemAtPosition(position).toString()

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
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                subdivision = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val cat = findViewById<Spinner>(R.id.CAT)
        val catadp =
            ArrayAdapter.createFromResource(this, R.array.CATEGORY, R.layout.simple_spinner_item)
        subadp.setDropDownViewResource(R.layout.simple_spinner_item)
        cat.adapter = catadp
        cat.prompt = "CATEGORY"
        cat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cat1 = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }




        name = findViewById(R.id.name)
        number = findViewById(R.id.number)
        pole = findViewById(R.id.pole)
        summary = findViewById(R.id.summary)
        Address = findViewById(R.id.address)
        phonenumber=findViewById(R.id.Phonenumber)



        database = Firebase.database.reference


        p = findViewById(R.id.progressBar3)
        val reg: Button = findViewById(R.id.btnSend)
        p.visibility = View.INVISIBLE


        fun showDatePickerDialog() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    val dateEditText = findViewById<EditText>(R.id.dateEditText)
                    dateEditText.setText(formattedDate)
                    this.selecteddate =dateEditText.text.toString().trim()
                },
                year, month, day
            )

            datePickerDialog.show()
        }


        val dateEditText: TextInputEditText = findViewById(R.id.dateEditText)
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }


        upload=findViewById(R.id.btnUploadImage)
        upload.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST)
        }



        reg.setOnClickListener {
            p.visibility = View.VISIBLE
            handler.postDelayed({
                p.visibility = View.INVISIBLE
                nameEditText = name.text.toString()
                if(nameEditText.isBlank())
                {
                    name.error="empty"
                }
                numberEditText = number.text.toString().trim()

                poleEditText = pole.text.toString().trim()
                summaryEditText = summary.text.toString().trim()
                AddressEditText = Address.text.toString().trim()
                phonenumberedittext=phonenumber.text.toString().trim()
                UserId = numberEditText
                writeUser(
                    UserId,
                    nameEditText,
                    AddressEditText,
                    poleEditText,
                    selectedDivision,
                    subdivision,
                    cat1,
                    summaryEditText,
                    selecteddate,
                    phonenumberedittext,
                    selectedImageUri
                )
                Toast.makeText(this,"Complaint Successfully Registered",Toast.LENGTH_LONG).show()
                val i =Intent(this,Dashboard::class.java)
                startActivity(i)


            }, 3000)
        }
         sharedPreferences = getSharedPreferences("complaintPrefs", Context.MODE_PRIVATE)
        complaintCounter = sharedPreferences.getInt("complaintCounter", 0)





    }






    data class User(val Username: String? = null,
                        val address:String?=null,
                        val poleNo:String?=null,
                        val Summary:String?=null,
                        val Division:String?=null,
                        val SubDivision:String?=null,
                        val Category:String?=null,
                        val date:String?=null,
                        val phoneno:String?=null,
                        )

        private fun writeUser(userId: String, name: String, address:String,poleNo:String,Divsion:String,SubDivision: String,Category:String,Summary:String,date:String,phonenumber:String,imagedata:Uri) {
            val user = User(name,address,poleNo,Summary,Divsion,SubDivision,Category,date,phonenumber)
            complaintCounter++
            val editor = sharedPreferences.edit()
            editor.putInt("complaintCounter", complaintCounter)
            editor.apply()
            complaintId=String.format("COMPLAINT__%05d",complaintCounter)
            paddedCounter = String.format("COMPLAINT__%05d(AccountId:%s)", complaintCounter,UserId)
            database.child("COMPLAINT_LIST").child(paddedCounter).setValue(user)
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            val imagesRef = storageRef.child("Complaint_Images/$name.jpg")

            val uploadTask = imagesRef.putFile(imagedata)


        }






    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST -> {
                    val imageData = data?.data // Get the image data from the intent
                    if (imageData != null) {
                        selectedImageUri = imageData // Assign the Uri to selectedImageUri
                    }
                }
            }
        }
    }

}



