package com.example.lilian.ui.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lilian.data.model.Content
import com.example.lilian.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ContentState {
    object Idle : ContentState()
    object Loading : ContentState()
    data class Success(val contentList: List<Content>) : ContentState()
    data class Error(val message: String) : ContentState()
}

class ContentViewModel(
    private val contentRepository: ContentRepository
) : ViewModel() {
    
    private val _contentState = MutableStateFlow<ContentState>(ContentState.Idle)
    val contentState: StateFlow<ContentState> = _contentState
    
    fun loadContent(categoryId: Int? = null) {
        viewModelScope.launch {
            _contentState.value = ContentState.Loading
            val result = contentRepository.getContent(categoryId = categoryId)
            result.onSuccess { content ->
                _contentState.value = ContentState.Success(content)
            }.onFailure { exception ->
                _contentState.value = ContentState.Error(exception.message ?: "Failed to load content")
            }
        }
    }
}
