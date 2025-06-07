package com.risparmio.budgetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.risparmio.budgetapp.data.model.User
import com.risparmio.budgetapp.repository.FirebaseAuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = FirebaseAuthRepository()

    private val _authResult = MutableLiveData<Result<User>>()
    val authResult: LiveData<Result<User>> = _authResult

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    fun signUp(email: String, password: String) = viewModelScope.launch {
        _authResult.postValue(authRepository.signUpWithEmail(email, password))
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResult.postValue(authRepository.signInWithEmail(email, password))
    }
}