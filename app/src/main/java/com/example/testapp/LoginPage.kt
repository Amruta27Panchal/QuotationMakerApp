package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginPage : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // This hides the ActionBar
        setContentView(R.layout.activity_login_page)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Get from Firebase console
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun Signup_loginpage(view: View) {
        val i=Intent(this,UserRegister::class.java)
        startActivity(i)
    }

    fun signinwithGoggle_loginpg(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Signed in successfully
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success
                    val user = firebaseAuth.currentUser
                    saveUserToFirebase(user)
                    // Navigate to dashboard
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFirebase(user: FirebaseUser?) {
        user?.let {
            val userId = it.uid
            val userEmail = it.email
            val userName = it.displayName

            // Assuming you are using Firebase Realtime Database
            val userMap = hashMapOf(
                "userId" to userId,
                "email" to userEmail,
                "name" to userName
            )

            FirebaseDatabase.getInstance().getReference("Users").child(userId).setValue(userMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User data saved successfully
                    } else {
                        Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    fun MoveToDashboard(view: View) {
        val i = Intent(this,BusinessRegister::class.java)
        startActivity(i)
    }

}