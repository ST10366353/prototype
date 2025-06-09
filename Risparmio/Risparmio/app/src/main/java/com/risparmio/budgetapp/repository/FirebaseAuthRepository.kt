package com.risparmio.budgetapp.repository

import com.google.firebase.database.FirebaseDatabase
import com.risparmio.budgetapp.data.model.User
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest

class FirebaseAuthRepository {
    private val db = FirebaseDatabase.getInstance().getReference("users")

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<User> {
        return try {
            val snapshot = db.orderByChild("email").equalTo(email).get().await()
            if (snapshot.exists()) {
                Result.failure(Exception("Email already registered"))
            } else {
                val key = db.push().key ?: return Result.failure(Exception("Database error"))
                val user = User(uid = key, email = email, passwordHash = hashPassword(password))
                db.child(key).setValue(user).await()
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val snapshot = db.orderByChild("email").equalTo(email).get().await()
            if (!snapshot.exists()) {
                return Result.failure(Exception("User not found"))
            }
            val user = snapshot.children.first().getValue(User::class.java)
            if (user != null && user.passwordHash == hashPassword(password)) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}