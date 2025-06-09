package com.risparmio.budgetapp.data.model.firebase
//Simplilearn. “Firebase Tutorial for Beginners 2025 | What Is Firebase? | Firebase Basics |  Simplilearn.” YouTube, 28 Feb. 2025, www.youtube.com/watch?v=_L8j-ZC83y4. Accessed 2 June 2025.
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirebaseCategory(
    var id: String = "",
    var name: String = "",
    var color: String? = null
)