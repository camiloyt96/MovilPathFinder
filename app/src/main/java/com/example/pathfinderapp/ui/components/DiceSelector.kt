package com.example.pathfinderapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pathfinderapp.ui.screens.DiceType

@Composable
fun DiceSelector(
    selectedDice: DiceType,
    onDiceSelected: (DiceType) -> Unit,
    isRolling: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Selecciona el dado",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(DiceType.D4, DiceType.D6, DiceType.D8, DiceType.D10).forEach { dice ->
                    FilterChip(
                        selected = selectedDice == dice,
                        onClick = { if (!isRolling) onDiceSelected(dice) },
                        label = { Text(dice.label, fontSize = 25.sp, fontWeight = if (selectedDice == dice) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(DiceType.D12, DiceType.D20, DiceType.D100).forEach { dice ->
                    FilterChip(
                        selected = selectedDice == dice,
                        onClick = { if (!isRolling) onDiceSelected(dice) },
                        label = { Text(dice.label, fontSize = 25.sp, fontWeight = if (selectedDice == dice) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}
