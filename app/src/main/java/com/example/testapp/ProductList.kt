package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View

class ProductList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        // Set up the action bar
        supportActionBar?.apply {
            title = "Product List"
            setDisplayHomeAsUpEnabled(true) // For back arrow
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

    fun Add_Product_btn(view: View) {
        val i = Intent(this,AddProduct::class.java)
        startActivity(i)
    }
}