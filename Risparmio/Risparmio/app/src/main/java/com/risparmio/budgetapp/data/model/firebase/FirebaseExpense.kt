package com.risparmio.budgetapp.data.model.firebase

data class FirebaseExpense(
    val id: String? = null,
    val amount: Double = 0.0,
    val category: String? = null,
    val date: String? = null,
    val description: String? = null,
    val endTime: String? = null,
    val imageUrl: String? = null,
    val startTime: String? = null
)