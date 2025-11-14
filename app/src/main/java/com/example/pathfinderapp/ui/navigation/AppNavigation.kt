package com.example.pathfinderapp.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.pathfinderapp.ui.components.LoadingScreen
import com.example.pathfinderapp.ui.components.AppScaffold
import com.example.pathfinderapp.ui.viewmodels.AuthState
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Pantalla de carga
    if (authState is AuthState.Loading) {
        LoadingScreen()
        return
    }

    // Scaffold principal (con o sin drawer según autenticación)
    AppScaffold(
        navController = navController,
        authViewModel = authViewModel,
        isAuthenticated = authState is AuthState.Authenticated,
        currentUser = currentUser,
        isDarkMode = isDarkMode,
        onThemeToggle = onThemeToggle
    )

    // Auto-navegación según estado
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> {}
        }
    }
}