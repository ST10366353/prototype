package com.risparmio.budgetapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.risparmio.budgetapp.adapters.MenuAdapter
import com.risparmio.budgetapp.R

class MenuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCurrency: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)



        btnCurrency.setOnClickListener {
            Toast.makeText(this, "Currency switching coming soon!", Toast.LENGTH_SHORT).show()
        }

        val menuItems = listOf(
            MenuItem("🏠", "Home", "View your dashboard and summary"),
            MenuItem("📊", "Analyze", "View spending analytics and reports"),
            MenuItem("➕", "Add Expense", "Add a new expense"),
            MenuItem("📜", "History", "View expense history"),
            MenuItem("📋", "Menu", "More options and settings")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MenuAdapter(menuItems) { item ->
            Toast.makeText(this, "${item.title} clicked", Toast.LENGTH_SHORT).show()

        }
    }
}
