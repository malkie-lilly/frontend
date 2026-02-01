package com.example.lilian.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lilian.data.model.User
import com.example.lilian.data.repository.AuthRepository
import com.example.lilian.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            
            val result = authRepository.login(email, password)
            
            result.onSuccess { loginResponse ->
                sessionManager.saveUserSession(
                    userId = loginResponse.user.id,
                    username = loginResponse.user.username,
                    email = loginResponse.user.email,
                    role = loginResponse.user.role_name ?: "Viewer",
                    sessionId = loginResponse.session_id
                )
                _loginState.value = LoginState.Success(loginResponse.user)
            }.onFailure { exception ->
                _loginState.value = LoginState.Error(exception.message ?: "Login failed")
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            sessionManager.clearSession()
        }
    }
}
