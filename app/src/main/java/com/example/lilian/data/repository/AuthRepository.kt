package com.example.lilian.data.repository

import com.example.lilian.data.api.ApiService
import com.example.lilian.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val apiService: ApiService) {
    
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(response.body()!!.data!!)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Login failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun register(username: String, email: String, password: String, roleId: Int = 3): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(RegisterRequest(username, email, password, roleId))
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(response.body()!!.data!!)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Registration failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.logout()
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Logout failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getCurrentUser(): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCurrentUser()
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(response.body()!!.data!!)
                } else {
                    Result.failure(Exception("Failed to get user"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
