package com.example.testapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddTerms : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var termsEditText: EditText // Reference for terms EditText
    private lateinit var descriptionEditText: EditText // Reference for description EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_terms)

        // Set up the action bar
        supportActionBar?.apply {
            title = "Add Terms and Conditions"
            setDisplayHomeAsUpEnabled(true) // For back arrow
        }
        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("Terms")

        // Initialize EditText views
        termsEditText = findViewById(R.id.product_qty) // Assuming this is the EditText for the terms
        descriptionEditText = findViewById(R.id.prod_description) // Assuming this is the EditText for the description
    }
    // Handle back arrow press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Go back to previous activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // Function to save terms data to Firebase
    fun saveTerms(view: View) {
        // Get input values
        val terms = termsEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        // Validate input
        if (terms.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        // Create a unique ID for the terms
        val termsId = database.push().key // Generate a unique ID

        // Save terms to Firebase
        if (termsId != null) {
            val termsData = Terms(termsId, terms, description) // Create a Terms object

            database.child(termsId).setValue(termsData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Terms added successfully", Toast.LENGTH_SHORT).show()
                    clearInputs() // Clear the input fields after successful addition
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add terms", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun clearInputs() {
        termsEditText.text.clear()
        descriptionEditText.text.clear()
    }
}

