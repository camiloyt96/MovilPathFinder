package com.example.pathfinderapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Home.route, Icons.Filled.Home, "Inicio"),
        BottomNavItem(Screen.Wiki.route, Icons.Filled.Book, "Wiki"),
        BottomNavItem(Screen.Dice.route, Icons.Filled.Casino, "D20"),
        BottomNavItem(Screen.Character.route, Icons.Filled.Person, "Personaje"),
        BottomNavItem(Screen.Bestiary.route, Icons.Filled.Book, "Bestiario"),
        BottomNavItem(Screen.Menu.route, Icons.Filled.Menu, "Menú")

    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
