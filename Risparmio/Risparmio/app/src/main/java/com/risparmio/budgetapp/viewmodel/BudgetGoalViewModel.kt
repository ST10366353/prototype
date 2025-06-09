package com.risparmio.budgetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BudgetGoalViewModel : ViewModel() {

    private val _totalSpent = MutableLiveData<Double>(8200.0)
    val totalSpent: LiveData<Double> = _totalSpent

    private val _budgetProgress = MutableLiveData<String>("🟡 65% of your budget used")
    val budgetProgress: LiveData<String> = _budgetProgress

    fun saveBudgetGoals(min: Double, max: Double) {//set minimum or maximum

    }
}
