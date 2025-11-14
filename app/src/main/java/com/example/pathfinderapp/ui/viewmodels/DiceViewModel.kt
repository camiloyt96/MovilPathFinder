package com.example.pathfinderapp.ui.viewmodel

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinderapp.ui.screens.DiceType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceViewModel : ViewModel() {

    // --- Estados principales ---
    private val _selectedDice = MutableStateFlow(DiceType.D4)
    val selectedDice = _selectedDice.asStateFlow()

    private val _diceValue = MutableStateFlow(DiceType.D4.sides)
    val diceValue = _diceValue.asStateFlow()

    private val _isRolling = MutableStateFlow(false)
    val isRolling = _isRolling.asStateFlow()

    private val _rollHistory = MutableStateFlow<List<Int>>(emptyList())
    val rollHistory = _rollHistory.asStateFlow()

    private val _shakeToRollEnabled = MutableStateFlow(true)
    val shakeToRollEnabled = _shakeToRollEnabled.asStateFlow()

    private val _rotationValue = MutableStateFlow(0f)
    val rotationValue = _rotationValue.asStateFlow()

    private val _scaleValue = MutableStateFlow(1f)
    val scaleValue = _scaleValue.asStateFlow()

    // --- Funciones de actualización de estado ---
    fun onShakeToggle(enabled: Boolean) {
        _shakeToRollEnabled.value = enabled
    }

    fun selectDice(dice: DiceType) {
        if (_isRolling.value) return
        _selectedDice.value = dice
        _rollHistory.value = emptyList()
        _diceValue.value = dice.sides
    }

    // --- Lógica principal: lanzar dado ---
    fun rollDice(context: Context?) {
        if (_isRolling.value) return

        _isRolling.value = true
        viewModelScope.launch {
            repeat(10) { i ->
                _rotationValue.value = i * 72f
                _scaleValue.value = if (i % 2 == 0) 1.2f else 1.0f
                delay(100)
            }

            val newValue = Random.nextInt(1, _selectedDice.value.sides + 1)
            _diceValue.value = newValue
            _rollHistory.value = (listOf(newValue) + _rollHistory.value).take(10)

            _rotationValue.value = 0f
            _scaleValue.value = 1f
            _isRolling.value = false

            // Vibración (si está activada)
            if (_shakeToRollEnabled.value && context != null) {
                vibrateDevice(context)
            }
        }
    }

    // --- Función de vibración del dispositivo ---
    private fun vibrateDevice(context: Context, duration: Long = 150) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
