package com.example.pathfinderapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinderapp.data.repository.FirebaseAppAuthRepository
import com.example.pathfinderapp.ui.screens.*
import com.example.pathfinderapp.ui.screens.auth.LoginScreen
import com.example.pathfinderapp.ui.screens.auth.RegisterScreen
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import com.example.pathfinderapp.ui.viewmodels.RegisterViewModel
import com.example.pathfinderapp.ui.viewmodels.RegisterViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel, // ✅ se pasa el mismo
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClick = { },
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle
            )
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(FirebaseAppAuthRepository())
            )

            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterClick = { username, email, password, confirmPassword ->
                    registerViewModel.register(username, email, password, confirmPassword)
                },
                onBackToLoginClick = {
                    navController.popBackStack()
                },
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDice = { navController.navigate(Screen.Dice.route) },
                onNavigateToCharacters = { navController.navigate(Screen.Character.route) },
                onNavigateToWiki = { navController.navigate(Screen.Wiki.route) }
            )
        }

        composable(Screen.Wiki.route) { WikiScreen() }
        composable(Screen.Dice.route) { DiceScreen() }
        composable(Screen.Character.route) { CharacterScreen() }
        composable(Screen.Bestiary.route) { BestiaryScreen() }

        composable(Screen.Menu.route) {
            MenuScreen(
                onLogoutClick = { authViewModel.logout() }
            )
        }
    }
}
