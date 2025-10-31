package com.example.pathfinderapp

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Wiki : Screen("wiki")
    object Dice : Screen("dice")
    object Character : Screen("character")
    object Menu : Screen("menu")

    object Bestiary : Screen("bestiary")
}