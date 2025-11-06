<<<<<<< HEAD
// ui/screens/auth/RegisterScreen.kt (modificado)

package com.example.pathfinderapp.ui.screens.auth

import kotlin.reflect.KClass // ¡Añade esta importación para KClass!
=======
package com.example.pathfinderapp.ui.screens.auth

>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
<<<<<<< HEAD
import androidx.lifecycle.viewmodel.compose.viewModel // Importa esto

// Importa tu AuthRepository y FirebaseAppAuthRepository
import com.example.pathfinderapp.data.repository.FirebaseAppAuthRepository
import com.example.pathfinderapp.data.repository.AuthRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
@Composable
fun RegisterScreen(
    // onRegisterClick ahora solo notifica un éxito, el ViewModel maneja la lógica
    onRegisterSuccess: () -> Unit = {},
    onBackToLoginClick: () -> Unit = {},
    // Inyecta el ViewModel usando viewModel() para que el sistema lo maneje
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(FirebaseAppAuthRepository()))
) {
    // Observar los estados del ViewModel
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registrationSuccess by viewModel.registrationSuccess.collectAsState()

    // Estados de UI locales
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // Validation states (ahora usamos los valores del ViewModel)
=======

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onBackToLoginClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    // Validation states
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
    val isUsernameValid = username.length >= 3
    val isEmailValid = email.contains("@") && email.contains(".")
    val isPasswordValid = password.length >= 6
    val doPasswordsMatch = password == confirmPassword && password.isNotBlank()
<<<<<<< HEAD
    // La validación final para el botón también incluye los términos
    val isFormValid = isUsernameValid && isEmailValid && isPasswordValid &&
            doPasswordsMatch && acceptTerms

    // Efecto para reaccionar al éxito del registro
    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            onRegisterSuccess() // Llama al callback de éxito para navegar
            viewModel.resetRegistrationState() // Opcional: resetear estado del VM si la pantalla se reutiliza
        }
    }

=======
    val isFormValid = isUsernameValid && isEmailValid && isPasswordValid &&
            doPasswordsMatch && acceptTerms

