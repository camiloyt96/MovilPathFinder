package com.example.pathfinderapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DiceScreen() {
    var diceValue by remember { mutableStateOf(20) }
    var isRolling by remember { mutableStateOf(false) }
    var rollHistory by remember { mutableStateOf(listOf<Int>()) }

    var rotationValue by remember { mutableStateOf(0f) }
    var scaleValue by remember { mutableStateOf(1f) }

    val scope = rememberCoroutineScope()

    val rollDice: () -> Unit = {
        if (!isRolling) {
            isRolling = true

            scope.launch {
                repeat(10) { i ->
                    rotationValue = (i * 72f)
                    scaleValue = if (i % 2 == 0) 1.2f else 1.0f
                    delay(100)
                }

                val newValue = Random.nextInt(1, 21)
                diceValue = newValue
                rollHistory = (listOf(newValue) + rollHistory).take(10)

                rotationValue = 0f
                scaleValue = 1f
                isRolling = false
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Dado D20",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .rotate(rotationValue)
                        .scale(scaleValue)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    getDiceColor(diceValue),
                                    getDiceColor(diceValue).copy(alpha = 0.7f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isRolling) "?" else diceValue.toString(),
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (!isRolling && diceValue > 0) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = getResultColor(diceValue)
                        ),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = getResultMessage(diceValue),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Button(
                onClick = rollDice,
                enabled = !isRolling,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.Casino,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRolling) "Tirando..." else "Tirar D20",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (rollHistory.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Historial de Tiradas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = rollHistory.joinToString(" • "),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total: ${rollHistory.size} tiradas",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getDiceColor(value: Int): Color {
    return when (value) {
        20 -> Color(0xFF4CAF50)
        1 -> Color(0xFFE53935)
        in 15..19 -> Color(0xFF2196F3)
        in 2..5 -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.primary
    }
}

@Composable
fun getResultColor(value: Int): Color {
    return when (value) {
        20 -> Color(0xFF4CAF50).copy(alpha = 0.3f)
        1 -> Color(0xFFE53935).copy(alpha = 0.3f)
        in 15..19 -> Color(0xFF2196F3).copy(alpha = 0.3f)
        in 2..5 -> Color(0xFFFF9800).copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.primaryContainer
    }
}

fun getResultMessage(value: Int): String {
    return when (value) {
        20 -> " ¡CRÍTICO PERFECTO! "
        1 -> " ¡FALLO CRÍTICO! "
        in 18..19 -> "¡Excelente tirada!"
        in 15..17 -> "¡Muy buena tirada!"
        in 11..14 -> "Tirada decente"
        in 6..10 -> "Tirada regular"
        in 2..5 -> "Tirada baja..."
        else -> "Resultado: $value"
    }
}