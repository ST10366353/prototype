package com.risparmio.budgetapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.viewmodel.BudgetGoalViewModel

class BudgetGoalActivity : ComponentActivity() {

    private val viewModel: BudgetGoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goal)

        val totalSpentText = findViewById<TextView>(R.id.totalSpentText)
        val budgetProgressText = findViewById<TextView>(R.id.budgetProgressText)
        val minBudgetEditText = findViewById<EditText>(R.id.minBudgetEditText)
        val maxBudgetEditText = findViewById<EditText>(R.id.maxBudgetEditText)
        val saveButton = findViewById<Button>(R.id.saveBudgetGoalsButton)

        viewModel.totalSpent.observe(this, Observer {
            totalSpentText.text = "ZAR %.2f".format(it)
        })

        viewModel.budgetProgress.observe(this, Observer {
            budgetProgressText.text = it
        })

        saveButton.setOnClickListener {
            val min = minBudgetEditText.text.toString().toDoubleOrNull()
            val max = maxBudgetEditText.text.toString().toDoubleOrNull()

            if (min != null && max != null && min <= max) {
                viewModel.saveBudgetGoals(min, max)
                Toast.makeText(this, "Budget goals saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter valid min and max values", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
