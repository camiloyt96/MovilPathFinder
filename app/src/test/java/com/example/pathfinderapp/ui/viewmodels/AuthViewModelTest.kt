package com.example.pathfinderapp.ui.viewmodels

import com.example.pathfinderapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var mockAuthRepository: AuthRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockAuthRepository = mockk(relaxed = true)

        // Mock para que getCurrentUser devuelva null por defecto
        coEvery { mockAuthRepository.getCurrentUser() } returns null

        authViewModel = AuthViewModel(mockAuthRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    // ==================== ESTADO INICIAL ====================

    @Test
    fun `estado inicial es Unauthenticated cuando no hay sesion`() {
        assertTrue(authViewModel.authState.value is AuthState.Unauthenticated)
        assertNull(authViewModel.currentUser.value)
    }

    @Test
    fun `estado inicial es Authenticated cuando hay sesion guardada`() {
        // Arrange
        val mockUser = mockk<FirebaseUser> {
            every { uid } returns "user123"
            every { email } returns "test@example.com"
            every { displayName } returns "TestUser"
        }
        coEvery { mockAuthRepository.getCurrentUser() } returns mockUser

        // Act
        val vm = AuthViewModel(mockAuthRepository)

        // Assert
        assertTrue(vm.authState.value is AuthState.Authenticated)
        assertEquals("user123", vm.currentUser.value?.id)
        assertEquals("TestUser", vm.currentUser.value?.username)
        assertEquals("test@example.com", vm.currentUser.value?.email)
    }

    // ==================== LOGIN ====================

    @Test
    fun `login exitoso actualiza estado a Authenticated`() {
        // Arrange
        val mockUser = mockk<FirebaseUser> {
            every { uid } returns "user123"
            every { email } returns "test@example.com"
            every { displayName } returns null
        }
        coEvery { mockAuthRepository.loginWithEmailAndPassword(any(), any()) } returns Result.success(mockUser)

        // Act
        authViewModel.login("test@example.com", "password123")

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Authenticated)
        assertEquals("user123", authViewModel.currentUser.value?.id)
        assertEquals("test", authViewModel.currentUser.value?.username)
        coVerify { mockAuthRepository.loginWithEmailAndPassword("test@example.com", "password123") }
    }

    @Test
    fun `login fallido actualiza estado a Error`() {
        // Arrange
        coEvery { mockAuthRepository.loginWithEmailAndPassword(any(), any()) } returns
                Result.failure(Exception("Credenciales inválidas"))

        // Act
        authViewModel.login("wrong@example.com", "wrongpass")

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Error)
        val errorState = authViewModel.authState.value as AuthState.Error
        assertEquals("Credenciales inválidas", errorState.message)
    }

    @Test
    fun `login pone estado Loading antes de completar`() {
        // Arrange
        val mockUser = mockk<FirebaseUser>(relaxed = true)
        coEvery { mockAuthRepository.loginWithEmailAndPassword(any(), any()) } returns Result.success(mockUser)

        // Act - El login se ejecuta síncronamente con UnconfinedTestDispatcher
        authViewModel.login("test@example.com", "pass")

        // Assert - Verifica que se llamó al repository
        coVerify { mockAuthRepository.loginWithEmailAndPassword("test@example.com", "pass") }
    }

    // ==================== REGISTER ====================

    @Test
    fun `register exitoso actualiza estado a Authenticated`() {
        // Arrange
        val mockUser = mockk<FirebaseUser> {
            every { uid } returns "newuser123"
            every { email } returns "new@example.com"
            every { displayName } returns null
        }
        coEvery { mockAuthRepository.registerWithEmailAndPassword(any(), any()) } returns Result.success(mockUser)

        // Act
        authViewModel.register("NewUser", "new@example.com", "password123", "password123")

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Authenticated)
        assertEquals("newuser123", authViewModel.currentUser.value?.id)
        assertEquals("NewUser", authViewModel.currentUser.value?.username)
        coVerify { mockAuthRepository.registerWithEmailAndPassword("new@example.com", "password123") }
    }

    @Test
    fun `register con passwords diferentes muestra error`() {
        // Act
        authViewModel.register("User", "test@example.com", "pass123", "differentpass")

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Error)
        val errorState = authViewModel.authState.value as AuthState.Error
        assertEquals("Las contraseñas no coinciden", errorState.message)
        coVerify(exactly = 0) { mockAuthRepository.registerWithEmailAndPassword(any(), any()) }
    }

    @Test
    fun `register con username corto muestra error`() {
        // Act
        authViewModel.register("Ab", "test@example.com", "password", "password")

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Error)
        val errorState = authViewModel.authState.value as AuthState.Error
        assertEquals("Usuario muy corto", errorState.message)
    }

    @Test
    fun `register con email invalido muestra error`() {
        // Act
        authViewModel.register("Username", "notanemail", "password", "password")

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Error)
        val errorState = authViewModel.authState.value as AuthState.Error
        assertEquals("Email inválido", errorState.message)
    }

    @Test
    fun `register con password corta muestra error`() {
        // Act
        authViewModel.register("Username", "test@example.com", "12345", "12345")

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Error)
        val errorState = authViewModel.authState.value as AuthState.Error
        assertEquals("Contraseña muy corta", errorState.message)
    }

    // ==================== LOGOUT ====================

    @Test
    fun `logout limpia usuario y actualiza estado`() {
        // Arrange - Primero hacer login
        val mockUser = mockk<FirebaseUser>(relaxed = true) {
            every { uid } returns "user123"
            every { email } returns "test@example.com"
        }
        coEvery { mockAuthRepository.loginWithEmailAndPassword(any(), any()) } returns Result.success(mockUser)
        authViewModel.login("test@example.com", "pass")

        // Act
        authViewModel.logout()

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Unauthenticated)
        assertNull(authViewModel.currentUser.value)
        coVerify { mockAuthRepository.logout() }
    }

    // ==================== CLEAR ERROR ====================

    @Test
    fun `clearError cambia de Error a Unauthenticated`() {
        // Arrange
        coEvery { mockAuthRepository.loginWithEmailAndPassword(any(), any()) } returns
                Result.failure(Exception("Error"))
        authViewModel.login("test@example.com", "wrong")

        // Act
        authViewModel.clearError()

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Unauthenticated)
    }

    @Test
    fun `clearError no afecta otros estados`() {
        // Arrange - Estado Authenticated
        val mockUser = mockk<FirebaseUser>(relaxed = true) {
            every { uid } returns "user123"
        }
        coEvery { mockAuthRepository.loginWithEmailAndPassword(any(), any()) } returns Result.success(mockUser)
        authViewModel.login("test@example.com", "pass")

        // Act
        authViewModel.clearError()

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Authenticated)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var mockAuthRepository: AuthRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockAuthRepository = mockk(relaxed = true)
        registerViewModel = RegisterViewModel(mockAuthRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    // ==================== ESTADO INICIAL ====================

    @Test
    fun `estado inicial todos los campos vacios`() {
        assertEquals("", registerViewModel.username.value)
        assertEquals("", registerViewModel.email.value)
        assertEquals("", registerViewModel.password.value)
        assertEquals("", registerViewModel.confirmPassword.value)
        assertFalse(registerViewModel.isLoading.value)
        assertNull(registerViewModel.errorMessage.value)
        assertFalse(registerViewModel.registrationSuccess.value)
    }

    // ==================== CAMBIOS DE CAMPOS ====================

    @Test
    fun `onUsernameChange actualiza username`() {
        registerViewModel.onUsernameChange("TestUser")
        assertEquals("TestUser", registerViewModel.username.value)
    }

    @Test
    fun `onEmailChange actualiza email`() {
        registerViewModel.onEmailChange("test@example.com")
        assertEquals("test@example.com", registerViewModel.email.value)
    }

    @Test
    fun `onPasswordChange actualiza password`() {
        registerViewModel.onPasswordChange("password123")
        assertEquals("password123", registerViewModel.password.value)
    }

    @Test
    fun `onConfirmPasswordChange actualiza confirmPassword`() {
        registerViewModel.onConfirmPasswordChange("password123")
        assertEquals("password123", registerViewModel.confirmPassword.value)
    }

    @Test
    fun `cambiar campo limpia errorMessage`() {
        // Arrange - Crear un error
        registerViewModel.registerUser()
        assertNotNull(registerViewModel.errorMessage.value)

        // Act
        registerViewModel.onUsernameChange("User")

        // Assert
        assertNull(registerViewModel.errorMessage.value)
    }

    // ==================== VALIDACIONES ====================

    @Test
    fun `registerUser con campos vacios muestra error`() {
        registerViewModel.registerUser()

        assertEquals("Por favor, completa todos los campos.", registerViewModel.errorMessage.value)
        assertFalse(registerViewModel.isLoading.value)
        coVerify(exactly = 0) { mockAuthRepository.registerWithEmailAndPassword(any(), any()) }
    }

    @Test
    fun `registerUser con passwords diferentes muestra error`() {
        // Arrange
        registerViewModel.onUsernameChange("User")
        registerViewModel.onEmailChange("test@example.com")
        registerViewModel.onPasswordChange("pass123")
        registerViewModel.onConfirmPasswordChange("differentpass")

        // Act
        registerViewModel.registerUser()

        // Assert
        assertEquals("Las contraseñas no coinciden.", registerViewModel.errorMessage.value)
        coVerify(exactly = 0) { mockAuthRepository.registerWithEmailAndPassword(any(), any()) }
    }

    // ==================== REGISTRO EXITOSO ====================

    @Test
    fun `register exitoso actualiza registrationSuccess`() {
        // Arrange
        val mockUser = mockk<FirebaseUser> {
            every { uid } returns "user123"
            every { email } returns "test@example.com"
        }
        coEvery { mockAuthRepository.registerWithEmailAndPassword(any(), any()) } returns Result.success(mockUser)

        // Act
        registerViewModel.register("TestUser", "test@example.com", "password123", "password123")

        // Assert
        assertTrue(registerViewModel.registrationSuccess.value)
        assertNull(registerViewModel.errorMessage.value)
        coVerify { mockAuthRepository.registerWithEmailAndPassword("test@example.com", "password123") }
    }

    @Test
    fun `register fallido actualiza errorMessage`() {
        // Arrange
        coEvery { mockAuthRepository.registerWithEmailAndPassword(any(), any()) } returns
                Result.failure(Exception("Error de registro"))

        // Act
        registerViewModel.register("User", "test@example.com", "password", "password")

        // Assert
        assertFalse(registerViewModel.registrationSuccess.value)
        assertTrue(registerViewModel.errorMessage.value?.contains("Error de registro") == true)
    }

    // ==================== RESET STATE ====================

    @Test
    fun `resetRegistrationState limpia todos los campos`() {
        // Arrange - Llenar campos
        registerViewModel.onUsernameChange("User")
        registerViewModel.onEmailChange("test@example.com")
        registerViewModel.onPasswordChange("pass")
        registerViewModel.onConfirmPasswordChange("pass")

        // Act
        registerViewModel.resetRegistrationState()

        // Assert
        assertEquals("", registerViewModel.username.value)
        assertEquals("", registerViewModel.email.value)
        assertEquals("", registerViewModel.password.value)
        assertEquals("", registerViewModel.confirmPassword.value)
        assertFalse(registerViewModel.registrationSuccess.value)
        assertNull(registerViewModel.errorMessage.value)
        assertFalse(registerViewModel.isLoading.value)
    }

    // ==================== METODO REGISTER ====================

    @Test
    fun `metodo register llena campos y ejecuta registerUser`() {
        // Arrange
        val mockUser = mockk<FirebaseUser>(relaxed = true)
        coEvery { mockAuthRepository.registerWithEmailAndPassword(any(), any()) } returns Result.success(mockUser)

        // Act
        registerViewModel.register("User", "test@example.com", "password123", "password123")

        // Assert
        assertEquals("User", registerViewModel.username.value)
        assertEquals("test@example.com", registerViewModel.email.value)
        assertEquals("password123", registerViewModel.password.value)
        assertEquals("password123", registerViewModel.confirmPassword.value)
        coVerify { mockAuthRepository.registerWithEmailAndPassword("test@example.com", "password123") }
    }
}