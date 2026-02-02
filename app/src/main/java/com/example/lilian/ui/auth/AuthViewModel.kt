package com.example.lilian.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lilian.data.model.User
import com.example.lilian.data.repository.AuthRepository
import com.example.lilian.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class LoginSuccess(val user: User) : AuthState()
    data class RegisterSuccess(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // For backward compatibility with existing code if needed, 
    // but better to migrate to authState.
    val loginState: StateFlow<AuthState> = _authState
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.login(email, password)
            
            result.onSuccess { loginResponse ->
                sessionManager.saveUserSession(
                    userId = loginResponse.user.id,
                    username = loginResponse.user.username,
                    email = loginResponse.user.email,
                    role = loginResponse.user.role_name ?: "Viewer",
                    sessionId = loginResponse.session_id
                )
                _authState.value = AuthState.LoginSuccess(loginResponse.user)
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Login failed")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = authRepository.register(username, email, password)
            
            result.onSuccess {
                _authState.value = AuthState.RegisterSuccess("Registration successful! Please login.")
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Registration failed")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            sessionManager.clearSession()
            _authState.value = AuthState.Idle
        }
    }
}
