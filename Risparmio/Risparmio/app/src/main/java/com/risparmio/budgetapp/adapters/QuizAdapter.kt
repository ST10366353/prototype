package com.risparmio.budgetapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.data.model.QuizQuestion

class QuizAdapter(
    private val questions: List<QuizQuestion>,
    private val onAnswerSelected: (Boolean) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.question_text_view)
        val trueButton: Button = view.findViewById(R.id.true_button)
        val falseButton: Button = view.findViewById(R.id.false_button)
        val scoreText: TextView = view.findViewById(R.id.score_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz_question, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val question = questions[position]
        holder.questionText.text = question.question
        
        holder.trueButton.setOnClickListener {
            onAnswerSelected(true)
        }
        
        holder.falseButton.setOnClickListener {
            onAnswerSelected(false)
        }
    }

    override fun getItemCount() = questions.size
} 