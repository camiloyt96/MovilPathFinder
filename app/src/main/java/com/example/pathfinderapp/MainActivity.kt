package com.example.pathfinderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pathfinderapp.data.repository.FirebaseAppAuthRepository
import com.example.pathfinderapp.ui.components.LoadingScreen
import com.example.pathfinderapp.ui.navigation.AppNavigation
import com.example.pathfinderapp.ui.theme.PathfinderAppTheme

import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import com.example.pathfinderapp.ui.viewmodels.AuthViewModelFactory

import com.example.pathfinderapp.ui.viewmodels.ThemeViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val themeViewModel: ThemeViewModel = viewModel()

            // Crear AuthViewModel con Factory
            val authRepository = remember { FirebaseAppAuthRepository() }
            val factory = remember { AuthViewModelFactory(authRepository) }
            val authViewModel: AuthViewModel = viewModel(factory = factory)

            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            // Estado para controlar la pantalla de carga
            var isLoading by remember { mutableStateOf(true) }

            // Simular tiempo de carga (puedes ajustar el delay)
            LaunchedEffect(Unit) {
                delay(2000) // 2 segundos de pantalla de carga
                isLoading = false
            }

            PathfinderAppTheme(darkTheme = isDarkMode) {
                if (isLoading) {
                    LoadingScreen()
                } else {
                    AppNavigation(
                        isDarkMode = isDarkMode,
                        onThemeToggle = { themeViewModel.toggleTheme() },
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}