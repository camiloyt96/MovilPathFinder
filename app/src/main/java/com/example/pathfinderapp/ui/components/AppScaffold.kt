package com.example.pathfinderapp.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pathfinderapp.ui.navigation.NavGraph
import com.example.pathfinderapp.ui.navigation.Screen
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import com.example.pathfinderapp.ui.viewmodels.CharacterViewModel
import com.example.pathfinderapp.ui.viewmodels.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    characterViewModel: CharacterViewModel, // ← AGREGA ESTE PARÁMETRO
    isAuthenticated: Boolean,
    currentUser: User?,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
    if (isAuthenticated) {
        AuthenticatedScaffold(
            navController = navController,
            authViewModel = authViewModel,
            characterViewModel = characterViewModel, // ← PÁSALO AQUÍ
            currentUser = currentUser,
            isDarkMode = isDarkMode,
            onThemeToggle = onThemeToggle
        )
    } else {
        UnauthenticatedScaffold(
            navController = navController,
            authViewModel = authViewModel,
            characterViewModel = characterViewModel, // ← PÁSALO AQUÍ
            isDarkMode = isDarkMode,
            onThemeToggle = onThemeToggle
        )
    }
}

@Composable
fun UnauthenticatedScaffold(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    characterViewModel: CharacterViewModel, // ← AGREGA ESTE PARÁMETRO
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavGraph(
            navController = navController,
            authViewModel = authViewModel,
            characterViewModel = characterViewModel, // ← PÁSALO AQUÍ
            isDarkMode = isDarkMode,
            onThemeToggle = onThemeToggle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedScaffold(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    characterViewModel: CharacterViewModel, // ← AGREGA ESTE PARÁMETRO
    currentUser: User?,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
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
            Screen.Characters.route -> "Mis Personajes"
            Screen.Character.route -> "Crear Personaje"
            Screen.Bestiary.route -> "Bestiario"
            Screen.Menu.route -> "Configuración"
            else -> "Pathfinder"
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedItem = selectedItem,
                currentUser = currentUser,
                onItemClick = { title, route ->
                    selectedItem = title
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawerState.close() }
                },
                onLogoutClick = {
                    authViewModel.logout()
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedItem) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = onThemeToggle) {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode
                                else Icons.Default.DarkMode,
                                contentDescription = "Tema"
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
                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    characterViewModel = characterViewModel, // ← PÁSALO AQUÍ
                    isDarkMode = isDarkMode,
                    onThemeToggle = onThemeToggle
                )
            }
        }
    }
}