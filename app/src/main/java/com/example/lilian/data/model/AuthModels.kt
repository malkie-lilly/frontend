package com.example.lilian.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role_id: Int = 3 // Default to Viewer
)

data class LoginResponse(
    val user: User,
    val session_id: String
)
