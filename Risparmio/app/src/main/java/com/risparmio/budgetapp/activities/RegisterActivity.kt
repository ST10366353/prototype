package com.risparmio.budgetapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.risparmio.budgetapp.databinding.ActivityRegisterBinding
import androidx.activity.viewModels
import com.risparmio.budgetapp.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    // ViewBinding to access views easily
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe signup status
        authViewModel.authResult.observe(this) {
            if (it.isSuccess) {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                // Redirect to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Registration failed: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Register button click
        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.signUp(email, password)
        }

        // Navigate to login screen
        binding.btnLoginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
