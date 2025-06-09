package com.risparmio.budgetapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.risparmio.budgetapp.R


class AnalysisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        // Navigate to Budget Goals
        val viewBudgetBtn = findViewById<TextView>(R.id.viewBudgetBtn)
        viewBudgetBtn.setOnClickListener {
            val intent = Intent(this, BudgetGoalActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Category Analysis
        val viewReportBtn = findViewById<TextView>(R.id.viewReportBtn)
        viewReportBtn.setOnClickListener {
            val intent = Intent(this,
                CategoryAnalysisActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Expense History
        val viewHistoryBtn = findViewById<Button>(R.id.viewHistoryBtn)
        viewHistoryBtn.setOnClickListener {
            val intent = Intent(this, ExpenseHistoryActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Financial Reports
        val viewReportsBtn = findViewById<Button>(R.id.viewReportsBtn)
        viewReportsBtn.setOnClickListener {
            val intent = Intent(this, FinancialReportActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Spending Insights
        val viewInsightsBtn = findViewById<Button>(R.id.viewInsightsBtn)
        viewInsightsBtn.setOnClickListener {
            val intent = Intent(this, SpendingInsightsActivity::class.java)
            startActivity(intent)
        }
    }
}
