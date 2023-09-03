    package com.example.complaintsystem

    import android.annotation.SuppressLint
    import android.app.Activity
    import android.app.AlertDialog
    import android.content.Intent
    import android.graphics.Bitmap
    import android.graphics.BitmapFactory
    import android.os.Bundle
    import android.provider.MediaStore
    import android.util.Base64
    import android.util.Log
    import android.widget.LinearLayout
    import androidx.appcompat.app.AppCompatActivity
    import android.widget.TextView
    import android.widget.Toast
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.IgnoreExtraProperties
    import com.google.firebase.database.Query
    import com.google.firebase.database.ValueEventListener
    import com.google.firebase.database.ktx.database
    import com.google.firebase.ktx.Firebase
    import java.lang.StringBuilder
    import android.content.Context

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.ImageView
    import android.widget.RelativeLayout
    import android.widget.ScrollView
    import androidx.cardview.widget.CardView
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.firebase.storage.FirebaseStorage
    import com.google.firebase.storage.StorageReference
    import java.util.Locale.Category
    import com.bumptech.glide.Glide
    import java.io.File


    class MyComplaints : AppCompatActivity() {

        private lateinit var linearLayout: LinearLayout
        private lateinit var displayimage:ImageView
        private  var image:String?=null
        private lateinit var database: DatabaseReference

        @SuppressLint("InflateParams")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.insertlayout)





            displayimage=findViewById(R.id.Complaint_Image)
            linearLayout= findViewById(R.id.dynamic)

             database = FirebaseDatabase.getInstance().reference.child("COMPLAINT_LIST")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (complaintSnapshot in snapshot.children) {
                        val complaint = complaintSnapshot.value as Map<*, *>

                        addComplaintToLayout(complaint,complaintSnapshot.key)

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })




        }

        @SuppressLint("SetTextI18n", "InflateParams")
        private fun addComplaintToLayout(complaint: Map<*, *>,title:String?) {

            val cardView = layoutInflater.inflate(R.layout.insertlayout, null) as ScrollView
            val textViewTitle = cardView.findViewById<TextView>(R.id.ComplaintDescriptionTitle)

            val ImageData=cardView.findViewById<ImageView>(R.id.Complaint_Image)
            val numbertext=cardView.findViewById<TextView>(R.id.Phonenumber)
            val datetext=cardView.findViewById<TextView>(R.id.Date)
            val nametext=cardView.findViewById<TextView>(R.id.Name)
            val divisiontext=cardView.findViewById<TextView>(R.id.Division)
            val addresstext=cardView.findViewById<TextView>(R.id.Address)
            val summarytext=cardView.findViewById<TextView>(R.id.Details)
            val Categorytext=cardView.findViewById<TextView>(R.id.Category)

            val username = complaint["username"] as? String

            val address = complaint["address"] as? String
            val category = complaint["category"] as? String
            val date = complaint["date"] as? String
            val division = complaint["division"] as? String
            val sub_division = complaint["subDivision"] as? String
            val Phoneno = complaint["phoneno"] as? String
            val summary = complaint["summary"] as? String

            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.reference






            val imageRef = storageReference.child("Complaint_Images/$username.jpg")
            val maxDownloadSizeBytes: Long = Long.MAX_VALUE
            imageRef.getBytes(maxDownloadSizeBytes)
                .addOnSuccessListener { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    ImageData.setImageBitmap(bitmap)
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error downloading image: ${exception.message}")
                }


           /* imageRef.getFile(localFile)
                .addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                    displayimage.setImageBitmap(bitmap)eListener { exception ->

                    Log.e("Firebase", "Error downloading image: ${exception.message}")
                }*/




            Categorytext.text=category.toString()
            textViewTitle.text=title.toString()
            numbertext.text=Phoneno.toString()
            datetext.text=date.toString()
            nametext.text=username.toString()
            addresstext.text=address.toString()
            divisiontext.text= "$division,$sub_division"
            summarytext.text=summary


            linearLayout.addView(cardView)
        }


    }

