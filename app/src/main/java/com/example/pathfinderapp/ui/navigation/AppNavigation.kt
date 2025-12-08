package com.example.pathfinderapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pathfinderapp.ui.components.LoadingScreen
import com.example.pathfinderapp.ui.components.AppScaffold
import com.example.pathfinderapp.ui.viewmodels.AuthState
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import com.example.pathfinderapp.ui.viewmodels.CharacterViewModel
import com.example.pathfinderapp.ui.viewmodels.CharacterViewModelFactory

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val context = LocalContext.current
    val characterViewModel: CharacterViewModel = viewModel(
        factory = CharacterViewModelFactory(context)
    )

    if (authState is AuthState.Loading) {
        LoadingScreen()
        return
    }

    AppScaffold(
        navController = navController,
        authViewModel = authViewModel,
        characterViewModel = characterViewModel,
        isAuthenticated = authState is AuthState.Authenticated,
        currentUser = currentUser,
        isDarkMode = isDarkMode,
        onThemeToggle = onThemeToggle
    )

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Log.d("AppNavigation", "Usuario autenticado, sincronizando personajes...")
                characterViewModel.syncWithFirebase()
                characterViewModel.loadCharacters()

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
