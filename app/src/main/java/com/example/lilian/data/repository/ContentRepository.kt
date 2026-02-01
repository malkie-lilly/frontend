package com.example.lilian.data.repository

import com.example.lilian.data.api.ApiService
import com.example.lilian.data.model.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContentRepository(private val apiService: ApiService) {
    
    suspend fun getContent(page: Int = 1, limit: Int = 20, categoryId: Int? = null): Result<List<Content>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getContent(page, limit, categoryId)
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(response.body()!!.data ?: emptyList())
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Failed to load content"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getContentById(id: Int): Result<Content> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getContentById(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    Result.success(response.body()!!.data!!)
                } else {
                    Result.failure(Exception("Content not found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
