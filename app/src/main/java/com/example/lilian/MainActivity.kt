package com.example.lilian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lilian.ui.ViewModelFactory
import com.example.lilian.ui.admin.AdminScreen
import com.example.lilian.ui.auth.AuthViewModel
import com.example.lilian.ui.auth.LoginScreen
import com.example.lilian.ui.auth.RegisterScreen
import com.example.lilian.ui.content.ContentDetailScreen
import com.example.lilian.ui.content.ContentScreen
import com.example.lilian.ui.content.ContentViewModel
import com.example.lilian.ui.content.VideoPlayerScreen
import com.example.lilian.ui.profile.ProfileScreen
import com.example.lilian.ui.theme.LilianTheme
import com.example.lilian.utils.SessionManager
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LilianTheme {
                AppNavigation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val sessionManager = SessionManager(context)
    val userSession by sessionManager.userSession.collectAsState(initial = null)
    
    val authViewModel: AuthViewModel = viewModel(factory = ViewModelFactory(context))
    val contentViewModel: ContentViewModel = viewModel(factory = ViewModelFactory(context))

    val startDestination = if (userSession?.isLoggedIn == true) "content" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("content") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login")
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable("content") {
            ContentScreen(
                viewModel = contentViewModel,
                userSession = userSession,
                onNavigateToAdmin = { navController.navigate("admin") },
                onNavigateToProfile = { navController.navigate("profile") },
                onContentClick = { content ->
                    navController.navigate("content_detail/${content.id}")
                }
            )
        }
        composable("admin") {
            AdminScreen()
        }
        composable("profile") {
            ProfileScreen(
                userSession = userSession,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(
            "content_detail/{contentId}",
            arguments = listOf(navArgument("contentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val contentId = backStackEntry.arguments?.getInt("contentId") ?: 0
            ContentDetailScreen(
                contentId = contentId,
                viewModel = contentViewModel,
                onBack = { navController.popBackStack() },
                onPlayClick = { content ->
                    val encodedUrl = URLEncoder.encode(content.video_url, StandardCharsets.UTF_8.toString())
                    navController.navigate("video_player/$encodedUrl/${content.title}")
                }
            )
        }
        composable(
            "video_player/{videoUrl}/{title}",
            arguments = listOf(
                navArgument("videoUrl") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            VideoPlayerScreen(
                videoUrl = videoUrl,
                title = title,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
