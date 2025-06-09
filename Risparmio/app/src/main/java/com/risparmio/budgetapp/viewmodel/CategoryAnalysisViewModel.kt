package com.risparmio.budgetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.risparmio.budgetapp.data.model.CategoryStat
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense
//PhilJay. “MPAndroidChart/MPChartExample/Src/Main at Master · PhilJay/MPAndroidChart.” GitHub, 2025, github.com/PhilJay/MPAndroidChart/tree/master/MPChartExample/src/main. Accessed 6 June 2025.
class CategoryAnalysisViewModel : ViewModel() {

    private val _categoryStats = MutableLiveData<List<CategoryStat>>()
    val categoryStats: LiveData<List<CategoryStat>> = _categoryStats

    fun computeStats(expenses: List<FirebaseExpense>) {
        val stats = expenses
            .filter { !it.category.isNullOrEmpty() }
            .groupBy { it.category!! }
            .map { (category, expenses) ->
                val minGoal = expenses.minOf { it.amount }
                val maxGoal = expenses.maxOf { it.amount }
                CategoryStat(category, expenses.sumOf { it.amount }, minGoal, maxGoal)
            }
        _categoryStats.value = stats
    }
}