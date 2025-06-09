package com.risparmio.budgetapp.data.model.firebase
//Simplilearn. “Firebase Tutorial for Beginners 2025 | What Is Firebase? | Firebase Basics |  Simplilearn.” YouTube, 28 Feb. 2025, www.youtube.com/watch?v=_L8j-ZC83y4. Accessed 9 June 2025.
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