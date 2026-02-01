package com.example.lilian.data.model

data class Content(
    val id: Int,
    val title: String,
    val description: String,
    val creator_id: Int,
    val creator_name: String?,
    val category_id: Int,
    val category_name: String?,
    val duration: Int,
    val thumbnail_url: String,
    val video_url: String,
    val status: String,
    val views: Int,
    val created_at: String
)
