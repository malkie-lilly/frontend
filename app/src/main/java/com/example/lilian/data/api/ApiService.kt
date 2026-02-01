package com.example.lilian.data.api

import com.example.lilian.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Authentication
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>
    
    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>
    
    @GET("auth/me")
    suspend fun getCurrentUser(): Response<ApiResponse<User>>
    
    // Content
    @GET("content")
    suspend fun getContent(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("category_id") categoryId: Int? = null
    ): Response<ApiResponse<List<Content>>>
    
    @GET("content/{id}")
    suspend fun getContentById(@Path("id") id: Int): Response<ApiResponse<Content>>
    
    @POST("content")
    suspend fun createContent(@Body content: CreateContentRequest): Response<ApiResponse<Content>>
    
    @PUT("content/{id}")
    suspend fun updateContent(
        @Path("id") id: Int,
        @Body content: UpdateContentRequest
    ): Response<ApiResponse<Content>>
    
    @DELETE("content/{id}")
    suspend fun deleteContent(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Categories
    @GET("categories")
    suspend fun getCategories(): Response<ApiResponse<List<Category>>>
    
    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<ApiResponse<Category>>
    
    // Users (Admin)
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<User>>>
}

data class CreateContentRequest(
    val title: String,
    val description: String,
    val category_id: Int,
    val duration: Int,
    val thumbnail_url: String,
    val video_url: String
)

data class UpdateContentRequest(
    val title: String?,
    val description: String?,
    val category_id: Int?,
    val status: String?
)
