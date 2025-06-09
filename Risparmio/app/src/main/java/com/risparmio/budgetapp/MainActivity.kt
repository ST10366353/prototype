package com.risparmio.budgetapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

import com.risparmio.budgetapp.activities.AnalysisActivity
import com.risparmio.budgetapp.activities.DashboardActivity
import com.risparmio.budgetapp.activities.SpendingInsightsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

//The activity page allows naviation to take place
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.btnDashboard).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnAnalysis).setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnSpendingInsights).setOnClickListener {
            startActivity(Intent(this, SpendingInsightsActivity::class.java))
        }
    }

    // Add to class level
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onDestroy() {
        super.onDestroy()
        scope.coroutineContext.cancelChildren()
    }
}
