package com.risparmio.budgetapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.adapters.QuizAdapter
import com.risparmio.budgetapp.data.model.QuizQuestion

class QuizActivity : AppCompatActivity() {

    private val allQuestions = listOf(
        QuizQuestion("A budget helps you track your spending.", true),
        QuizQuestion("You should never use a credit card.", false),
        QuizQuestion("Emergency funds should cover at least 6 months.", true),
        QuizQuestion("Buying coffee daily is a good savings plan.", false),
        QuizQuestion("Tracking subscriptions is part of budgeting.", true),
        QuizQuestion("Saving should start only after retirement.", false),
        QuizQuestion("A budget can help you achieve long-term financial goals.", true),
        QuizQuestion("You should spend all your income each month for a balanced budget.", false),
        QuizQuestion("Setting aside money for taxes is part of budgeting.", true),
        QuizQuestion("Budgets are only useful for large expenses, not small ones.", false),
        QuizQuestion("Using budgeting apps can simplify tracking expenses.", true),
        QuizQuestion("It's okay to ignore small expenses in a budget.", false),
        QuizQuestion("A budget should include money for leisure activities.", true),
        QuizQuestion("You don't need a budget if you have no debt.", false),
        QuizQuestion("Adjusting your budget after life changes is necessary.", true),
        QuizQuestion("Saving 10% of your income is unrealistic for most people.", false),
        QuizQuestion("A budget helps you avoid overspending on credit cards.", true),
        QuizQuestion("Budgets are only for people who are bad with money.", false),
        QuizQuestion("Planning for irregular expenses is part of budgeting.", true),
        QuizQuestion("Impulse buying affects your budget negatively.", true),
        QuizQuestion("A high salary means you don't need a budget.", false)
    )

    private lateinit var recyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter
    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var questions: List<QuizQuestion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        recyclerView = findViewById(R.id.quiz_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        questions = allQuestions.shuffled().take(7)
        setupQuiz()
    }

    private fun setupQuiz() {
        quizAdapter = QuizAdapter(questions) { userAnswer ->
            checkAnswer(userAnswer)
        }
        recyclerView.adapter = quizAdapter
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questions[currentQuestionIndex].answer
        if (userAnswer == correctAnswer) {
            score++
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }
        currentQuestionIndex++
        
        if (currentQuestionIndex >= questions.size) {
            showFinalScore()
        } else {
            recyclerView.smoothScrollToPosition(currentQuestionIndex)
        }
    }

    private fun showFinalScore() {
        val message = "You scored $score out of ${questions.size}"
        AlertDialog.Builder(this)
            .setTitle("Quiz Finished")
            .setMessage(message)
            .setPositiveButton("Retry") { _, _ ->
                score = 0
                currentQuestionIndex = 0
                questions = allQuestions.shuffled().take(7)
                setupQuiz()
            }
            .setNegativeButton("Close") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
