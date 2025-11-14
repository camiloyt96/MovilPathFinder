package com.example.pathfinderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pathfinderapp.data.models.MonsterDetail
import com.example.pathfinderapp.data.models.Monster
import com.example.pathfinderapp.ui.components.BestiaryFilters
import com.example.pathfinderapp.ui.components.SearchBar
import com.example.pathfinderapp.ui.viewmodels.BestiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestiaryScreen(
    viewModel: BestiaryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var filtersVisible by remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bestiario", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.searchMonsters(it) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(
                onClick = { filtersVisible = !filtersVisible },
                modifier = Modifier.height(40.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Filter",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (filtersVisible) {
                BestiaryFilters(
                    selectedSize = uiState.selectedSize,
                    onSizeSelected = { viewModel.filterBySize(it) },
                    selectedType = uiState.selectedType,
                    onTypeSelected = { viewModel.filterByType(it) },
                    selectedCRRange = uiState.selectedChallengeRange,
                    onCRRangeSelected = { viewModel.filterByChallengeRange(it) }
                )
            }

            // Contenido principal
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
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
                            onClose = { viewModel.clearSelectedMonster() }
                        )
                    }

                    else -> {
                        MonsterList(
                            monsters = uiState.filteredMonsters,
                            onMonsterClick = { viewModel.loadMonsterDetail(it.index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonsterList(
    monsters: List<Monster>,
    onMonsterClick: (Monster) -> Unit
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
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = monster.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
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
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(monster.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
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
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = ability.desc,
                                    style = MaterialTheme.typography.bodySmall
                                )
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
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = action.desc,
                                    style = MaterialTheme.typography.bodySmall
                                )
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
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
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
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}


