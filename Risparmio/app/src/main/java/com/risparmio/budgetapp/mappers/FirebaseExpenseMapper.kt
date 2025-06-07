package com.risparmio.budgetapp.mappers

import com.risparmio.budgetapp.data.model.Expense
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun FirebaseExpense.toExpense(): Expense {
    return Expense(
        id = this.id?.toIntOrNull() ?: 0,
        date = this.date?.let { parseDate(it) } ?: Date(),
        startTime = this.startTime,
        endTime = this.endTime,
        category = this.category,
        amount = this.amount,
        description = this.description,
        imageUrl = this.imageUrl
    )
}

fun Expense.toFirebaseExpense(): FirebaseExpense {
    return FirebaseExpense(
        id = this.id.toString(),
        date = dateFormat.format(this.date),
        startTime = this.startTime,
        endTime = this.endTime,
        category = this.category,
        amount = this.amount,
        description = this.description,
        imageUrl = this.imageUrl
    )
}

private fun parseDate(dateString: String): Date {
    return dateFormat.parse(dateString) ?: Date()
}