// auth/RegisterViewModel.kt

package com.example.pathfinderapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinderapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
        _errorMessage.value = null // Limpiar error al cambiar input
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _errorMessage.value = null // Limpiar error al cambiar input
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _errorMessage.value = null // Limpiar error al cambiar input
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _errorMessage.value = null // Limpiar error al cambiar input
    }

    fun registerUser() {
        println("registerUser() ejecutado con email=${email.value}")
        if (email.value.isBlank() || password.value.isBlank() || username.value.isBlank() || confirmPassword.value.isBlank()) {
            _errorMessage.value = "Por favor, completa todos los campos."
            return
        }
        if (password.value != confirmPassword.value) {
            _errorMessage.value = "Las contraseñas no coinciden."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = authRepository.registerWithEmailAndPassword(email.value, password.value)
            _isLoading.value = false

            result.onSuccess { firebaseUser ->
                _registrationSuccess.value = true
                _errorMessage.value = null
                println("Registro exitoso para: ${firebaseUser.email}")
            }.onFailure { exception ->
                // Manejar los errores específicos de Firebase Auth
                _errorMessage.value = when (exception) {
                    is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil. Necesita al menos 6 caracteres y una combinación más compleja."
                    is FirebaseAuthInvalidCredentialsException -> "El formato del email es inválido."
                    is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este email."
                    else -> "Error de registro: ${exception.localizedMessage ?: "Ocurrió un error desconocido."}"
                }
                _registrationSuccess.value = false
                println("Error de registro: ${exception.localizedMessage}")
            }
        }
    }

    fun resetRegistrationState() {
        _registrationSuccess.value = false
        _errorMessage.value = null
        _isLoading.value = false
        _username.value = ""
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
    }

    fun register(username: String, email: String, password: String, confirmPassword: String) {
        _username.value = username
        _email.value = email
        _password.value = password
        _confirmPassword.value = confirmPassword
        registerUser()
    }
}
