package com.example.pathfinderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pathfinderapp.data.repository.FirebaseAppAuthRepository
import com.example.pathfinderapp.ui.navigation.AppNavigation
import com.example.pathfinderapp.ui.theme.PathfinderAppTheme
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import com.example.pathfinderapp.ui.viewmodels.AuthViewModelFactory
import com.example.pathfinderapp.ui.viewmodels.ThemeViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()

            // FIX: Crear AuthViewModel con Factory
            val authRepository = remember { FirebaseAppAuthRepository() }
            val factory = remember { AuthViewModelFactory(authRepository) }
            val authViewModel: AuthViewModel = viewModel(factory = factory)

            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            PathfinderAppTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeToggle = { themeViewModel.toggleTheme() },
                    authViewModel = authViewModel
                )
            }
        }
    }
}