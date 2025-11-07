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
    isDarkMode: Boolean,  // 👈 Ya lo recibe
    onThemeToggle: () -> Unit  // 👈 Ya lo recibe
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 🔹 Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = { /* Agregar si la implementas */ },
                isDarkMode = isDarkMode,  // 👈 AGREGADO: pasa el estado del tema
                onThemeToggle = onThemeToggle  // 👈 AGREGADO: pasa la función de toggle
            )
        }

        // 🔹 Pantalla de Registro
        composable(Screen.Register.route) {
            // ✅ Crear el ViewModel con su Factory
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
                isDarkMode = isDarkMode,  // Ya estaba
                onThemeToggle = onThemeToggle  // Ya estaba
            )
        }

        // 🔹 Otras pantallas (sin cambios)
        composable(Screen.Home.route) { HomeScreen() }
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