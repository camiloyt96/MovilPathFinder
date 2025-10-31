package com.example.pathfinderapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinderapp.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
        composable(route = Screen.Wiki.route) {
            WikiScreen()
        }
        composable(route = Screen.Dice.route) {
            DiceScreen()
        }
        composable(route = Screen.Character.route) {
            CharacterScreen()
        }
        composable(route = Screen.Menu.route) {
            MenuScreen()
        }
        composable(route = Screen.Bestiary.route) {
            BestiaryScreen()
        }
    }
}