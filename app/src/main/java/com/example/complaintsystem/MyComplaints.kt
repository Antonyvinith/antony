    package com.example.complaintsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class MyComplaints:AppCompatActivity()
{
        lateinit var progressBar: ProgressBar
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycomplaints)
        var comp1:TextView=findViewById(R.id.comp1)
        var b:Button=findViewById(R.id.buttonClickToList)
        val comp1pic:ImageView=findViewById(R.id.comppic)
        progressBar=findViewById(R.id.progressBar2)
        progressBar.visibility=View.INVISIBLE
        b.setOnClickListener{
            progressBar.visibility=View.VISIBLE
            val delay:Long=3000

            Handler().postDelayed({

              progressBar.visibility = View.INVISIBLE


            }, delay)


        }
        val del:ImageView=findViewById(R.id.buttonDelete)
        del.setOnClickListener {
            comp1.text=""
            comp1pic.setImageResource(0)
        }













    }


}
