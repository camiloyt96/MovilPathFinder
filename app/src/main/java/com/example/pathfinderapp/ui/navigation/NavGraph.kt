package com.example.pathfinderapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinderapp.ui.screens.*
import com.example.pathfinderapp.ui.screens.auth.LoginScreen
import com.example.pathfinderapp.ui.screens.auth.RegisterScreen
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import com.example.pathfinderapp.ui.screens.BestiaryScreen
import com.example.pathfinderapp.ui.screens.WikiScreen

@Composable
fun NavGraph(
    navController: NavHostController,
<<<<<<< HEAD
    authViewModel: AuthViewModel
=======
    authViewModel: AuthViewModel,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
<<<<<<< HEAD
                }
=======
                },
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
<<<<<<< HEAD
                onRegisterSuccess = {
                    // Navega al Home o Login después del registro exitoso
                    navController.navigate(Screen.Home.route) {
                        // Limpia el stack para que no pueda volver atrás
                        popUpTo(Screen.Login.route) { inclusive = false }
                    }
                },
                onBackToLoginClick = {
                    navController.popBackStack()
                }
=======
                onRegisterClick = { username, email, password, confirmPassword ->
                    authViewModel.register(username, email, password, confirmPassword)
                },
                onBackToLoginClick = {
                    navController.popBackStack()
                },
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
            )
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }

        composable(Screen.Wiki.route) {
            WikiScreen()
        }

        composable(Screen.Dice.route) {
            DiceScreen()
        }

        composable(Screen.Character.route) {
            CharacterScreen()
        }

        composable(Screen.Bestiary.route) {
            BestiaryScreen()
        }

        composable(Screen.Menu.route) {
            MenuScreen(
                onLogoutClick = {
                    authViewModel.logout()
                }
            )
        }
    }
}