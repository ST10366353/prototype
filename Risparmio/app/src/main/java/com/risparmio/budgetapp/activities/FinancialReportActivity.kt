package com.risparmio.budgetapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.risparmio.budgetapp.databinding.ActivityFinancialReportBinding
import com.risparmio.budgetapp.viewmodel.firebase.FirebaseExpenseViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class FinancialReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinancialReportBinding
    private val viewModel: FirebaseExpenseViewModel by viewModels()
    private val decimalFormat = DecimalFormat("#,##0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinancialReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFinancialReport()
    }

    private fun setupFinancialReport() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val currentMonth = monthFormat.format(Date())

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDay = dateFormat.format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val lastDay = dateFormat.format(calendar.time)

        viewModel.getExpensesByDateRange(firstDay, lastDay).observe(this, Observer { expenses ->
            val monthlyTotal = expenses.sumOf { it.amount ?: 0.0 }
            val formatted = decimalFormat.format(monthlyTotal)

            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val dailyAverage = if (daysInMonth > 0) monthlyTotal / daysInMonth else 0.0

            val dailySpending = mutableMapOf<String, Double>()
            for (expense in expenses) {
                val day = expense.date?.substring(8, 10) ?: "--"
                dailySpending[day] = dailySpending.getOrDefault(day, 0.0) + (expense.amount ?: 0.0)
            }

            val highestSpendingEntry = dailySpending.maxByOrNull { it.value }
            val highestDay = highestSpendingEntry?.key ?: "--"
            val highestAmount = highestSpendingEntry?.value ?: 0.0

            val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())

            binding.tvHighestSpendingDay.text = "R${decimalFormat.format(highestAmount)}"
            binding.tvHighestSpendingDayDesc.text = "On $highestDay $monthName"
            binding.tvDailyAverage.text = "R${decimalFormat.format(dailyAverage)}"
            binding.tvDailyAverageDesc.text = "Average daily spending this month"

            if (expenses.isNotEmpty()) {
                val sortedExpenses = expenses.sortedByDescending { it.date }
                val recentExpense = sortedExpenses.first()
                binding.tvMostRecentTransaction.text = "R${decimalFormat.format(recentExpense.amount ?: 0.0)}"
                binding.tvMostRecentTransactionDesc.text = recentExpense.date?.let {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        .format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)!!)
                } ?: ""
            } else {
                binding.tvMostRecentTransaction.text = "No transactions this month"
                binding.tvMostRecentTransactionDesc.text = ""
            }
        })
    }
}
