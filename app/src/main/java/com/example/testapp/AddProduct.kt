package com.example.testapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddProduct : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productQty: EditText
    private lateinit var productDescription: EditText
    private lateinit var add_prod:ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Set up the action bar
        supportActionBar?.apply {
            title = "Add Product"
            setDisplayHomeAsUpEnabled(true) // For back arrow
        }

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("Product")

        // Initialize EditText views
        productName = findViewById(R.id.prod_name)
        productPrice = findViewById(R.id.prod_Price)
        productQty = findViewById(R.id.product_qty)
        productDescription = findViewById(R.id.prod_description)
        add_prod=findViewById(R.id.add_prod_btn_addpage)
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

    // Function to add product to Firebase
    fun addProduct(view: View) {
        // Get input values
        val name = productName.text.toString().trim()
        val price = productPrice.text.toString().toDoubleOrNull()
        val quantity = productQty.text.toString().toIntOrNull()
        val description = productDescription.text.toString().trim()

        // Validate input
        if (name.isEmpty() || price == null || quantity == null || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Additional validations
        if (price <= 0) {
            Toast.makeText(this, "Price must be a positive number", Toast.LENGTH_SHORT).show()
            return
        }
        if (quantity <= 0) {
            Toast.makeText(this, "Quantity must be a positive number", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a product object
        val productId = database.push().key // Generate a unique ID

        // Save product to Firebase
        if (productId != null) {
            val product = Product(productId, name, price, quantity, description) // Create Product object

            database.child(productId).setValue(product)
                .addOnSuccessListener {
                    Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                    clearInputs() // Clear the input fields after successful addition
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun clearInputs() {
        productName.text.clear()
        productPrice.text.clear()
        productQty.text.clear()
        productDescription.text.clear()
    }

}
