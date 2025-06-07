package com.risparmio.budgetapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense
import com.risparmio.budgetapp.viewmodel.firebase.FirebaseExpenseViewModel
import androidx.activity.viewModels
import com.risparmio.budgetapp.adapters.ExpenseAdapter

class ExpenseHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerExpenses: RecyclerView
    private lateinit var btnLogout: Button
    private lateinit var btnDateRange: Button
    private lateinit var btnThisMonth: Button
    private lateinit var btnLastMonth: Button
    private lateinit var spinnerCategory: Spinner
    private lateinit var searchView: SearchView
    private lateinit var emptyMessage: TextView
    private lateinit var adapter: ExpenseAdapter
    private val viewModel: FirebaseExpenseViewModel by viewModels()
    private lateinit var categoryList: List<com.risparmio.budgetapp.data.model.firebase.FirebaseCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_history)

        recyclerExpenses = findViewById(R.id.recyclerExpenses)
        btnLogout = findViewById(R.id.btnLogout)
        btnDateRange = findViewById(R.id.btnDateRange)
        btnThisMonth = findViewById(R.id.btnThisMonth)
        btnLastMonth = findViewById(R.id.btnLastMonth)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        searchView = findViewById(R.id.searchView)
        emptyMessage = findViewById(R.id.emptyMessage)

        adapter = ExpenseAdapter()
        recyclerExpenses.layoutManager = LinearLayoutManager(this)
        recyclerExpenses.adapter = adapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                if (selectedCategory == "All Categories") {
                    viewModel.allExpenses.observe(this@ExpenseHistoryActivity) { expenses ->
                        updateExpensesList(expenses)
                    }
                } else {
                    viewModel.getExpensesByCategory(selectedCategory).observe(this@ExpenseHistoryActivity) { expenses ->
                        updateExpensesList(expenses)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) filterExpensesByQuery(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.allExpenses.observe(this@ExpenseHistoryActivity) { expenses ->
                        updateExpensesList(expenses)
                    }
                } else {
                    filterExpensesByQuery(newText)
                }
                return true
            }
        })

        viewModel.allExpenses.observe(this) { expenses ->
            updateExpensesList(expenses)
        }

        viewModel.allCategories.observe(this) { categories ->
            categoryList = categories
            // Use categoryList for filtering or display as needed
        }
    }

    private fun filterExpensesByQuery(query: String) {
        viewModel.allExpenses.value?.let { allExpenses ->
            val filtered = allExpenses.filter {
                it.description?.contains(query, ignoreCase = true) == true ||
                        it.category?.contains(query, ignoreCase = true) == true
            }
            updateExpensesList(filtered)
        }
    }

    private fun updateExpensesList(expenses: List<FirebaseExpense>) {
        if (expenses.isEmpty()) {
            emptyMessage.visibility = View.VISIBLE
            recyclerExpenses.visibility = View.GONE
        } else {
            emptyMessage.visibility = View.GONE
            recyclerExpenses.visibility = View.VISIBLE
            adapter.submitList(expenses)
        }
    }
}
