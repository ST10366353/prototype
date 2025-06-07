package com.risparmio.budgetapp.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.adapters.ExpenseAdapter
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense
import com.risparmio.budgetapp.data.model.firebase.FirebaseCategory
import com.risparmio.budgetapp.viewmodel.firebase.FirebaseExpenseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.text.DecimalFormat

class DashboardActivity : AppCompatActivity() {
    private val addExpenseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            refreshDashboard()
        }
    }

    private lateinit var btnAddExpense: MaterialButton
    private lateinit var btnAchievements: MaterialButton
    private lateinit var btnCurrency: MaterialButton
    private lateinit var txtLevel: TextView
    private lateinit var txtTotalSpent: TextView
    private lateinit var btnNavHome: MaterialButton
    private lateinit var btnNavAnalyze: MaterialButton
    private lateinit var btnNavAdd: MaterialButton
    private lateinit var btnNavHistory: MaterialButton
    private lateinit var btnNavChallenges: MaterialButton
    private lateinit var preferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var viewModel: FirebaseExpenseViewModel
    private lateinit var categoryList: List<FirebaseCategory>

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val currencyOptions = arrayOf(
        "🇿🇦 ZAR", "🇺🇸 USD", "🇪🇺 EUR", "🇬🇧 GBP", "🇯🇵 JPY"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewModel = ViewModelProvider(this).get(FirebaseExpenseViewModel::class.java)

        recyclerView = findViewById(R.id.rvExpenses)
        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter

        setupViews()
        setupListeners()
        setupSharedPreferences()
        updateCurrencyButton()
        refreshDashboard()

        viewModel.allExpenses.observe(this, Observer { expenses ->
            expenseAdapter.submitList(expenses)
            val totalSpent = expenses.sumOf { expense -> expense.amount ?: 0.0 }
            txtTotalSpent.text = formatCurrency(totalSpent, preferences.getString("currency", "🇿🇦 ZAR"))
        })

        viewModel.allCategories.observe(this) { categories ->
            categoryList = categories
            // Use categoryList for filtering or display as needed
        }
    }

    private fun setupViews() {
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnAchievements = findViewById(R.id.btnAchievements)
        btnCurrency = findViewById(R.id.btnCurrency)
        txtLevel = findViewById(R.id.txtLevel)
        txtTotalSpent = findViewById(R.id.txtTotalSpent)
        btnNavHome = findViewById(R.id.btnNavHome)
        btnNavAnalyze = findViewById(R.id.btnNavAnalyze)
        btnNavAdd = findViewById(R.id.btnNavAdd)
        btnNavHistory = findViewById(R.id.btnNavHistory)
        btnNavChallenges = findViewById(R.id.btnNavChallenges)
    }

    private fun setupListeners() {
        btnAddExpense.setOnClickListener {
            addExpenseLauncher.launch(Intent(this, AddExpenseActivity::class.java))
        }

        btnAchievements.setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }

        btnCurrency.setOnClickListener {
            showCurrencySelectionDialog()
        }

        btnNavHome.setOnClickListener {
            Toast.makeText(this, "You're already on Dashboard", Toast.LENGTH_SHORT).show()
        }

        btnNavAnalyze.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        btnNavAdd.setOnClickListener {
            addExpenseLauncher.launch(Intent(this, AddExpenseActivity::class.java))
        }

        btnNavHistory.setOnClickListener {
            startActivity(Intent(this, ExpenseHistoryActivity::class.java))
        }

        btnNavChallenges.setOnClickListener {
            startActivity(Intent(this, ChallengesActivity::class.java))
        }
    }

    private fun setupSharedPreferences() {
        preferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    private fun showCurrencySelectionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Select Currency")
            .setItems(currencyOptions) { _: DialogInterface, which: Int ->
                val selectedCurrency = currencyOptions[which]
                preferences.edit().putString("currency", selectedCurrency).apply()
                updateCurrencyButton()
                refreshDashboard()
            }
            .create()
            .show()
    }

    private fun updateCurrencyButton() {
        val savedCurrency = preferences.getString("currency", "🇿🇦 ZAR")
        btnCurrency.text = savedCurrency
    }

    private fun refreshDashboard() {
        // Data refresh handled by LiveData observer
    }

    private fun formatCurrency(amount: Double, currency: String?): String {
        val decimalFormat = DecimalFormat("#,##0.00")
        return when (currency) {
            "🇿🇦 ZAR" -> "ZAR ${decimalFormat.format(amount)}"
            "🇺🇸 USD" -> "USD ${decimalFormat.format(amount)}"
            "🇪🇺 EUR" -> "EUR ${decimalFormat.format(amount)}"
            else -> "${currency ?: ""} ${decimalFormat.format(amount)}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.cancel()
    }
}