package com.risparmio.budgetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.risparmio.budgetapp.data.model.CategoryStat
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense

class CategoryAnalysisViewModel : ViewModel() {

    private val _categoryStats = MutableLiveData<List<CategoryStat>>()
    val categoryStats: LiveData<List<CategoryStat>> = _categoryStats

    fun computeStats(expenses: List<FirebaseExpense>) {
        val stats = expenses
            .filter { !it.category.isNullOrEmpty() }
            .groupBy { it.category!! }
            .map { (category, expenses) ->
                CategoryStat(category, expenses.sumOf { it.amount })
            }
        _categoryStats.value = stats
    }
}
