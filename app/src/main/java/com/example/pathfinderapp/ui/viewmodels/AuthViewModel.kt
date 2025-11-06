package com.example.pathfinderapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkSavedSession()
    }

    private fun checkSavedSession() {
        viewModelScope.launch {
            delay(1000) // Simula carga
            // TODO: Verificar SharedPreferences aquí
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                delay(1500) // Simula API

                if (email.isNotBlank() && password.isNotBlank()) {
                    val user = User(
                        id = "user_${System.currentTimeMillis()}",
                        username = email.substringBefore("@"),
                        email = email
                    )
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Credenciales inválidas")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error: ${e.message}")
            }
        }
    }

    fun register(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // Validaciones
                when {
                    password != confirmPassword -> {
                        _authState.value = AuthState.Error("Las contraseñas no coinciden")
                        return@launch
                    }
                    username.length < 3 -> {
                        _authState.value = AuthState.Error("Usuario muy corto")
                        return@launch
                    }
                    !email.contains("@") -> {
                        _authState.value = AuthState.Error("Email inválido")
                        return@launch
                    }
                    password.length < 6 -> {
                        _authState.value = AuthState.Error("Contraseña muy corta")
                        return@launch
                    }
                }

                delay(2000) // Simula registro

                val user = User(
                    id = "user_${System.currentTimeMillis()}",
                    username = username,
                    email = email
                )
                _currentUser.value = user
                _authState.value = AuthState.Authenticated

            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error al registrar: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
}

// Estados de autenticación
sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

// Modelo de usuario
data class User(
    val id: String,
    val username: String,
    val email: String
)