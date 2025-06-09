package com.risparmio.budgetapp.data.model

data class CategoryStat(
    val name: String,
    val amount: Double,
    val minGoal: Double = 0.0,
    val maxGoal: Double = 0.0
)