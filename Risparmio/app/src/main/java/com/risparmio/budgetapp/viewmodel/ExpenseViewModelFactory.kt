package com.risparmio.budgetapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.risparmio.budgetapp.viewmodel.firebase.FirebaseExpenseViewModel

class ExpenseViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirebaseExpenseViewModel::class.java)) {
            return FirebaseExpenseViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
