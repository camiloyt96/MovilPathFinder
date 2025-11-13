package com.example.pathfinderapp.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.pathfinderapp.ui.viewmodel.DiceViewModel
import androidx.compose.ui.platform.LocalContext

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.LaunchedEffect
import kotlin.math.sqrt


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
import androidx.compose.ui.tooling.preview.Preview

enum class DiceType(val sides: Int, val label: String) {
    D4(4, "D4"),
    D6(6, "D6"),
    D8(8, "D8"),
    D10(10, "D10"),
    D12(12, "D12"),
    D20(20, "D20"),
    D100(100, "D100")
}

@Composable
fun DiceScreen(viewModel: DiceViewModel = viewModel()) {
    val context = LocalContext.current
    // --- Estados del ViewModel ---
    val selectedDice by viewModel.selectedDice.collectAsState()
    val diceValue by viewModel.diceValue.collectAsState()
    val isRolling by viewModel.isRolling.collectAsState()
    val rollHistory by viewModel.rollHistory.collectAsState()
    val shakeToRollEnabled by viewModel.shakeToRollEnabled.collectAsState()
    val rotationValue by viewModel.rotationValue.collectAsState()
    val scaleValue by viewModel.scaleValue.collectAsState()

    // --- Detector de movimiento (shake) ---
    val sensorManager = remember { context.getSystemService(SensorManager::class.java) }
    val accelerometer = remember { sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    DisposableEffect(shakeToRollEnabled) {
        if (shakeToRollEnabled && accelerometer != null) {
            val shakeListener = object : SensorEventListener {
                private var lastTime = 0L
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        val x = it.values[0]
                        val y = it.values[1]
                        val z = it.values[2]
                        val acceleration = sqrt(x * x + y * y + z * z)
                        val now = System.currentTimeMillis()

                        // Detectar sacudida (ajusta el umbral según sensibilidad deseada)
                        if (acceleration > 16 && now - lastTime > 1000) {
                            lastTime = now
                            viewModel.rollDice(context)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                shakeListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI
            )

            onDispose {
                sensorManager.unregisterListener(shakeListener)
            }
        } else {
            onDispose { }
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
                text = "Dado ${selectedDice.label}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Activar / desactivar agitar ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Agitar para tirar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (shakeToRollEnabled) "Activado" else "Desactivado",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (shakeToRollEnabled)
                                Color(0xFF4CAF50)
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = shakeToRollEnabled,
                        onCheckedChange = { viewModel.onShakeToggle(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Selección de dado ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
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
                                    onClick = {
                                        if (!isRolling) viewModel.selectDice(dice)
                                    },
                                    label = {
                                        Text(
                                            text = dice.label,
                                            fontSize = 25.sp,
                                            fontWeight = if (selectedDice == dice) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
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
                                    onClick = {
                                        if (!isRolling) viewModel.selectDice(dice)
                                    },
                                    label = {
                                        Text(
                                            text = dice.label,
                                            fontSize = 25.sp,
                                            fontWeight = if (selectedDice == dice) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
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

            // --- Dado principal ---
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
                                    getDiceColor(diceValue, selectedDice.sides),
                                    getDiceColor(diceValue, selectedDice.sides).copy(alpha = 0.7f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isRolling) "?" else diceValue.toString(),
                        fontSize = if (diceValue >= 100) 60.sp else 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (!isRolling && diceValue > 0) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = getResultColor(diceValue, selectedDice.sides)
                        ),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = getResultMessage(diceValue, selectedDice.sides),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = if (shakeToRollEnabled)
                    "Puedes agitar para lanzar el dado"
                else
                    "Usa el botón para lanzar el dado",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = { viewModel.rollDice(context) },
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
                    text = if (isRolling) "Tirando..." else "Tirar ${selectedDice.label}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Historial ---
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
                            Text(
                                text = "Promedio: ${rollHistory.average().toInt()}",
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
fun getDiceColor(value: Int, maxValue: Int): Color {
    return when {
        value == maxValue -> Color(0xFF4CAF50)
        value == 1 -> Color(0xFFE53935)
        value >= (maxValue * 0.75).toInt() -> Color(0xFF2196F3)
        value <= (maxValue * 0.25).toInt() -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.primary
    }
}

@Composable
fun getResultColor(value: Int, maxValue: Int): Color {
    return when {
        value == maxValue -> Color(0xFF4CAF50).copy(alpha = 0.3f)
        value == 1 -> Color(0xFFE53935).copy(alpha = 0.3f)
        value >= (maxValue * 0.75).toInt() -> Color(0xFF2196F3).copy(alpha = 0.3f)
        value <= (maxValue * 0.25).toInt() -> Color(0xFFFF9800).copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.primaryContainer
    }
}

fun getResultMessage(value: Int, maxValue: Int): String {
    return when {
        value == maxValue -> " ¡CRÍTICO PERFECTO! "
        value == 1 -> " ¡FALLO CRÍTICO! "
        value >= (maxValue * 0.9).toInt() -> " ¡Excelente tirada!"
        value >= (maxValue * 0.75).toInt() -> " ¡Muy buena tirada!"
        value >= (maxValue * 0.5).toInt() -> " Tirada decente"
        value >= (maxValue * 0.3).toInt() -> " Tirada regular"
        value <= (maxValue * 0.25).toInt() -> " Tirada baja..."
        else -> "Resultado: $value"
    }
}

@Preview(showBackground = true)
@Composable
fun DiceScreenPreview() {
    MaterialTheme {
        DiceScreen()
    }
}
