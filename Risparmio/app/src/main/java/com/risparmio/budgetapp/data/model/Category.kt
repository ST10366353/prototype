// File: com/risparmio/budgetapp/model/ExpenseCategory.kt
package com.risparmio.budgetapp.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: String = "" // Optional field
)
