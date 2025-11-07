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

    // Estados para los campos de texto del formulario
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    // Estado para el indicador de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Estado para indicar si el registro fue exitoso
    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    // Funciones para actualizar los estados desde la UI
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

    // Función principal para manejar el registro
    fun registerUser() {
        println("🔥 registerUser() ejecutado con email=${email.value}")
        // Validación básica en el ViewModel antes de llamar al repositorio
        // Las validaciones más complejas (ej. longitud mínima) pueden estar en la UI para feedback inmediato
        // o también centralizadas aquí. Por simplicidad, asumimos que la UI ya hizo gran parte.
        if (email.value.isBlank() || password.value.isBlank() || username.value.isBlank() || confirmPassword.value.isBlank()) {
            _errorMessage.value = "Por favor, completa todos los campos."
            return
        }
        if (password.value != confirmPassword.value) {
            _errorMessage.value = "Las contraseñas no coinciden."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null // Limpiar cualquier error previo

        viewModelScope.launch {
            val result = authRepository.registerWithEmailAndPassword(email.value, password.value)
            _isLoading.value = false

            result.onSuccess { firebaseUser ->
                // Registro exitoso, puedes hacer algo con firebaseUser.displayName, etc.
                // Aquí es donde también podrías guardar el username en Firestore
                // o redirigir al usuario.
                _registrationSuccess.value = true
                _errorMessage.value = null // Asegurarse de que no haya error visible
                println("Registro exitoso para: ${firebaseUser.email}") // Solo para debug
            }.onFailure { exception ->
                // Manejar los errores específicos de Firebase Auth
                _errorMessage.value = when (exception) {
                    is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil. Necesita al menos 6 caracteres y una combinación más compleja."
                    is FirebaseAuthInvalidCredentialsException -> "El formato del email es inválido."
                    is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este email."
                    else -> "Error de registro: ${exception.localizedMessage ?: "Ocurrió un error desconocido."}"
                }
                _registrationSuccess.value = false
                println("Error de registro: ${exception.localizedMessage}") // Solo para debug
            }
        }
    }

    // Función para resetear el estado del registro (ej. si el usuario vuelve a la pantalla)
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
