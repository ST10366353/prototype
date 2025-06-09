package com.risparmio.budgetapp.viewmodel.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense
import com.risparmio.budgetapp.data.model.firebase.FirebaseCategory
import com.risparmio.budgetapp.repository.firebase.FirebaseExpenseRepository
import com.risparmio.budgetapp.repository.firebase.FirebaseCategoryRepository
import android.net.Uri
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseExpenseViewModel : ViewModel() {
    private val expenseRepository = FirebaseExpenseRepository()
    private val categoryRepository = FirebaseCategoryRepository()
    private val _allExpenses = MutableLiveData<List<FirebaseExpense>>()
    val allExpenses: LiveData<List<FirebaseExpense>> = _allExpenses
    val allCategories: LiveData<List<FirebaseCategory>> = categoryRepository.allCategories

    init {
        fetchExpenses()
    }

    private fun fetchExpenses() {
        expenseRepository.allExpenses.observeForever { expenses ->
            _allExpenses.value = expenses
        }
    }

    suspend fun uploadPhoto(imageUri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val photoRef = storageRef.child("expense_images/${UUID.randomUUID()}")
        val uploadTask = photoRef.putFile(imageUri)
        return uploadTask.await().storage.downloadUrl.await().toString()
    }

    fun insertExpense(expense: FirebaseExpense) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("expenses")
        val expenseId = databaseRef.push().key
        expenseId?.let {
            databaseRef.child(it).setValue(expense.copy(id = it))
        }
    }

    fun insertCategory(category: FirebaseCategory, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        categoryRepository.insert(category, onSuccess, onFailure)
    }

    fun getExpensesByCategory(category: String): LiveData<List<FirebaseExpense>> {
        return expenseRepository.getByCategory(category)
    }

    fun getExpensesByDateRange(startDate: String, endDate: String): LiveData<List<FirebaseExpense>> {
        return expenseRepository.getByDateRange(startDate, endDate)
    }
}