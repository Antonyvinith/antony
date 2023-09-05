package com.example.complaintsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {


    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private  var allowedPassword="admin123"
    private var allowedUserId="admin@email.com"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val usernameEditText = findViewById<EditText>(R.id.inputEmail)
        val passwordEditText = findViewById<EditText>(R.id.inputPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        auth = FirebaseAuth.getInstance()





        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()



            if (username.isNotEmpty() && password.isNotEmpty()) {

                if (username == allowedUserId && password == allowedPassword) {
                    signInForAdminUser(username, password)
                }
                else
                {
                    Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                }
            }

                else {
                    Toast.makeText(this, "Please fill in fields", Toast.LENGTH_SHORT).show()
                }
            }

            val signin: ImageView = findViewById(R.id.google)
            signin.setOnClickListener {
                signIn()
            }
        }


     fun signInForAdminUser(userId:String,password:String)
    {
        auth.signInWithEmailAndPassword(userId, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser
                    startActivity(Intent(this, Admin::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Authentication failed. Check your email and password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }



    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        @Suppress("DEPRECATION")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Dashboard::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


