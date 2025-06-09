package com.risparmio.budgetapp.data.model.firebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirebaseCategory(
    var id: String = "",
    var name: String = "",
    var color: String? = null
)