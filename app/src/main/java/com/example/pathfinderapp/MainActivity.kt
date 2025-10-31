package com.example.pathfinderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pathfinderapp.ui.theme.PathfinderAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            PathfinderAppTheme(darkTheme = isDarkMode) {
                MainScreen(
                    isDarkMode = isDarkMode,
                    onThemeToggle = { themeViewModel.toggleTheme() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("Inicio") }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        selectedItem = when (currentRoute) {
            Screen.Home.route -> "Inicio"
            Screen.Wiki.route -> "Wiki"
            Screen.Dice.route -> "Dado D20"
            Screen.Character.route -> "Personaje"
            Screen.Bestiary.route -> "Bestiario"
            Screen.Menu.route -> "Menú"
            else -> "Inicio"
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "PATHFINDER",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Juego de Rol",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                HorizontalDivider()

                Spacer(modifier = Modifier.height(8.dp))

                val items = listOf(
                    Triple("Inicio", Icons.Default.Home, Screen.Home.route),
                    Triple("Wiki", Icons.Default.Book, Screen.Wiki.route),
                    Triple("Dado D20", Icons.Default.Casino, Screen.Dice.route),
                    Triple("Personaje", Icons.Default.Person, Screen.Character.route),
                    Triple("Bestiario", Icons.Default.Book, Screen.Bestiary.route),
                    Triple("Menú", Icons.Default.Menu, Screen.Menu.route)
                )

                items.forEach { (title, icon, route) ->
                    NavigationDrawerItem(
                        icon = { Icon(icon, contentDescription = null) },
                        label = { Text(title) },
                        selected = selectedItem == title,
                        onClick = {
                            selectedItem = title
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider()


            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedItem) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = onThemeToggle) {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Cambiar tema"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {
                NavGraph(navController = navController)
            }
        }
    }
}
