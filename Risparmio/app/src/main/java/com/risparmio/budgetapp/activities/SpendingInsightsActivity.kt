package com.risparmio.budgetapp.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.viewmodel.firebase.FirebaseExpenseViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class SpendingInsightsActivity : AppCompatActivity() {
    private val currencyFormatter = DecimalFormat("R#,##0.00")
    private val viewModel: FirebaseExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_insights)

        loadInsights()
    }

    private fun loadInsights() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDay = dateFormat.format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val lastDay = dateFormat.format(calendar.time)

        viewModel.getExpensesByDateRange(firstDay, lastDay).observe(this, Observer { expenses ->
            val totalSpent = expenses.sumOf { it.amount ?: 0.0 }
            val dailyAverage = if (expenses.isNotEmpty()) {
                totalSpent / expenses.map { it.date }.distinct().count()
            } else 0.0

            val mostUsedCategory = expenses
                .groupBy { it.category ?: "N/A" }
                .maxByOrNull { it.value.sumOf { e -> e.amount ?: 0.0 } }
                ?.key ?: "N/A"

            findViewById<TextView>(R.id.tvMonthlyTotal).text = currencyFormatter.format(totalSpent)
            findViewById<TextView>(R.id.tvDailyAverage).text = currencyFormatter.format(dailyAverage)
            findViewById<TextView>(R.id.tvActiveCategory).text = mostUsedCategory
        })
    }
}
