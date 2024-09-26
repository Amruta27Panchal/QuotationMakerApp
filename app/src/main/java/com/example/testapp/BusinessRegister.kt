package com.example.testapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BusinessRegister : AppCompatActivity() {

    private lateinit var logoImageView: ImageView
    private lateinit var signatureImageView: ImageView

    private lateinit var logoUri: Uri
    private lateinit var signatureUri: Uri

    private lateinit var database: DatabaseReference

    // Declare EditText fields for business information
    private lateinit var businessNameEditText: EditText
    private lateinit var ownerNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var contactNumberEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var gstinEditText: EditText
    private lateinit var businessNoEditText: EditText
    private lateinit var businessCategoryEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_register)
        // Set up the action bar
        supportActionBar?.apply {
            title = "Business Regsiter"
            setDisplayHomeAsUpEnabled(true) // For back arrow
        }

        logoImageView = findViewById(R.id.Logoupload_id_updatepg)
        signatureImageView = findViewById(R.id.signtureupload_id_updatepg)

        // Initialize Firebase Realtime Database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize EditText fields
        businessNameEditText = findViewById(R.id.BusinessName_id_updatepg)
        ownerNameEditText = findViewById(R.id.OwnerName_id_updatepg)
        emailEditText = findViewById(R.id.Emailid_updatepg)
        contactNumberEditText = findViewById(R.id.contactno_id_updatepg)
        addressEditText = findViewById(R.id.Address_id_updatepg)
        gstinEditText = findViewById(R.id.gstin_id_updatepg)
        businessNoEditText = findViewById(R.id.Businessno_id_updatepg)
        businessCategoryEditText = findViewById(R.id.businesscategory_id_updatepg)

        // Set image upload listeners
        logoImageView.setOnClickListener { selectImage("logo") }
        signatureImageView.setOnClickListener { selectImage("signature") }
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
    private fun selectImage(type: String) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, if (type == "logo") 1000 else 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                when (requestCode) {
                    1000 -> {
                        logoUri = imageUri
                        logoImageView.setImageURI(logoUri)
                    }

                    1001 -> {
                        signatureUri = imageUri
                        signatureImageView.setImageURI(signatureUri)
                    }
                }
            }
        }
    }

    fun saveBusinessInfo(view: View) {
        val businessName = businessNameEditText.text.toString().trim()
        val ownerName = ownerNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val contactNumber = contactNumberEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()
        val gstin = gstinEditText.text.toString().trim()
        val businessNo = businessNoEditText.text.toString().trim()
        val businessCategory = businessCategoryEditText.text.toString().trim()

        // Validate input fields
        if (businessName.isEmpty() || ownerName.isEmpty() || contactNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in the required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the UID of the currently logged-in user
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Create a map to store business information
            val businessData = HashMap<String, Any>()
            businessData["businessName"] = businessName
            businessData["ownerName"] = ownerName
            businessData["email"] = email
            businessData["contactNumber"] = contactNumber
            businessData["address"] = address
            businessData["gstin"] = gstin
            businessData["businessNo"] = businessNo
            businessData["businessCategory"] = businessCategory

            // Save the business data in Firebase under the user's UID
            database.child("businesses").child(userId).setValue(businessData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Business info saved successfully!", Toast.LENGTH_SHORT).show()

                    // Redirect user to dashboard after successful registration
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish() // Optionally, call finish() to close the current activity
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save business info: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error generating business ID", Toast.LENGTH_SHORT).show()
        }
    }
}