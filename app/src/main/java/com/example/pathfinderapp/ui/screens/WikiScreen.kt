package com.example.pathfinderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.pathfinderapp.data.models.SpellDetail
import com.example.pathfinderapp.data.models.Spell
import com.example.pathfinderapp.ui.components.SearchBar
import com.example.pathfinderapp.ui.viewmodels.WikiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WikiScreen(
    viewModel: WikiViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wiki de Hechizos", fontWeight = FontWeight.Bold) },
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
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.searchSpells(it) }
            )

            SpellLevelFilters(
                selectedLevel = uiState.selectedLevel,
                onLevelSelected = { viewModel.filterByLevel(it) }
            )

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
                            onRetry = { viewModel.loadSpells() },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.selectedSpell != null -> {
                        SpellDetailView(
                            spell = uiState.selectedSpell!!,
                            onClose = { viewModel.clearSelectedSpell() }
                        )
                    }

                    else -> {
                        SpellList(
                            spells = uiState.filteredSpells,
                            onSpellClick = { viewModel.loadSpellDetail(it.index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpellLevelFilters(
    selectedLevel: Int?,
    onLevelSelected: (Int?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedLevel == null,
                onClick = { onLevelSelected(null) },
                label = { Text("Todos") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        item {
            FilterChip(
                selected = selectedLevel == 0,
                onClick = { onLevelSelected(0) },
                label = { Text("Cantrips") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        items((1..9).toList()) { level ->
            FilterChip(
                selected = selectedLevel == level,
                onClick = { onLevelSelected(level) },
                label = { Text("Nivel $level") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SpellList(
    spells: List<Spell>,
    onSpellClick: (Spell) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(spells) { spell ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSpellClick(spell) },
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
                        text = spell.name,
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
fun SpellDetailView(
    spell: SpellDetail,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(spell.name, fontWeight = FontWeight.Bold) },
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
            item {
                SpellDetailCard(title = "Información Básica") {
                    SpellDetailRow("Nivel", if (spell.level == 0) "Cantrip" else "Nivel ${spell.level}")
                    SpellDetailRow("Escuela", spell.school.name)
                    SpellDetailRow("Tiempo de lanzamiento", spell.castingTime)
                    SpellDetailRow("Alcance", spell.range)
                    SpellDetailRow("Duración", spell.duration)
                    SpellDetailRow("Concentración", if (spell.concentration) "Sí" else "No")
                    SpellDetailRow("Ritual", if (spell.ritual) "Sí" else "No")
                }
            }

            item {
                SpellDetailCard(title = "Componentes") {
                    Text(text = spell.components.joinToString(", "))
                    if (!spell.material.isNullOrEmpty()) {
                        Text(
                            text = "Material: ${spell.material}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            item {
                SpellDetailCard(title = "Descripción") {
                    spell.desc.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            if (!spell.higherLevel.isNullOrEmpty()) {
                item {
                    SpellDetailCard(title = "En Niveles Superiores") {
                        spell.higherLevel.forEach { paragraph ->
                            Text(
                                text = paragraph,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                SpellDetailCard(title = "Clases") {
                    Text(text = spell.classes.joinToString(", ") { it.name })
                }
            }

            if (spell.damage != null) {
                item {
                    SpellDetailCard(title = "Daño") {
                        spell.damage.damageType?.let {
                            SpellDetailRow("Tipo", it.name)
                        }
                        spell.damage.damageAtSlotLevel?.forEach { (level, damage) ->
                            SpellDetailRow("Nivel $level", damage)
                        }
                        spell.damage.damageAtCharacterLevel?.forEach { (level, damage) ->
                            SpellDetailRow("Nivel $level", damage)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpellDetailCard(
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
fun SpellDetailRow(label: String, value: String) {
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
