package com.example.pathfinderapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.ui.viewmodels.CharacterViewModel

@Composable
fun CharactersListScreen(
    viewModel: CharacterViewModel,
    onCreateCharacter: () -> Unit = {}
) {
    val characters by viewModel.characters.collectAsState()
    var characterToDelete by remember { mutableStateOf<CharacterProfile?>(null) }

    LaunchedEffect(Unit) {
        Log.d("CharactersListScreen", "Pantalla cargada, personajes: ${characters.size}")
    }

    // Diálogo de confirmación para eliminar
    if (characterToDelete != null) {
        AlertDialog(
            onDismissRequest = { characterToDelete = null },
            title = { Text("Eliminar Personaje") },
            text = {
                Text("¿Estás seguro de que quieres eliminar a ${characterToDelete?.name}? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        characterToDelete?.let { viewModel.removeCharacter(it.id) }
                        characterToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { characterToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            if (characters.size < 5) {
                FloatingActionButton(
                    onClick = onCreateCharacter,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear personaje"
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Mis personajes (${characters.size}/5)",
                style = MaterialTheme.typography.headlineMedium
            )

            if (characters.size >= 5) {
                Text(
                    "Has alcanzado el límite de personajes. Elimina uno para crear otro.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            if (characters.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Aún no tienes personajes",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "¡Crea tu primer aventurero!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = onCreateCharacter) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Crear primer personaje")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(characters, key = { it.id }) { ch ->
                        CharacterReadOnlyCard(
                            character = ch,
                            onDelete = { characterToDelete = ch }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterReadOnlyCard(
    character: CharacterProfile,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(character.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("Raza: ${character.race.name}")
                    Text("Clase: ${character.characterClass.name} (${character.characterClass.hitDie})")
                }

                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar personaje",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Stats base:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "FUE ${character.stats.strength}  | DES ${character.stats.dexterity}  | CON ${character.stats.constitution}\n" +
                        "INT ${character.stats.intelligence} | SAB ${character.stats.wisdom} | CAR ${character.stats.charisma}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            if (character.race.bonuses.isNotEmpty()) {
                Text(
                    "Bonificadores raciales: " +
                            character.race.bonuses.entries.joinToString { (k, v) -> "$k ${if (v >= 0) "+" else ""}$v" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            character.race.specialTraits.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Rasgos: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}