>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
<<<<<<< HEAD
=======
        IconButton(
            onClick = onThemeToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {

        }

>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

<<<<<<< HEAD
            // ... (Resto de tu UI, sin cambios en esta sección) ...
=======
            // Header
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🛡️",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Únete a la aventura en Pathfinder",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Username field
            OutlinedTextField(
<<<<<<< HEAD
                value = username, // Usa el estado del ViewModel
                onValueChange = { viewModel.onUsernameChange(it) }, // Llama a la función del ViewModel
=======
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = null
                },
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                label = { Text("Nombre de Usuario") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Usuario")
                },
                trailingIcon = {
                    if (username.isNotBlank()) {
                        Icon(
                            imageVector = if (isUsernameValid)
                                Icons.Default.CheckCircle
                            else
                                Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isUsernameValid)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    }
                },
                supportingText = {
                    if (username.isNotBlank() && !isUsernameValid) {
                        Text("Mínimo 3 caracteres")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = username.isNotBlank() && !isUsernameValid
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            OutlinedTextField(
<<<<<<< HEAD
                value = email, // Usa el estado del ViewModel
                onValueChange = { viewModel.onEmailChange(it) }, // Llama a la función del ViewModel
=======
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                trailingIcon = {
                    if (email.isNotBlank()) {
                        Icon(
                            imageVector = if (isEmailValid)
                                Icons.Default.CheckCircle
                            else
                                Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isEmailValid)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    }
                },
                supportingText = {
                    if (email.isNotBlank() && !isEmailValid) {
                        Text("Email inválido")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = email.isNotBlank() && !isEmailValid
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
<<<<<<< HEAD
                value = password, // Usa el estado del ViewModel
                onValueChange = { viewModel.onPasswordChange(it) }, // Llama a la función del ViewModel
=======
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                "Ocultar contraseña"
                            else
                                "Mostrar contraseña"
                        )
                    }
                },
                supportingText = {
                    if (password.isNotBlank() && !isPasswordValid) {
                        Text("Mínimo 6 caracteres")
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = password.isNotBlank() && !isPasswordValid
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm password field
            OutlinedTextField(
<<<<<<< HEAD
                value = confirmPassword, // Usa el estado del ViewModel
                onValueChange = { viewModel.onConfirmPasswordChange(it) }, // Llama a la función del ViewModel
=======
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = null
                },
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                label = { Text("Confirmar Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Confirmar")
                },
                trailingIcon = {
                    Row {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                        if (confirmPassword.isNotBlank() && password.isNotBlank()) {
                            Icon(
                                imageVector = if (doPasswordsMatch)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (doPasswordsMatch)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                },
                supportingText = {
                    if (confirmPassword.isNotBlank() && !doPasswordsMatch) {
                        Text("Las contraseñas no coinciden")
                    }
                },
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = confirmPassword.isNotBlank() && !doPasswordsMatch
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password strength indicator
            if (password.isNotBlank()) {
                PasswordStrengthIndicator(password)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Terms and conditions
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { acceptTerms = !acceptTerms }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = acceptTerms,
                        onCheckedChange = { acceptTerms = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Acepto los Términos y Condiciones y la Política de Privacidad",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

<<<<<<< HEAD
            // Error message (ahora muestra el errorMessage del ViewModel)
=======
            // Error message
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register button
            Button(
                onClick = {
<<<<<<< HEAD
                    // La validación interna del ViewModel ya se encargará,
                    // pero la UI puede dar feedback rápido si los campos no son válidos
                    if (isFormValid) { // Solo llama si la validación de la UI es correcta
                        viewModel.registerUser()
                    } else {
                        // Opcional: Mostrar un error genérico si el formulario no es válido
                        // El ViewModel también puede hacer esto en registerUser()
                    }
=======
                    if (!isFormValid) {
                        errorMessage = when {
                            !isUsernameValid -> "El nombre de usuario debe tener al menos 3 caracteres"
                            !isEmailValid -> "Por favor ingresa un email válido"
                            !isPasswordValid -> "La contraseña debe tener al menos 6 caracteres"
                            !doPasswordsMatch -> "Las contraseñas no coinciden"
                            !acceptTerms -> "Debes aceptar los términos y condiciones"
                            else -> "Por favor completa todos los campos"
                        }
                        return@Button
                    }
                    isLoading = true
                    onRegisterClick(username, email, password, confirmPassword)
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
<<<<<<< HEAD
                enabled = isFormValid && !isLoading // El botón se deshabilita con isFormValid y isLoading
=======
                enabled = isFormValid && !isLoading
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Crear Cuenta",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back to login
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Inicia Sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onBackToLoginClick() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

<<<<<<< HEAD
// ... (El resto de tus funciones como PasswordStrengthIndicator y calculatePasswordStrength, sin cambios) ...

=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = calculatePasswordStrength(password)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Fortaleza de contraseña: ${strength.first}",
            style = MaterialTheme.typography.labelSmall,
            color = strength.second
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { strength.third },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = strength.second,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

fun calculatePasswordStrength(password: String): Triple<String, Color, Float> {
    var score = 0

    if (password.length >= 8) score++
    if (password.length >= 12) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isLowerCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    return when (score) {
        0, 1, 2 -> Triple("Débil", Color.Red, 0.33f)
        3, 4 -> Triple("Media", Color(0xFFFFA726), 0.66f)
        else -> Triple("Fuerte", Color.Green, 1f)
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen()
    }
<<<<<<< HEAD
}

// Necesitas un ViewModelFactory para pasar dependencias al ViewModel
class RegisterViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
=======
>>>>>>> ac7f3c53888c40f5cbb7cc2976737976f9aaba88
}