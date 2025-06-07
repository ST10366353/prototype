package com.risparmio.budgetapp.data.model

import java.util.*

data class Expense(
    val id: Int,
    val date: Date,
    val startTime: String?,
    val endTime: String?,
    val category: String?,
    val amount: Double, // Matches Firebase model
    val description: String?,
    val imageUrl: String?
)
