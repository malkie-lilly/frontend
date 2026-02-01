package com.example.lilian.ui.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lilian.data.model.Content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDetailScreen(
    contentId: Int,
    viewModel: ContentViewModel,
    onBack: () -> Unit,
    onPlayClick: (Content) -> Unit
) {
    val contentState by viewModel.contentState.collectAsState()
    var currentContent by remember { mutableStateOf<Content?>(null) }

    LaunchedEffect(contentId) {
        // If content is already in list, we could find it. 
        // For simplicity, we just look at the success state if it exists.
        if (contentState is ContentState.Success) {
            currentContent = (contentState as ContentState.Success).contentList.find { it.id == contentId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentContent?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        currentContent?.let { content ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                AsyncImage(
                    model = content.thumbnail_url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    contentScale = ContentScale.Crop
                )
                
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = content.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "By ${content.creator_name}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = content.description)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { onPlayClick(content) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Watch Now")
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
