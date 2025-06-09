package com.risparmio.budgetapp.repository.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.risparmio.budgetapp.data.model.firebase.FirebaseCategory
import java.util.*

class FirebaseCategoryRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val categoriesRef: DatabaseReference = database.getReference("categories")
    
    private val _allCategories = MutableLiveData<List<FirebaseCategory>>()
    val allCategories: LiveData<List<FirebaseCategory>> = _allCategories
    
    init {
        // Listen for changes in the categories node
        categoriesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoriesList = mutableListOf<FirebaseCategory>()
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(FirebaseCategory::class.java)
                    category?.let { categoriesList.add(it) }
                }
                _allCategories.value = categoriesList
            }
            
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    
    fun insert(category: FirebaseCategory, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Generate a unique ID if not provided
        if (category.id.isEmpty()) {
            category.id = categoriesRef.push().key ?: UUID.randomUUID().toString()
        }
        
        categoriesRef.child(category.id).setValue(category)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
    
    fun delete(categoryId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        categoriesRef.child(categoryId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}