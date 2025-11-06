package com.example.pathfinderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pathfinderapp.data.models.MonsterDetail
import com.example.pathfinderapp.ui.viewmodels.BestiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestiaryScreen(
    viewModel: BestiaryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

<<<<<<< HEAD
    // Colores temáticos rojo/naranja
    val primaryColor = Color(0xFFE53935)
    val secondaryColor = Color(0xFFFF6F00)

=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bestiario", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
<<<<<<< HEAD
                    containerColor = primaryColor,
                    titleContentColor = Color.White
=======
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
<<<<<<< HEAD
                .background(Color(0xFFFFF3E0))
=======
                .background(MaterialTheme.colorScheme.background)
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.searchMonsters(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar monstruo...") },
                leadingIcon = { Icon(Icons.Default.Search, "Buscar") },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchMonsters("") }) {
                            Icon(Icons.Default.Close, "Limpiar")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
<<<<<<< HEAD
                    focusedBorderColor = primaryColor,
=======
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Contenido
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
<<<<<<< HEAD
                            color = primaryColor
=======
                            color = MaterialTheme.colorScheme.primary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                        )
                    }

                    uiState.error != null -> {
                        ErrorMessage(
                            message = uiState.error ?: "",
                            onRetry = { viewModel.loadMonsters() },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.selectedMonster != null -> {
                        MonsterDetailView(
                            monster = uiState.selectedMonster!!,
                            onClose = { viewModel.clearSelectedMonster() },
<<<<<<< HEAD
                            primaryColor = primaryColor
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                        )
                    }

                    else -> {
                        MonsterList(
                            monsters = uiState.filteredMonsters,
                            onMonsterClick = { viewModel.loadMonsterDetail(it.index) },
<<<<<<< HEAD
                            primaryColor = primaryColor,
                            secondaryColor = secondaryColor
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonsterList(
    monsters: List<com.example.pathfinderapp.data.models.Monster>,
    onMonsterClick: (com.example.pathfinderapp.data.models.Monster) -> Unit,
<<<<<<< HEAD
    primaryColor: Color,
    secondaryColor: Color
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(monsters) { monster ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMonsterClick(monster) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
<<<<<<< HEAD
                    containerColor = Color.White
=======
                    containerColor = MaterialTheme.colorScheme.surface
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
<<<<<<< HEAD
                                colors = listOf(primaryColor.copy(alpha = 0.1f), Color.White)
=======
                                colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), MaterialTheme.colorScheme.surface)
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = monster.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
<<<<<<< HEAD
                        color = primaryColor
=======
                        color = MaterialTheme.colorScheme.primary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonsterDetailView(
    monster: MonsterDetail,
    onClose: () -> Unit,
<<<<<<< HEAD
    primaryColor: Color
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(monster.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
<<<<<<< HEAD
                        Icon(Icons.Default.Close, "Cerrar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White
=======
                        Icon(Icons.Default.Close, "Cerrar", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
<<<<<<< HEAD
                .background(Color(0xFFFFF3E0)),
=======
                .background(MaterialTheme.colorScheme.background),
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Información básica
            item {
                DetailCard(title = "Información Básica") {
                    DetailRow("Tipo", "${monster.size} ${monster.type}")
                    DetailRow("Alineamiento", monster.alignment)
                    DetailRow("CR", "${monster.challengeRating} (${monster.xp} XP)")
                }
            }

            // Estadísticas
            item {
                DetailCard(title = "Estadísticas") {
                    DetailRow("CA", monster.armorClass?.firstOrNull()?.value?.toString() ?: "N/A")
                    DetailRow("HP", "${monster.hitPoints} (${monster.hitDice})")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("FUE", monster.strength)
                        StatItem("DES", monster.dexterity)
                        StatItem("CON", monster.constitution)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("INT", monster.intelligence)
                        StatItem("SAB", monster.wisdom)
                        StatItem("CAR", monster.charisma)
                    }
                }
            }

            // Habilidades especiales
            if (!monster.specialAbilities.isNullOrEmpty()) {
                item {
                    DetailCard(title = "Habilidades Especiales") {
                        monster.specialAbilities.forEach { ability ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = ability.name,
                                    fontWeight = FontWeight.Bold,
<<<<<<< HEAD
                                    color = primaryColor
=======
                                    color = MaterialTheme.colorScheme.primary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                                )
                                Text(text = ability.desc, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            // Acciones
            if (!monster.actions.isNullOrEmpty()) {
                item {
                    DetailCard(title = "Acciones") {
                        monster.actions.forEach { action ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = action.name,
                                    fontWeight = FontWeight.Bold,
<<<<<<< HEAD
                                    color = primaryColor
=======
                                    color = MaterialTheme.colorScheme.primary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                                )
                                Text(text = action.desc, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
<<<<<<< HEAD
        elevation = CardDefaults.cardElevation(4.dp)
=======
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
<<<<<<< HEAD
                color = Color(0xFFE53935),
=======
                color = MaterialTheme.colorScheme.primary,
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        Text(text = value.toString(), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
<<<<<<< HEAD
        Text(text = message, color = Color.Red)
=======
        Text(text = message, color = MaterialTheme.colorScheme.error)
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}