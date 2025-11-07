package com.example.pathfinderapp.ui.navigation

sealed class Screen(val route: String) {

    object Login : Screen("login")
    object Register : Screen("register")

    object Home : Screen("home")
    object Wiki : Screen("wiki")
    object Dice : Screen("dice")
    object Character : Screen("character")
    object Menu : Screen("menu")
    object Bestiary : Screen("bestiary")
}
