package com.example.testapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BusinessUpdate : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private lateinit var businessNameEditText: EditText
    private lateinit var ownerNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var contactNumberEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var gstinEditText: EditText
    private lateinit var businessNoEditText: EditText
    private lateinit var businessCategoryEditText: EditText

    private lateinit var businessName: String // To hold the passed business name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_update)

        // Set up the action bar
        supportActionBar?.apply {
            title = "Update Business Info"
            setDisplayHomeAsUpEnabled(true) // For back arrow
        }

        // Initialize Firebase Realtime Database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize EditText fields
        businessNameEditText = findViewById(R.id.business_name)
        ownerNameEditText = findViewById(R.id.owner_name)
        emailEditText = findViewById(R.id.email_id)
        contactNumberEditText = findViewById(R.id.contact_no)
        addressEditText = findViewById(R.id.address)
        gstinEditText = findViewById(R.id.gstin_pan)
        businessNoEditText = findViewById(R.id.business_register)
        businessCategoryEditText = findViewById(R.id.business_category)

        // Get the business name from the Intent
        businessName = intent.getStringExtra("BUSINESS_NAME") ?: ""

        // Fetch the existing business data
        if (businessName.isNotEmpty()) {
            fetchBusinessInfo(businessName)
        } else {
            Toast.makeText(this, "No business name provided", Toast.LENGTH_SHORT).show()
        }
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

    private fun fetchBusinessInfo(businessName: String) {
        // Get the UID of the currently logged-in user
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val businessRef = database.child("businesses").child(userId)

            businessRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Populate the EditText fields with the fetched data
                        businessNameEditText.setText(snapshot.child("businessName").value.toString())
                        ownerNameEditText.setText(snapshot.child("ownerName").value.toString())
                        emailEditText.setText(snapshot.child("email").value.toString())
                        contactNumberEditText.setText(snapshot.child("contactNumber").value.toString())
                        addressEditText.setText(snapshot.child("address").value.toString())
                        gstinEditText.setText(snapshot.child("gstin").value.toString())
                        businessNoEditText.setText(snapshot.child("businessNo").value.toString())
                        businessCategoryEditText.setText(snapshot.child("businessCategory").value.toString())
                    } else {
                        Toast.makeText(this@BusinessUpdate, "Business info not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@BusinessUpdate, "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateBusinessInfo(view: View) {
        val updatedBusinessName = businessNameEditText.text.toString().trim()
        val updatedOwnerName = ownerNameEditText.text.toString().trim()
        val updatedEmail = emailEditText.text.toString().trim()
        val updatedContactNumber = contactNumberEditText.text.toString().trim()
        val updatedAddress = addressEditText.text.toString().trim()
        val updatedGstin = gstinEditText.text.toString().trim()
        val updatedBusinessNo = businessNoEditText.text.toString().trim()
        val updatedBusinessCategory = businessCategoryEditText.text.toString().trim()

        // Validate input fields
        if (updatedBusinessName.isEmpty() || updatedOwnerName.isEmpty() || updatedContactNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in the required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a map to store updated business information
        val updatedBusinessData = HashMap<String, Any>()
        updatedBusinessData["businessName"] = updatedBusinessName
        updatedBusinessData["ownerName"] = updatedOwnerName
        updatedBusinessData["email"] = updatedEmail
        updatedBusinessData["contactNumber"] = updatedContactNumber
        updatedBusinessData["address"] = updatedAddress
        updatedBusinessData["gstin"] = updatedGstin
        updatedBusinessData["businessNo"] = updatedBusinessNo
        updatedBusinessData["businessCategory"] = updatedBusinessCategory

        // Update the business data in Firebase
        database.child("businesses").child(businessName).updateChildren(updatedBusinessData)
            .addOnSuccessListener {
                Toast.makeText(this, "Business info updated successfully!", Toast.LENGTH_SHORT).show()
                // Optionally, redirect to another activity or finish the current one
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update business info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
