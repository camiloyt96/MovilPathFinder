package com.example.pathfinderapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BestiaryFilters(
    selectedSize: String?,
    onSizeSelected: (String?) -> Unit,
    selectedType: String?,
    onTypeSelected: (String?) -> Unit,
    selectedCRRange: ClosedFloatingPointRange<Double>?,
    onCRRangeSelected: (ClosedFloatingPointRange<Double>?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {

        // --- Filtro por tamaño ---
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedSize == null,
                    onClick = { onSizeSelected(null) },
                    label = { Text("Todos los tamaños") }
                )
            }
            items(listOf("Small","Medium","Large","Huge","Gargantuan")) { size ->
                FilterChip(
                    selected = selectedSize == size,
                    onClick = { onSizeSelected(size) },
                    label = { Text(size) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Filtro por tipo ---
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeSelected(null) },
                    label = { Text("Todos los tipos") }
                )
            }
            items(listOf("Aberration", "Beast", "Celestial", "Dragon", "Elemental", "Fey", "Fiend", "Giant", "Humanoid", "Monstrosity", "Ooze", "Plant", "Undead")) { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) },
                    label = { Text(type) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Filtro por CR ---
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCRRange == null,
                    onClick = { onCRRangeSelected(null) },
                    label = { Text("Todos los CR") }
                )
            }
            items(
                listOf(
                    0.0..1.0,
                    1.0..5.0,
                    5.0..10.0,
                    10.0..20.0,
                    20.0..30.0
                )
            ) { range ->
                val label = "${range.start.toInt()}-${range.endInclusive.toInt()}"
                FilterChip(
                    selected = selectedCRRange == range,
                    onClick = { onCRRangeSelected(range) },
                    label = { Text(label) }
                )
            }
        }
    }
}
