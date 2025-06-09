package com.risparmio.budgetapp.data.model

data class Challenge(
    val id: Int,
    val title: String,
    val description: String,
    val rewardPoints: Int,
    val timeRemaining: String,
    val isCompleted: Boolean

)
