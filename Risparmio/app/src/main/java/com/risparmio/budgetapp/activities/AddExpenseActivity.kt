package com.risparmio.budgetapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.risparmio.budgetapp.R
import com.risparmio.budgetapp.data.model.firebase.FirebaseExpense
import com.risparmio.budgetapp.data.model.firebase.FirebaseCategory
import com.risparmio.budgetapp.viewmodel.firebase.FirebaseExpenseViewModel
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var etDate: TextInputEditText
    private lateinit var etStartTime: TextInputEditText
    private lateinit var etEndTime: TextInputEditText
    private lateinit var etAmount: TextInputEditText
    private lateinit var etCategory: AutoCompleteTextView
    private lateinit var etDescription: TextInputEditText
    private lateinit var btnAddPhoto: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var imgPreview: ImageView
    private lateinit var tvNoImageSelected: TextView

    private var selectedImageUri: Uri? = null
    private val calendar = Calendar.getInstance()
    private lateinit var categoryAdapter: ArrayAdapter<String>

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        setupViews()
        setupListeners()
        observeCategories()
    }

    private fun setupViews() {
        etDate = findViewById(R.id.etDate)
        etStartTime = findViewById(R.id.etStartTime)
        etEndTime = findViewById(R.id.etEndTime)
        etAmount = findViewById(R.id.etAmount)
        etCategory = findViewById(R.id.etCategory)
        etDescription = findViewById(R.id.etDescription)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSaveExpense)
        imgPreview = findViewById(R.id.imgPreview)
        tvNoImageSelected = findViewById(R.id.tvNoImageSelected)
    }

    private fun setupListeners() {
        etDate.setOnClickListener { showDatePicker() }
        etStartTime.setOnClickListener { showTimePicker(etStartTime) }
        etEndTime.setOnClickListener { showTimePicker(etEndTime) }
        etCategory.setOnClickListener { etCategory.showDropDown() }

        etCategory.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()
            if (selected == "➕ Add New Category") {
                showAddCategoryDialog()
            }
        }

        btnAddPhoto.setOnClickListener { pickImage() }
        btnCancel.setOnClickListener { finish() }
        btnSave.setOnClickListener { saveExpense() }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                etDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(target: TextInputEditText) {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                target.setText(String.format("%02d:%02d", hourOfDay, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private val viewModel: FirebaseExpenseViewModel by viewModels()

    private fun observeCategories() {
        viewModel.allCategories.observe(this) { categoryList ->
            if (categoryList.isNullOrEmpty()) {
                insertDefaultCategories()
            } else {
                val categoryNames = categoryList.map { it.name } + "➕ Add New Category"
                categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryNames)
                etCategory.setAdapter(categoryAdapter)
            }
        }
    }

    private fun insertDefaultCategories() {
        val defaultCategories = listOf(
            FirebaseCategory(name = "🍔 Groceries"),
            FirebaseCategory(name = "🚗 Transport"),
            FirebaseCategory(name = "🏠 Rent"),
            FirebaseCategory(name = "🎉 Entertainment"),
            FirebaseCategory(name = "🛒 Shopping"),
            FirebaseCategory(name = "💡 Utilities"),
            FirebaseCategory(name = "💼 Work"),
            FirebaseCategory(name = "🍽️ Dining Out"),
            FirebaseCategory(name = "✈️ Travel"),
            FirebaseCategory(name = "🏥 Health")
        )
        defaultCategories.forEach { category ->
            viewModel.insertCategory(category, {}, {})
        }
    }

    private fun setupCategoryDropdown() {
        // No-op: now handled by observeCategories()
    }

    private fun saveExpense() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val amount = etAmount.text.toString().toDoubleOrNull()
                if (amount == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddExpenseActivity, "Invalid amount", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                var newExpense = FirebaseExpense(
                    amount = amount,
                    date = etDate.text.toString(),
                    startTime = etStartTime.text.toString(),
                    endTime = etEndTime.text.toString(),
                    category = etCategory.text.toString(),
                    description = etDescription.text.toString()
                )
                selectedImageUri?.let { uri ->
                    val imageUrl = viewModel.uploadPhoto(uri)
                    newExpense = newExpense.copy(imageUrl = imageUrl)
                    viewModel.insertExpense(newExpense)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddExpenseActivity, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    return@launch
                }
                viewModel.insertExpense(newExpense)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddExpenseActivity, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddExpenseActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showAddCategoryDialog() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("New Category")
            .setMessage("Enter the new category name:")
            .setView(editText)
            .setPositiveButton("Add") { dialog, _ ->
                val newCategoryName = editText.text.toString().trim()
                if (newCategoryName.isNotEmpty()) {
                    val newCategory = FirebaseCategory(
                        name = newCategoryName
                    )
                    viewModel.insertCategory(newCategory,
                        onSuccess = {
                            categoryAdapter.notifyDataSetChanged()
                            etCategory.setText(newCategoryName)
                        },
                        onFailure = { exception ->
                            Toast.makeText(this, "Failed to add category: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    @Deprecated("Deprecated in parent class")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                imgPreview.setImageURI(it)
                imgPreview.visibility = ImageView.VISIBLE
                tvNoImageSelected.visibility = TextView.GONE
            }
        }
    }
}