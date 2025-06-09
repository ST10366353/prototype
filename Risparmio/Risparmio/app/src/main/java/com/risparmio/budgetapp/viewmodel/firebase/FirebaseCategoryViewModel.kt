package com.risparmio.budgetapp.viewmodel.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.risparmio.budgetapp.data.model.firebase.FirebaseCategory
import com.risparmio.budgetapp.repository.firebase.FirebaseCategoryRepository

class FirebaseCategoryViewModel : ViewModel() {
    private val repository = FirebaseCategoryRepository()
    val allCategories: LiveData<List<FirebaseCategory>> = repository.allCategories
    
    fun insert(category: FirebaseCategory, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        repository.insert(category, onSuccess, onFailure)
    }
    
    fun delete(categoryId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        repository.delete(categoryId, onSuccess, onFailure)
    }
}