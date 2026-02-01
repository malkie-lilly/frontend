package com.example.lilian.data.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role_id: Int,
    val role_name: String?,
    val status: String,
    val profile_picture: String?,
    val bio: String?,
    val created_at: String
)
