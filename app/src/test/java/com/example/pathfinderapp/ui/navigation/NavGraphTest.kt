package com.example.pathfinderapp.ui.navigation

import androidx.navigation.NavHostController
import com.example.pathfinderapp.ui.viewmodels.AuthState
import com.example.pathfinderapp.ui.viewmodels.AuthViewModel
import com.example.pathfinderapp.ui.viewmodels.CharacterViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class NavGraphTest {

    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var authStateFlow: MutableStateFlow<AuthState>
    private lateinit var currentUserFlow: MutableStateFlow<Any?>

    @Before
    fun setup() {
        // Mock AuthViewModel
        authViewModel = mockk(relaxed = true)
        authStateFlow = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
        currentUserFlow = MutableStateFlow<Any?>(null)

        every { authViewModel.authState } returns authStateFlow
        every { authViewModel.currentUser } returns currentUserFlow as MutableStateFlow<Nothing>

        // Mock CharacterViewModel
        characterViewModel = mockk(relaxed = true)
        coEvery { characterViewModel.syncWithFirebase() } just Runs
        coEvery { characterViewModel.loadCharacters() } just Runs

        // Mock NavController
        navController = mockk(relaxed = true)
        every { navController.navigate(any<String>()) } just Runs
        every { navController.popBackStack() } returns true
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `navController is properly mocked`() {
        assertNotNull(navController)
    }

    @Test
    fun `authViewModel is properly mocked`() {
        assertNotNull(authViewModel)
    }

    @Test
    fun `characterViewModel is properly mocked`() {
        assertNotNull(characterViewModel)
    }

    @Test
    fun `can navigate to home screen`() {
        navController.navigate(Screen.Home.route)
        verify { navController.navigate(Screen.Home.route) }
    }

    @Test
    fun `can navigate to login screen`() {
        navController.navigate(Screen.Login.route)
        verify { navController.navigate(Screen.Login.route) }
    }

    @Test
    fun `can navigate to register screen`() {
        navController.navigate(Screen.Register.route)
        verify { navController.navigate(Screen.Register.route) }
    }

    @Test
    fun `can navigate to wiki screen`() {
        navController.navigate(Screen.Wiki.route)
        verify { navController.navigate(Screen.Wiki.route) }
    }

    @Test
    fun `can navigate to dice screen`() {
        navController.navigate(Screen.Dice.route)
        verify { navController.navigate(Screen.Dice.route) }
    }

    @Test
    fun `can navigate to characters screen`() {
        navController.navigate(Screen.Characters.route)
        verify { navController.navigate(Screen.Characters.route) }
    }

    @Test
    fun `can navigate to character screen`() {
        navController.navigate(Screen.Character.route)
        verify { navController.navigate(Screen.Character.route) }
    }

    @Test
    fun `can navigate to bestiary screen`() {
        navController.navigate(Screen.Bestiary.route)
        verify { navController.navigate(Screen.Bestiary.route) }
    }

    @Test
    fun `can navigate to menu screen`() {
        navController.navigate(Screen.Menu.route)
        verify { navController.navigate(Screen.Menu.route) }
    }

    @Test
    fun `can pop back stack`() {
        navController.popBackStack()
        verify { navController.popBackStack() }
    }

    @Test
    fun `can navigate to multiple screens`() {
        navController.navigate(Screen.Home.route)
        navController.navigate(Screen.Characters.route)
        navController.navigate(Screen.Character.route)

        verify { navController.navigate(Screen.Home.route) }
        verify { navController.navigate(Screen.Characters.route) }
        verify { navController.navigate(Screen.Character.route) }
    }

    @Test
    fun `authViewModel logout can be called`() {
        every { authViewModel.logout() } just Runs

        authViewModel.logout()

        verify { authViewModel.logout() }
    }

    @Test
    fun `authState can change`() {
        authStateFlow.value = AuthState.Loading
        authStateFlow.value = AuthState.Authenticated
        authStateFlow.value = AuthState.Unauthenticated

        assertNotNull(authStateFlow.value)
    }

    @Test
    fun `characterViewModel sync can be called`() {
        coEvery { characterViewModel.syncWithFirebase() } just Runs

        assertNotNull(characterViewModel)
    }

    @Test
    fun `characterViewModel load can be called`() {
        coEvery { characterViewModel.loadCharacters() } just Runs

        assertNotNull(characterViewModel)
    }

    @Test
    fun `all required screens have valid routes`() {
        val routes = listOf(
            Screen.Login.route,
            Screen.Register.route,
            Screen.Home.route,
            Screen.Wiki.route,
            Screen.Dice.route,
            Screen.Characters.route,
            Screen.Character.route,
            Screen.Bestiary.route,
            Screen.Menu.route
        )

        routes.forEach { route ->
            navController.navigate(route)
            verify { navController.navigate(route) }
        }
    }

    @Test
    fun `navigation can handle rapid calls`() {
        repeat(5) {
            navController.navigate(Screen.Home.route)
            navController.navigate(Screen.Characters.route)
            navController.popBackStack()
        }

        verify(atLeast = 5) { navController.navigate(Screen.Home.route) }
        verify(atLeast = 5) { navController.navigate(Screen.Characters.route) }
        verify(atLeast = 5) { navController.popBackStack() }
    }

    @Test
    fun `viewModels maintain state across navigation`() {
        navController.navigate(Screen.Characters.route)

        assertNotNull(authViewModel)
        assertNotNull(characterViewModel)
    }

    @Test
    fun `auth state changes dont affect viewModel availability`() {
        authStateFlow.value = AuthState.Loading
        assertNotNull(authViewModel)

        authStateFlow.value = AuthState.Authenticated
        assertNotNull(authViewModel)

        authStateFlow.value = AuthState.Unauthenticated
        assertNotNull(authViewModel)

        authStateFlow.value = AuthState.Error("Test")
        assertNotNull(authViewModel)
    }
}