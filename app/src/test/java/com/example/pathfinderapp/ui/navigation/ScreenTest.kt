package com.example.pathfinderapp.ui.navigation

import org.junit.Test
import kotlin.test.assertEquals


class ScreenTest {

    @Test
    fun `login screen should have correct route`() {
        assertEquals("login", Screen.Login.route)
    }

    @Test
    fun `register screen should have correct route`() {
        assertEquals("register", Screen.Register.route)
    }

    @Test
    fun `home screen should have correct route`() {
        assertEquals("home", Screen.Home.route)
    }

    @Test
    fun `wiki screen should have correct route`() {
        assertEquals("wiki", Screen.Wiki.route)
    }

    @Test
    fun `dice screen should have correct route`() {
        assertEquals("dice", Screen.Dice.route)
    }

    @Test
    fun `character screen should have correct route`() {
        assertEquals("character", Screen.Character.route)
    }

    @Test
    fun `characters screen should have correct route`() {
        assertEquals("characters", Screen.Characters.route)
    }

    @Test
    fun `menu screen should have correct route`() {
        assertEquals("menu", Screen.Menu.route)
    }

    @Test
    fun `bestiary screen should have correct route`() {
        assertEquals("bestiary", Screen.Bestiary.route)
    }

    @Test
    fun `all screens should have unique routes`() {
        val routes = listOf(
            Screen.Login.route,
            Screen.Register.route,
            Screen.Home.route,
            Screen.Wiki.route,
            Screen.Dice.route,
            Screen.Character.route,
            Screen.Characters.route,
            Screen.Menu.route,
            Screen.Bestiary.route
        )

        assertEquals(routes.size, routes.distinct().size, "All routes should be unique")
    }

    @Test
    fun `all routes should not be empty`() {
        val screens = listOf(
            Screen.Login,
            Screen.Register,
            Screen.Home,
            Screen.Wiki,
            Screen.Dice,
            Screen.Character,
            Screen.Characters,
            Screen.Menu,
            Screen.Bestiary
        )

        screens.forEach { screen ->
            assert(screen.route.isNotEmpty()) { "${screen::class.simpleName} route should not be empty" }
        }
    }

    @Test
    fun `all routes should not contain spaces`() {
        val screens = listOf(
            Screen.Login,
            Screen.Register,
            Screen.Home,
            Screen.Wiki,
            Screen.Dice,
            Screen.Character,
            Screen.Characters,
            Screen.Menu,
            Screen.Bestiary
        )

        screens.forEach { screen ->
            assert(!screen.route.contains(" ")) { "${screen::class.simpleName} route should not contain spaces" }
        }
    }

    @Test
    fun `all routes should be lowercase`() {
        val screens = listOf(
            Screen.Login,
            Screen.Register,
            Screen.Home,
            Screen.Wiki,
            Screen.Dice,
            Screen.Character,
            Screen.Characters,
            Screen.Menu,
            Screen.Bestiary
        )

        screens.forEach { screen ->
            assertEquals(screen.route, screen.route.lowercase(),
                "${screen::class.simpleName} route should be lowercase")
        }
    }
}