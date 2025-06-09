package com.risparmio.budgetapp

import android.app.Application
import android.content.ComponentCallbacks2
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class MyApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            // Initialize Firebase in background
            FirebaseDatabase.getInstance().apply {
                setPersistenceEnabled(true)
                reference.keepSynced(true)

                // Parallelize path pre-caching
                listOf("expenses", "users", "categories").map { path ->
                    async {
                        getReference(path).keepSynced(true)
                    }
                }.awaitAll()
            }
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            applicationScope.coroutineContext.cancelChildren()
        }
    }
}