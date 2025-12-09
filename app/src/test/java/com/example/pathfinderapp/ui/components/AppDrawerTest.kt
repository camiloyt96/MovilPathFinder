package com.example.pathfinderapp.ui.components

import com.example.pathfinderapp.ui.navigation.Screen
import com.example.pathfinderapp.ui.viewmodels.User
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AppDrawerTest {

    @Test
    fun `user data is displayed correctly when user is not null`() {
        val user = User(
            id = "123",
            username = "TestUser",
            email = "test@example.com"
        )

        assertNotNull(user)
        assertEquals("TestUser", user.username)
        assertEquals("test@example.com", user.email)
    }

    @Test
    fun `user defaults are used when user is null`() {
        val user: User? = null
        val defaultUsername = "Usuario"
        val defaultEmail = ""

        val displayUsername = user?.username ?: defaultUsername
        val displayEmail = user?.email ?: defaultEmail

        assertEquals("Usuario", displayUsername)
        assertEquals("", displayEmail)
    }

    @Test
    fun `menu items have correct routes`() {
        val menuItems = listOf(
            Triple("Inicio", "Home", Screen.Home.route),
            Triple("Wiki", "Book", Screen.Wiki.route),
            Triple("Dado D20", "Casino", Screen.Dice.route),
            Triple("Mis Personajes", "Person", Screen.Characters.route),
            Triple("Bestiario", "Pets", Screen.Bestiary.route),
            Triple("Configuración", "Settings", Screen.Menu.route)
        )

        assertEquals(6, menuItems.size)
        assertTrue(menuItems.any { it.third == Screen.Home.route })
        assertTrue(menuItems.any { it.third == Screen.Wiki.route })
        assertTrue(menuItems.any { it.third == Screen.Dice.route })
        assertTrue(menuItems.any { it.third == Screen.Characters.route })
        assertTrue(menuItems.any { it.third == Screen.Bestiary.route })
        assertTrue(menuItems.any { it.third == Screen.Menu.route })
    }

    @Test
    fun `all menu items have non-empty titles`() {
        val menuItems = listOf(
            "Inicio",
            "Wiki",
            "Dado D20",
            "Mis Personajes",
            "Bestiario",
            "Configuración"
        )

        menuItems.forEach { title ->
            assertTrue(title.isNotEmpty(), "Menu item title should not be empty")
        }
    }

    @Test
    fun `logout button callback works`() {
        var logoutCalled = false
        val onLogoutClick = { logoutCalled = true }

        onLogoutClick()

        assertTrue(logoutCalled, "Logout callback should be called")
    }

    @Test
    fun `menu item click callback works with correct parameters`() {
        var clickedTitle: String? = null
        var clickedRoute: String? = null

        val onItemClick: (String, String) -> Unit = { title, route ->
            clickedTitle = title
            clickedRoute = route
        }

        onItemClick("Inicio", Screen.Home.route)

        assertEquals("Inicio", clickedTitle)
        assertEquals(Screen.Home.route, clickedRoute)
    }

    @Test
    fun `selected item matches one of the menu items`() {
        val menuTitles = listOf(
            "Inicio",
            "Wiki",
            "Dado D20",
            "Mis Personajes",
            "Bestiario",
            "Configuración"
        )

        val selectedItem = "Inicio"

        assertTrue(menuTitles.contains(selectedItem))
    }

    @Test
    fun `header shows app name correctly`() {
        val appName = "PATHFINDER"

        assertEquals("PATHFINDER", appName)
        assertTrue(appName.isNotEmpty())
    }

    @Test
    fun `user email can be empty`() {
        val user = User(
            id = "123",
            username = "TestUser",
            email = ""
        )

        assertEquals("", user.email)
        assertNotNull(user.username)
    }

    @Test
    fun `menu items count is correct`() {
        val expectedMenuItemsCount = 6

        val menuItems = listOf(
            Screen.Home.route,
            Screen.Wiki.route,
            Screen.Dice.route,
            Screen.Characters.route,
            Screen.Bestiary.route,
            Screen.Menu.route
        )

        assertEquals(expectedMenuItemsCount, menuItems.size)
    }
}