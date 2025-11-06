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
import com.example.pathfinderapp.ui.viewmodels.WikiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WikiScreen(
    viewModel: WikiViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

<<<<<<< HEAD
    // Colores temáticos púrpura/rosa
    val primaryColor = Color(0xFF9C27B0)
    val secondaryColor = Color(0xFFE91E63)

=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wiki de Hechizos", fontWeight = FontWeight.Bold) },
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
                .background(Color(0xFFF3E5F5))
=======
                .background(MaterialTheme.colorScheme.background)
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.searchSpells(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar hechizo...") },
                leadingIcon = { Icon(Icons.Default.Search, "Buscar") },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchSpells("") }) {
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

            // Filtros de nivel
            SpellLevelFilters(
                selectedLevel = uiState.selectedLevel,
                onLevelSelected = { viewModel.filterByLevel(it) },
<<<<<<< HEAD
                primaryColor = primaryColor
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
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
                            onRetry = { viewModel.loadSpells() },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.selectedSpell != null -> {
                        SpellDetailView(
                            spell = uiState.selectedSpell!!,
                            onClose = { viewModel.clearSelectedSpell() },
<<<<<<< HEAD
                            primaryColor = primaryColor
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                        )
                    }

                    else -> {
                        SpellList(
                            spells = uiState.filteredSpells,
                            onSpellClick = { viewModel.loadSpellDetail(it.index) },
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
fun SpellLevelFilters(
    selectedLevel: Int?,
    onLevelSelected: (Int?) -> Unit,
<<<<<<< HEAD
    primaryColor: Color
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botón "Todos"
        item {
            FilterChip(
                selected = selectedLevel == null,
                onClick = { onLevelSelected(null) },
                label = { Text("Todos") },
                colors = FilterChipDefaults.filterChipColors(
<<<<<<< HEAD
                    selectedContainerColor = primaryColor,
                    selectedLabelColor = Color.White
=======
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                )
            )
        }

        // Cantrips (nivel 0)
        item {
            FilterChip(
                selected = selectedLevel == 0,
                onClick = { onLevelSelected(0) },
                label = { Text("Cantrips") },
                colors = FilterChipDefaults.filterChipColors(
<<<<<<< HEAD
                    selectedContainerColor = primaryColor,
                    selectedLabelColor = Color.White
=======
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                )
            )
        }

        // Niveles 1-9
        items((1..9).toList()) { level ->
            FilterChip(
                selected = selectedLevel == level,
                onClick = { onLevelSelected(level) },
                label = { Text("Nivel $level") },
                colors = FilterChipDefaults.filterChipColors(
<<<<<<< HEAD
                    selectedContainerColor = primaryColor,
                    selectedLabelColor = Color.White
=======
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SpellList(
    spells: List<com.example.pathfinderapp.data.models.Spell>,
    onSpellClick: (com.example.pathfinderapp.data.models.Spell) -> Unit,
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
        items(spells) { spell ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSpellClick(spell) },
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
                        text = spell.name,
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
fun SpellDetailView(
    spell: SpellDetail,
    onClose: () -> Unit,
<<<<<<< HEAD
    primaryColor: Color
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(spell.name, fontWeight = FontWeight.Bold) },
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
                .background(Color(0xFFF3E5F5)),
=======
                .background(MaterialTheme.colorScheme.background),
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Información básica
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

            // Componentes
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

            // Descripción
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

            // Niveles superiores
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

            // Clases
            item {
                SpellDetailCard(title = "Clases") {
                    Text(text = spell.classes.joinToString(", ") { it.name })
                }
            }

            // Daño (si existe)
            if (spell.damage != null) {
                item {
                    SpellDetailCard(title = "Daño") {
                        spell.damage.damageType?.let {
                            SpellDetailRow("Tipo", it.name)
                        }
                        if (!spell.damage.damageAtSlotLevel.isNullOrEmpty()) {
                            Text(
                                text = "Por nivel de ranura:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            spell.damage.damageAtSlotLevel.forEach { (level, damage) ->
                                SpellDetailRow("Nivel $level", damage)
                            }
                        }
                        if (!spell.damage.damageAtCharacterLevel.isNullOrEmpty()) {
                            Text(
                                text = "Por nivel de personaje:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            spell.damage.damageAtCharacterLevel.forEach { (level, damage) ->
                                SpellDetailRow("Nivel $level", damage)
                            }
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
                color = Color(0xFF9C27B0),
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