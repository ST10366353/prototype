package com.risparmio.budgetapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense

class ExpenseAdapter : ListAdapter<FirebaseExpense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val imgExpense: ImageView = itemView.findViewById(R.id.imgExpense)

        fun bind(expense: FirebaseExpense) {
            tvCategory.text = "Category: ${expense.category}"
            tvDate.text = "Date: ${expense.date}"
            tvAmount.text = "Amount: ${expense.amount}"

            if (!expense.imageUrl.isNullOrEmpty()) {
                imgExpense.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(expense.imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgExpense)
            } else {
                imgExpense.visibility = View.GONE
            }
        }
    }

    private class ExpenseDiffCallback : DiffUtil.ItemCallback<FirebaseExpense>() {
        override fun areItemsTheSame(oldItem: FirebaseExpense, newItem: FirebaseExpense): Boolean {
            return oldItem.id == newItem.id // Assuming FirebaseExpense has an 'id' field
        }

        override fun areContentsTheSame(oldItem: FirebaseExpense, newItem: FirebaseExpense): Boolean {
            return oldItem == newItem
        }
    }
}
