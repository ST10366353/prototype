
package com.risparmio.budgetapp.repository.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense
import java.util.*

class FirebaseExpenseRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val expensesRef: DatabaseReference = database.getReference("expenses")
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference.child("expense_photos")

    private val _allExpenses = MutableLiveData<List<FirebaseExpense>>()
    val allExpenses: LiveData<List<FirebaseExpense>> = _allExpenses

    init {
        expensesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expensesList = mutableListOf<FirebaseExpense>()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(FirebaseExpense::class.java)
                    expense?.let { expensesList.add(it) }
                }
                _allExpenses.value = expensesList
            }

            override fun onCancelled(error: DatabaseError) {
                // Log error in production
            }
        })
    }

    fun insert(expense: FirebaseExpense, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val expenseId = expense.id ?: expensesRef.push().key
        val newExpense = expense.copy(id = expenseId)

        // id is guaranteed to be non-null since we set it above
        expensesRef.child(newExpense.id!!).setValue(newExpense)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun delete(expenseId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        expensesRef.child(expenseId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getByCategory(category: String): LiveData<List<FirebaseExpense>> {
        val filteredExpenses = MutableLiveData<List<FirebaseExpense>>()
        expensesRef.orderByChild("category").equalTo(category)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val expensesList = mutableListOf<FirebaseExpense>()
                    for (expenseSnapshot in snapshot.children) {
                        val expense = expenseSnapshot.getValue(FirebaseExpense::class.java)
                        expense?.let { expensesList.add(it) }
                    }
                    filteredExpenses.value = expensesList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log error in production
                }
            })
        return filteredExpenses
    }

    fun getByDateRange(startDate: String, endDate: String): LiveData<List<FirebaseExpense>> {
        val filteredExpenses = MutableLiveData<List<FirebaseExpense>>()
        expensesRef.orderByChild("date")
            .startAt(startDate)
            .endAt(endDate)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val expensesList = mutableListOf<FirebaseExpense>()
                    for (expenseSnapshot in snapshot.children) {
                        val expense = expenseSnapshot.getValue(FirebaseExpense::class.java)
                        expense?.let { expensesList.add(it) }
                    }
                    filteredExpenses.value = expensesList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log error in production
                }
            })
        return filteredExpenses
    }

    fun uploadPhoto(photoUri: android.net.Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val photoRef = storageRef.child("${UUID.randomUUID()}.jpg")
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener { onFailure(it) }
    }
}