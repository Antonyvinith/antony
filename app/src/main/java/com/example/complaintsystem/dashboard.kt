package com.example.complaintsystem

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.view.View

class Dashboard : AppCompatActivity() {
    lateinit var logout:ImageView
    lateinit var create:ImageView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        var auth:FirebaseAuth=FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        logout=findViewById(R.id.logout)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        create=findViewById(R.id.CreateComplaint)
        create.setOnClickListener {
            val i= Intent(this,Createcomplaint::class.java)
            startActivity(i)
        }



        var contact:ImageView=findViewById(R.id.Contact)
        contact.setOnClickListener{

            val i= Intent(this,Contact_us::class.java)
            startActivity(i)
        }



        var more1:ImageView=findViewById(R.id.more)
        more1.setOnClickListener{

            val i= Intent(this,more::class.java)
            startActivity(i)
        }


        logout.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    val snackbar = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Logged Out",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.show()
                } else {
                    Toast.makeText(this, "Sign-out failed. Please try again.", Toast.LENGTH_SHORT).show()

                }
            }

            }
        }
    }


