package com.risparmio.budgetapp.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.adapters.ChallengeAdapter
import com.risparmio.budgetapp.data.model.Challenge

class ChallengesActivity : AppCompatActivity() {

    private lateinit var recyclerChallenges: RecyclerView
    private lateinit var challengeAdapter: ChallengeAdapter
    private lateinit var searchView: SearchView

    private var challenges = listOf(
        Challenge(
            id = 1,
            title = "Daily Tracker",
            description = "Track expenses for 3 consecutive days",
            rewardPoints = 100,
            timeRemaining = "2 days left",
            isCompleted = false
        ),
        Challenge(
            id = 2,
            title = "Category Explorer",
            description = "Add 5 different categories",
            rewardPoints = 150,
            timeRemaining = "5 days left",
            isCompleted = true
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges)

        recyclerChallenges = findViewById(R.id.recyclerChallenges)
        recyclerChallenges.layoutManager = LinearLayoutManager(this)

        searchView = findViewById(R.id.searchView)
        challengeAdapter = ChallengeAdapter(challenges)
        recyclerChallenges.adapter = challengeAdapter

        setupSearchFilter()

        //  Spending goal input logic
        val minGoalInput = findViewById<EditText>(R.id.etMinGoal)
        val maxGoalInput = findViewById<EditText>(R.id.etMaxGoal)
        val saveButton = findViewById<Button>(R.id.btnSaveSpendingGoal)

        // Load saved values from SharedPreferences
        val prefs = getSharedPreferences("SpendingGoals", MODE_PRIVATE)
        val savedMin = prefs.getFloat("minGoal", -1f)
        val savedMax = prefs.getFloat("maxGoal", -1f)

        if (savedMin >= 0f) minGoalInput.setText(savedMin.toString())
        if (savedMax >= 0f) maxGoalInput.setText(savedMax.toString())

        saveButton.setOnClickListener {
            val minGoal = minGoalInput.text.toString().toDoubleOrNull()
            val maxGoal = maxGoalInput.text.toString().toDoubleOrNull()

            if (minGoal == null || maxGoal == null) {
                Toast.makeText(this, "Please enter valid amounts.", Toast.LENGTH_SHORT).show()
            } else if (minGoal > maxGoal) {
                Toast.makeText(this, "Minimum can't be more than maximum.", Toast.LENGTH_SHORT).show()
            } else {
                prefs.edit()
                    .putFloat("minGoal", minGoal.toFloat())
                    .putFloat("maxGoal", maxGoal.toFloat())
                    .apply()

                Toast.makeText(this, "Goals saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSearchFilter() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = challenges.filter {
                    it.title.contains(newText ?: "", ignoreCase = true)
                }
                challengeAdapter.updateList(filtered)
                return true
            }
        })
    }
}
