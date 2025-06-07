package com.risparmio.budgetapp.activities

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.risparmio.budgetapp.data.model.CategoryStat
import com.risparmio.budgetapp.viewmodel.CategoryAnalysisViewModel
import com.risparmio.budgetapp.viewmodel.firebase.FirebaseExpenseViewModel

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.risparmio.budgetapp.R


class CategoryAnalysisActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var viewModel: CategoryAnalysisViewModel
    private lateinit var expenseViewModel: FirebaseExpenseViewModel
    private lateinit var spinner: Spinner
    private var allExpenses: List<com.risparmio.budgetapp.data.model.firebase.FirebaseExpense> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_analysis)

        viewModel = ViewModelProvider(this)[CategoryAnalysisViewModel::class.java]
        expenseViewModel = ViewModelProvider(this)[FirebaseExpenseViewModel::class.java]

        pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)
        spinner = findViewById(R.id.timePeriodSpinner)

        val timePeriods = listOf("All Time", "This Month", "Last Month", "Last 3 Months", "Last 6 Months")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timePeriods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                filterAndComputeStats(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        expenseViewModel.allExpenses.observe(this) { expenses ->
            allExpenses = expenses
            filterAndComputeStats(spinner.selectedItemPosition)
        }

        viewModel.categoryStats.observe(this) { data ->
            updatePieChart(data)
            updateBarChart(data)
        }
    }

    private fun filterAndComputeStats(periodIndex: Int) {
        val filtered = when (periodIndex) {
            1 -> filterThisMonth(allExpenses)
            2 -> filterLastMonth(allExpenses)
            3 -> filterLastNMonths(allExpenses, 3)
            4 -> filterLastNMonths(allExpenses, 6)
            else -> allExpenses
        }
        viewModel.computeStats(filtered)
    }

    private fun filterThisMonth(expenses: List<com.risparmio.budgetapp.data.model.firebase.FirebaseExpense>): List<com.risparmio.budgetapp.data.model.firebase.FirebaseExpense> {
        val cal = java.util.Calendar.getInstance()
        val year = cal.get(java.util.Calendar.YEAR)
        val month = cal.get(java.util.Calendar.MONTH) + 1
        return expenses.filter {
            val parts = it.date?.split("-")
            parts?.size == 3 && parts[0].toInt() == year && parts[1].toInt() == month
        }
    }
    private fun filterLastMonth(expenses: List<com.risparmio.budgetapp.data.model.firebase.FirebaseExpense>): List<com.risparmio.budgetapp.data.model.firebase.FirebaseExpense> {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.MONTH, -1)
        val year = cal.get(java.util.Calendar.YEAR)
        val month = cal.get(java.util.Calendar.MONTH) + 1
        return expenses.filter {
            val parts = it.date?.split("-")
            parts?.size == 3 && parts[0].toInt() == year && parts[1].toInt() == month
        }
    }
    private fun filterLastNMonths(expenses: List<com.risparmio.budgetapp.data.model.firebase.FirebaseExpense>, n: Int): List<com.risparmio.budgetapp.data.model.firebase.FirebaseExpense> {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.MONTH, -n)
        val fromYear = cal.get(java.util.Calendar.YEAR)
        val fromMonth = cal.get(java.util.Calendar.MONTH) + 1
        return expenses.filter {
            val parts = it.date?.split("-")
            if (parts?.size == 3) {
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                (year > fromYear) || (year == fromYear && month >= fromMonth)
            } else false
        }
    }

    private fun updatePieChart(data: List<CategoryStat>) {
        val entries = data.map { PieEntry(it.amount.toFloat(), it.name) }
        val pieDataSet = PieDataSet(entries, "Spending by Category").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }

        pieChart.data = PieData(pieDataSet)
        pieChart.description = Description().apply { text = "" }
        pieChart.invalidate()
    }

    private fun updateBarChart(data: List<CategoryStat>) {
        val entries = data.mapIndexed { index, stat ->
            BarEntry(index.toFloat(), stat.amount.toFloat())
        }

        val barDataSet = BarDataSet(entries, "Category Spending").apply {
            colors = ColorTemplate.COLORFUL_COLORS.toList()
        }

        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.description = Description().apply { text = "" }
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.name })
        barChart.invalidate()
    }
}
