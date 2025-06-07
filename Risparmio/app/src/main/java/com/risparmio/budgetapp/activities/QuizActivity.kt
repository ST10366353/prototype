package com.risparmio.budgetapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.risparmio.budgetapp.R


class QuizActivity : AppCompatActivity() {

    private val allQuestions = listOf(
        QuizQuestion("A budget helps you track your spending.", true),
        QuizQuestion("You should never use a credit card.", false),
        QuizQuestion("Emergency funds should cover at least 6 months.", true),
        QuizQuestion("Buying coffee daily is a good savings plan.", false),
        QuizQuestion("Tracking subscriptions is part of budgeting.", true),
        QuizQuestion("Saving should start only after retirement.", false),
        QuizQuestion("Impulse buying affects your budget negatively.", true),
        QuizQuestion("A high salary means you don’t need a budget.", false)
    )

    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var scoreTextView: TextView

    private lateinit var questions: List<QuizQuestion>
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionTextView = findViewById(R.id.question_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        scoreTextView = findViewById(R.id.score_text_view)

        questions = allQuestions.shuffled().take(7)
        showQuestion()

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }
    }

    private fun showQuestion() {
        if (currentQuestionIndex < questions.size) {
            val q = questions[currentQuestionIndex]
            questionTextView.text = q.question
            scoreTextView.text = "Score: $score"
        } else {
            showFinalScore()
        }
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
        showQuestion()
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
                showQuestion()
            }
            .setNegativeButton("Close") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
