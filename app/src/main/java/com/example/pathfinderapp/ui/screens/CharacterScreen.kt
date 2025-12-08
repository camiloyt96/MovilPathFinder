package com.example.pathfinderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.data.models.Race
import com.example.pathfinderapp.data.models.CharacterClass
import com.example.pathfinderapp.data.models.CharacterStats
import android.util.Log
import com.example.pathfinderapp.ui.viewmodels.CharacterViewModel
import com.example.pathfinderapp.ui.viewmodels.CharacterViewModelFactory


@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel,
    onCharacterCreated: () -> Unit = {}
) {

    var currentStep by remember { mutableStateOf(0) }
    var characterName by remember { mutableStateOf("") }
    var selectedRace by remember { mutableStateOf<Race?>(null) }
    var selectedClass by remember { mutableStateOf<CharacterClass?>(null) }
    var stats by remember { mutableStateOf(CharacterStats()) }
    var pointsRemaining by remember { mutableStateOf(25) }

    val races = listOf(
        Race(
            "Humano",
            "Versátiles y ambiciosos, dominan muchas tierras",
            mapOf("Libre" to 2),
            "Dote adicional, +1 rango de habilidad por nivel"
        ),
        Race(
            "Elfo",
            "Gráciles, inmortales y mágicamente dotados",
            mapOf("Destreza" to 2, "Inteligencia" to 2, "Constitución" to -2),
            "Inmunidad al sueño mágico, visión en la penumbra"
        ),
        Race(
            "Enano",
            "Robustos, honorables y maestros artesanos",
            mapOf("Constitución" to 2, "Sabiduría" to 2, "Carisma" to -2),
            "Visión en la oscuridad, resistencia vs venenos y magia"
        ),
        Race(
            "Mediano",
            "Pequeños, ágiles y afortunados",
            mapOf("Destreza" to 2, "Carisma" to 2, "Fuerza" to -2),
            "Tamaño pequeño, +1 a todas las salvaciones"
        ),
        Race(
            "Gnomo",
            "Curiosos, excéntricos y vinculados a la magia",
            mapOf("Constitución" to 2, "Carisma" to 2, "Fuerza" to -2),
            "Tamaño pequeño, magia innata, resistencia a ilusiones"
        ),
        Race(
            "Semielfo",
            "Herencia dual, adaptables y carismáticos",
            mapOf("Libre" to 2),
            "Visión en la penumbra, inmunidad al sueño, +2 Percepción"
        ),
        Race(
            "Semiorco",
            "Fuertes, tenaces y marginados sociales",
            mapOf("Libre" to 1),
            "Visión en la oscuridad, ferocidad (sigue luchando a 0 PG)"
        )
    )

    val classes = listOf(
        CharacterClass(
            "Bárbaro",
            "Guerrero feroz que entra en furia para destruir enemigos",
            "d12",
            "Fuerza, Constitución"
        ),
        CharacterClass(
            "Bardo",
            "Artista versátil que inspira aliados con música y magia",
            "d8",
            "Carisma, Destreza"
        ),
        CharacterClass(
            "Clérigo",
            "Seguidor devoto que canaliza poder divino para curar y destruir",
            "d8",
            "Sabiduría, Carisma"
        ),
        CharacterClass(
            "Druida",
            "Guardián de la naturaleza que adopta formas animales",
            "d8",
            "Sabiduría, Constitución"
        ),
        CharacterClass(
            "Explorador",
            "Cazador experto que rastrea enemigos predilectos",
            "d10",
            "Destreza, Sabiduría"
        ),
        CharacterClass(
            "Guerrero",
            "Maestro del combate con armas y armaduras",
            "d10",
            "Fuerza, Constitución o Destreza"
        ),
        CharacterClass(
            "Hechicero",
            "Lanzador innato con sangre mágica en sus venas",
            "d6",
            "Carisma, Destreza"
        ),
        CharacterClass(
            "Mago",
            "Erudito arcano que domina conjuros mediante estudio",
            "d6",
            "Inteligencia, Destreza"
        ),
        CharacterClass(
            "Monje",
            "Asceta marcial que perfecciona cuerpo y mente",
            "d8",
            "Sabiduría, Destreza"
        ),
        CharacterClass(
            "Paladín",
            "Campeón sagrado del bien y la justicia",
            "d10",
            "Fuerza, Carisma"
        ),
        CharacterClass(
            "Pícaro",
            "Experto en sigilo, trampas y ataque furtivo",
            "d8",
            "Destreza, Carisma o Inteligencia"
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Creación de Personaje",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Pathfinder 1ª Edición",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            StepIndicator(currentStep = currentStep, totalSteps = 4)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (currentStep) {
                    0 -> NameStep(characterName) { characterName = it }
                    1 -> RaceStep(races, selectedRace) { selectedRace = it }
                    2 -> ClassStep(classes, selectedClass) { selectedClass = it }
                    3 -> StatsStep(
                        stats = stats,
                        pointsRemaining = pointsRemaining,
                        selectedRace = selectedRace,
                        onStatChange = { newStats, newPoints ->
                            stats = newStats
                            pointsRemaining = newPoints
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = { currentStep-- }
                    ) {
                        Text("Anterior")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (currentStep < 3) {
                    Button(
                        onClick = { currentStep++ },
                        enabled = when (currentStep) {
                            0 -> characterName.isNotBlank()
                            1 -> selectedRace != null
                            2 -> selectedClass != null
                            else -> true
                        }
                    ) {
                        Text("Siguiente")
                    }
                } else {
                    Button(
                        onClick = {
                            if (selectedRace != null && selectedClass != null) {
                                Log.d("CharacterScreen", "Creando personaje: $characterName")
                                val character = CharacterProfile(
                                    name = characterName,
                                    race = selectedRace!!,
                                    characterClass = selectedClass!!,
                                    stats = stats
                                )
                                Log.d("CharacterScreen", "Personaje creado: ${character.id}, ${character.name}")
                                viewModel.addCharacter(character)
                                Log.d("CharacterScreen", "Personaje agregado al ViewModel")
                                onCharacterCreated()
                            }
                        },
                        enabled = pointsRemaining == 0
                    ) {
                        Text("Crear Personaje")
                    }
                }
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(totalSteps) { step ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            if (step <= currentStep) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${step + 1}",
                        color = if (step <= currentStep) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (step) {
                        0 -> "Nombre"
                        1 -> "Raza"
                        2 -> "Clase"
                        3 -> "Stats"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun NameStep(name: String, onNameChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿Cómo se llama tu héroe?",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre del Personaje") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Ej: Valeros, Seoni, Kyra...") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Escoge un nombre épico para tu aventurero de Pathfinder",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun RaceStep(races: List<Race>, selected: Race?, onSelect: (Race) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Elige tu Raza",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(races) { race ->
            RaceCard(race, selected == race) { onSelect(race) }
        }
    }
}

@Composable
fun RaceCard(race: Race, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(8.dp)
                ) else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = race.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = race.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bonificadores: ${race.bonuses.entries.joinToString { "${it.key} ${if (it.value > 0) "+" else ""}${it.value}" }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = race.specialTraits,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun ClassStep(classes: List<CharacterClass>, selected: CharacterClass?, onSelect: (CharacterClass) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Elige tu Clase",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(classes) { cls ->
            ClassCard(cls, selected == cls) { onSelect(cls) }
        }
    }
}

@Composable
fun ClassCard(cls: CharacterClass, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(8.dp)
                ) else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cls.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = cls.hitDie,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cls.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Estadísticas clave: ${cls.primaryStats}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StatsStep(
    stats: CharacterStats,
    pointsRemaining: Int,
    selectedRace: Race?,
    onStatChange: (CharacterStats, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Asigna tus Estadísticas",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Sistema de compra por puntos (25 puntos)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (pointsRemaining == 0)
                    MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = "Puntos restantes: $pointsRemaining",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        selectedRace?.let { race ->
            if (race.bonuses.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Bonificadores raciales: ${race.name}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = race.bonuses.entries.joinToString {
                                "${it.key} ${if (it.value > 0) "+" else ""}${it.value}"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        StatRow("Fuerza (FUE)", stats.strength) { newVal ->
            val cost = calculatePointCost(newVal) - calculatePointCost(stats.strength)
            if (pointsRemaining - cost >= 0 && newVal in 7..18) {
                onStatChange(stats.copy(strength = newVal), pointsRemaining - cost)
            }
        }
        StatRow("Destreza (DES)", stats.dexterity) { newVal ->
            val cost = calculatePointCost(newVal) - calculatePointCost(stats.dexterity)
            if (pointsRemaining - cost >= 0 && newVal in 7..18) {
                onStatChange(stats.copy(dexterity = newVal), pointsRemaining - cost)
            }
        }
        StatRow("Constitución (CON)", stats.constitution) { newVal ->
            val cost = calculatePointCost(newVal) - calculatePointCost(stats.constitution)
            if (pointsRemaining - cost >= 0 && newVal in 7..18) {
                onStatChange(stats.copy(constitution = newVal), pointsRemaining - cost)
            }
        }
        StatRow("Inteligencia (INT)", stats.intelligence) { newVal ->
            val cost = calculatePointCost(newVal) - calculatePointCost(stats.intelligence)
            if (pointsRemaining - cost >= 0 && newVal in 7..18) {
                onStatChange(stats.copy(intelligence = newVal), pointsRemaining - cost)
            }
        }
        StatRow("Sabiduría (SAB)", stats.wisdom) { newVal ->
            val cost = calculatePointCost(newVal) - calculatePointCost(stats.wisdom)
            if (pointsRemaining - cost >= 0 && newVal in 7..18) {
                onStatChange(stats.copy(wisdom = newVal), pointsRemaining - cost)
            }
        }
        StatRow("Carisma (CAR)", stats.charisma) { newVal ->
            val cost = calculatePointCost(newVal) - calculatePointCost(stats.charisma)
            if (pointsRemaining - cost >= 0 && newVal in 7..18) {
                onStatChange(stats.copy(charisma = newVal), pointsRemaining - cost)
            }
        }
    }
}

@Composable
fun StatRow(name: String, value: Int, onValueChange: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold)
                Text(
                    text = "Modificador: ${getModifier(value)} | Coste: ${calculatePointCost(value)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { onValueChange(value - 1) },
                    enabled = value > 7
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                }
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.widthIn(min = 40.dp)
                )
                IconButton(
                    onClick = { onValueChange(value + 1) },
                    enabled = value < 18
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Aumentar")
                }
            }
        }
    }
}

fun calculatePointCost(value: Int): Int {
    return when (value) {
        7 -> -4
        8 -> -2
        9 -> -1
        10 -> 0
        11 -> 1
        12 -> 2
        13 -> 3
        14 -> 5
        15 -> 7
        16 -> 10
        17 -> 13
        18 -> 17
        else -> 0
    }
}

fun getModifier(stat: Int): String {
    val mod = (stat - 10) / 2
    return if (mod >= 0) "+$mod" else "$mod"
}
