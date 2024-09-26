package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View

class TermsList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_list)

        // Set up the action bar
        supportActionBar?.apply {
            title = "Terms List"
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
    fun AddTerms(view: View) {
        val i=Intent(this,AddTerms::class.java)
        startActivity(i)
    }
}