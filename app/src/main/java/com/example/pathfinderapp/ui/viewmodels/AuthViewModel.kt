package com.example.pathfinderapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinderapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkSavedSession()
    }

    private fun checkSavedSession() {
        viewModelScope.launch {
            val firebaseUser = authRepository.getCurrentUser()
            if (firebaseUser != null) {
                _currentUser.value = User(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "Usuario",
                    email = firebaseUser.email ?: ""
                )
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.loginWithEmailAndPassword(email, password)

            result.onSuccess { firebaseUser ->
                _currentUser.value = User(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName ?: email.substringBefore("@"),
                    email = firebaseUser.email ?: email
                )
                _authState.value = AuthState.Authenticated
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun register(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
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

                val result = authRepository.registerWithEmailAndPassword(email, password)

                result.onSuccess { firebaseUser ->
                    _currentUser.value = User(
                        id = firebaseUser.uid,
                        username = username,
                        email = firebaseUser.email ?: email
                    )
                    _authState.value = AuthState.Authenticated
                }.onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Error al registrar")
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error al registrar: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
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

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

data class User(
    val id: String,
    val username: String,
    val email: String
)