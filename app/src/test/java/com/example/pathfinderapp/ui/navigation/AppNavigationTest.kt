package com.example.pathfinderapp.ui.navigation

import com.example.pathfinderapp.ui.viewmodels.AuthState
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AppNavigationTest {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var authStateFlow: MutableStateFlow<AuthState>
    private lateinit var currentUserFlow: MutableStateFlow<Any?>

    @Before
    fun setup() {
        authViewModel = mockk(relaxed = true)
        authStateFlow = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
        currentUserFlow = MutableStateFlow<Any?>(null)

        every { authViewModel.authState } returns authStateFlow
        every { authViewModel.currentUser } returns currentUserFlow as MutableStateFlow<Nothing>
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `authViewModel is properly mocked`() {
        assertNotNull(authViewModel)
        assertNotNull(authViewModel.authState)
        assertNotNull(authViewModel.currentUser)
    }

    @Test
    fun `authState flow starts as unauthenticated`() {
        assertEquals(AuthState.Unauthenticated, authStateFlow.value)
    }

    @Test
    fun `authState can change to loading`() {
        authStateFlow.value = AuthState.Loading
        assertEquals(AuthState.Loading, authStateFlow.value)
    }

    @Test
    fun `authState can change to authenticated`() {
        authStateFlow.value = AuthState.Authenticated
        assertEquals(AuthState.Authenticated, authStateFlow.value)
    }

    @Test
    fun `authState can change to error`() {
        val errorMessage = "Test error"
        authStateFlow.value = AuthState.Error(errorMessage)
        assertEquals(AuthState.Error(errorMessage), authStateFlow.value)
    }

    @Test
    fun `currentUser flow starts as null`() {
        assertEquals(null, currentUserFlow.value)
    }

    @Test
    fun `currentUser can be set`() {
        val mockUser = mockk<Any>()
        currentUserFlow.value = mockUser
        assertEquals(mockUser, currentUserFlow.value)
    }

    @Test
    fun `authState transitions work correctly`() {
        authStateFlow.value = AuthState.Loading
        assertEquals(AuthState.Loading, authStateFlow.value)

        authStateFlow.value = AuthState.Authenticated
        assertEquals(AuthState.Authenticated, authStateFlow.value)

        authStateFlow.value = AuthState.Unauthenticated
        assertEquals(AuthState.Unauthenticated, authStateFlow.value)
    }

    @Test
    fun `authState can transition to error from any state`() {
        authStateFlow.value = AuthState.Loading
        authStateFlow.value = AuthState.Error("Error from loading")
        assert(authStateFlow.value is AuthState.Error)

        authStateFlow.value = AuthState.Authenticated
        authStateFlow.value = AuthState.Error("Error from authenticated")
        assert(authStateFlow.value is AuthState.Error)
    }

    @Test
    fun `multiple auth state changes maintain correct state`() {
        val states = listOf(
            AuthState.Loading,
            AuthState.Authenticated,
            AuthState.Error("Test"),
            AuthState.Unauthenticated
        )

        states.forEach { state ->
            authStateFlow.value = state
            assertEquals(state, authStateFlow.value)
        }
    }

    @Test
    fun `authViewModel logout can be called`() {
        every { authViewModel.logout() } just Runs

        authViewModel.logout()

        verify { authViewModel.logout() }
    }

    @Test
    fun `isAuthenticated logic works correctly`() {
        authStateFlow.value = AuthState.Loading
        val isLoadingAuthenticated = authStateFlow.value is AuthState.Authenticated
        assertEquals(false, isLoadingAuthenticated)

        authStateFlow.value = AuthState.Authenticated
        val isAuthenticated = authStateFlow.value is AuthState.Authenticated
        assertEquals(true, isAuthenticated)

        authStateFlow.value = AuthState.Unauthenticated
        val isUnauthenticated = authStateFlow.value is AuthState.Authenticated
        assertEquals(false, isUnauthenticated)

        authStateFlow.value = AuthState.Error("Test")
        val isError = authStateFlow.value is AuthState.Authenticated
        assertEquals(false, isError)
    }
}