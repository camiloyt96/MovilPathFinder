package com.example.pathfinderapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pathfinderapp.ui.navigation.Screen
import com.example.pathfinderapp.ui.viewmodels.User

@Composable
fun AppDrawer(
    selectedItem: String,
    currentUser: User?,
    onItemClick: (String, String) -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))

        DrawerHeader(currentUser)

        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        DrawerMenuItems(
            selectedItem = selectedItem,
            onItemClick = onItemClick
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider()

        DrawerLogoutButton(onLogoutClick)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DrawerHeader(currentUser: User?) {
    Column(
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp)
    ) {
        Text(
            text = "PATHFINDER",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = currentUser?.username ?: "Usuario",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = currentUser?.email ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DrawerMenuItems(
    selectedItem: String,
    onItemClick: (String, String) -> Unit
) {
    val items = listOf(
        Triple("Inicio", Icons.Default.Home, Screen.Home.route),
        Triple("Wiki", Icons.Default.Book, Screen.Wiki.route),
        Triple("Dado D20", Icons.Default.Casino, Screen.Dice.route),
        Triple("Mis Personajes", Icons.Default.Person, Screen.Characters.route), // ✅ Cambio aquí
        Triple("Bestiario", Icons.Default.Pets, Screen.Bestiary.route),
        Triple("Configuración", Icons.Default.Settings, Screen.Menu.route)
    )

    items.forEach { (title, icon, route) ->
        NavigationDrawerItem(
            icon = { Icon(icon, contentDescription = null) },
            label = { Text(title) },
            selected = selectedItem == title,
            onClick = { onItemClick(title, route) },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun DrawerLogoutButton(onLogoutClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
        label = { Text("Cerrar Sesión") },
        selected = false,
        onClick = onLogoutClick,
        modifier = Modifier.padding(12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    )
}