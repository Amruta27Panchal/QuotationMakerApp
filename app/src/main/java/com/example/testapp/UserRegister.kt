package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRegister : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var showHidePasswordBtn: ImageView
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        // Initialize EditText fields
        nameEditText = findViewById(R.id.enterName_id)
        emailEditText = findViewById(R.id.enterEmail_id)
        passwordEditText = findViewById(R.id.enterPassword_id_registerpg)
        mobileEditText = findViewById(R.id.enterMobile_id)
        showHidePasswordBtn = findViewById(R.id.passwordToggle)

        // Initialize Firebase Database and Auth reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        firebaseAuth = FirebaseAuth.getInstance()

        // Handle password visibility toggle
        showHidePasswordBtn.setOnClickListener {
            togglePasswordVisibility()
        }
    }


    fun BackToLogin(view: View) {
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
    }

    fun movetoBusinessPage(view: View) {
        saveUserToFirebase() // Call this method on button click
    }

    private fun saveUserToFirebase() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val mobile = mobileEditText.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User registered successfully
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    user?.let {
                        saveUserDetails(it.uid, name, email, mobile)
                    }
                } else {
                    Toast.makeText(this, "Failed to register user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun validatePassword(password: String): Boolean {
        // Password should be at least 6 characters long, contain numbers and special characters
        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{6,}$")
        return password.matches(passwordRegex)
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            showHidePasswordBtn.setImageResource(R.drawable.baseline_visibility_off_24) // Closed eye icon
        } else {
            // Show password
            passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            showHidePasswordBtn.setImageResource(R.drawable.baseline_visibility_24) // Open eye icon
        }
        isPasswordVisible = !isPasswordVisible

        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun saveUserDetails(userId: String, name: String, email: String, mobile: String) {
        // Create a user map to save
        val userMap = hashMapOf(
            "userId" to userId,
            "name" to name,
            "email" to email,
            "mobile" to mobile
        )

        // Save to Firebase Realtime Database using the user ID
        databaseReference.child(name).setValue(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,BusinessRegister::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
