package com.example.testapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.EditText
import android.util.Patterns

class AddCustomerPage : AppCompatActivity() {

    // Firebase Realtime Database reference
    private lateinit var database: DatabaseReference

    private lateinit var name: EditText
    private lateinit var companyName: EditText
    private lateinit var email: EditText
    private lateinit var mobile: EditText
    private lateinit var address1: EditText
    private lateinit var address2: EditText
    private lateinit var additionalInfo: EditText
    private lateinit var addButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer_page)

        // Initialize views
        name = findViewById(R.id.cust_name)
        companyName = findViewById(R.id.company_name)
        email = findViewById(R.id.cut_pg_email)
        mobile = findViewById(R.id.cust_mobile)
        address1 = findViewById(R.id.cust_address1)
        address2 = findViewById(R.id.cust_address2)
        additionalInfo = findViewById(R.id.et_additional_info)
        addButton =
            findViewById(R.id.cust_page_add_btn) // Assuming this is a button to add customer

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("Customer")

        addButton.setOnClickListener {
            addCustomer()
        }

        // Set up the action bar
        supportActionBar?.apply {
            title = "Add Customer"
            setDisplayHomeAsUpEnabled(true) // For back arrow
        }
    }

    private fun addCustomer() {
        val customerName = name.text.toString().trim()
        val company = companyName.text.toString().trim()
        val emailValue = email.text.toString().trim()
        val mobileValue = mobile.text.toString().trim()
        val addr1 = address1.text.toString().trim()
        val addr2 = address2.text.toString().trim()
        val addInfo = additionalInfo.text.toString().trim()

        // Input validations
        if (customerName.isEmpty()) {
            Toast.makeText(this, "Please enter the customer's name", Toast.LENGTH_SHORT).show()
        }
    }
}
