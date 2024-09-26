package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Customer_List : AppCompatActivity() {

    private lateinit var customerList: ArrayList<Customer>
    private lateinit var searchView: SearchView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_list) // Ensure this layout has the correct IDs

        // Set up the action bar
        supportActionBar?.apply {
            title = "Customer List"
            setDisplayHomeAsUpEnabled(true) // For back arrow
        }

        searchView = findViewById(R.id.SearchView_add_customer)

        customerList = arrayListOf()
        database = FirebaseDatabase.getInstance().getReference("Customers")

        // Load customer data
        //loadCustomerData()
    }

    // Handle back arrow press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun AddCustomer(view: View) {
        Log.d("Customer_List", "AddCustomer button clicked")
        val i = Intent(this, AddCustomerPage::class.java)
        startActivity(i)
    }
}
