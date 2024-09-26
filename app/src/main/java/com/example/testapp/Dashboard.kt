package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    fun MoveToBusiness(view: View) {
        val i= Intent(this,BusinessUpdate::class.java)
        startActivity(i)
    }

    fun MoveToCustomer(view: View) {
        val i=Intent(this,Customer_List::class.java)
        startActivity(i)
    }

    fun MoveToProduct(view: View) {
        val i=Intent(this,ProductList::class.java)
        startActivity(i)
    }

    fun MoveToTerms(view: View) {
        val i=Intent(this,TermsList::class.java)
        startActivity(i)
    }
}