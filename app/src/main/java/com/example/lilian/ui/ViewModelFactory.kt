package com.example.lilian.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lilian.data.api.RetrofitClient
import com.example.lilian.data.repository.AuthRepository
import com.example.lilian.data.repository.ContentRepository
import com.example.lilian.ui.auth.AuthViewModel
import com.example.lilian.ui.content.ContentViewModel
import com.example.lilian.utils.SessionManager

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val apiService = RetrofitClient.apiService
    private val sessionManager = SessionManager(context)
    private val authRepository = AuthRepository(apiService)
    private val contentRepository = ContentRepository(apiService)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(ContentViewModel::class.java) -> {
                ContentViewModel(contentRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
