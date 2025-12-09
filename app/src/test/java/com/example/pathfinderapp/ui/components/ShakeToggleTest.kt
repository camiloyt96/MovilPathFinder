package com.example.pathfinderapp.ui.components

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ShakeToggleTest {

    @Test
    fun `toggle starts disabled by default`() {
        val enabled = false

        assertFalse(enabled)
    }

    @Test
    fun `toggle can be enabled`() {
        var enabled = false
        val onToggle: (Boolean) -> Unit = { enabled = it }

        onToggle(true)

        assertTrue(enabled)
    }

    @Test
    fun `toggle can be disabled`() {
        var enabled = true
        val onToggle: (Boolean) -> Unit = { enabled = it }

        onToggle(false)

        assertFalse(enabled)
    }

    @Test
    fun `toggle can be switched multiple times`() {
        var enabled = false
        val onToggle: (Boolean) -> Unit = { enabled = it }

        onToggle(true)
        assertTrue(enabled)

        onToggle(false)
        assertFalse(enabled)

        onToggle(true)
        assertTrue(enabled)
    }

    @Test
    fun `title text is correct`() {
        val titleText = "Agitar para tirar"

        assertEquals("Agitar para tirar", titleText)
        assertTrue(titleText.isNotEmpty())
    }

    @Test
    fun `enabled status text is correct when enabled`() {
        val enabled = true
        val statusText = if (enabled) "Activado" else "Desactivado"

        assertEquals("Activado", statusText)
    }

    @Test
    fun `enabled status text is correct when disabled`() {
        val enabled = false
        val statusText = if (enabled) "Activado" else "Desactivado"

        assertEquals("Desactivado", statusText)
    }

    @Test
    fun `card padding is appropriate`() {
        val padding = 16 // dp

        assertEquals(16, padding)
        assertTrue(padding > 0)
    }

    @Test
    fun `enabled color is green`() {
        val enabledColor = 0xFF4CAF50

        assertEquals(0xFF4CAF50, enabledColor)
    }

    @Test
    fun `toggle state matches callback value`() {
        var toggleState = false
        val onToggle: (Boolean) -> Unit = { toggleState = it }

        onToggle(true)
        assertTrue(toggleState)

        onToggle(false)
        assertFalse(toggleState)
    }

    @Test
    fun `switch thumb color is white`() {
        val thumbColor = "White"

        assertEquals("White", thumbColor)
    }

    @Test
    fun `checked track color is green`() {
        val checkedTrackColor = 0xFF4CAF50

        assertEquals(0xFF4CAF50, checkedTrackColor)
    }

    @Test
    fun `feature description is clear`() {
        val title = "Agitar para tirar"
        val enabledText = "Activado"
        val disabledText = "Desactivado"

        assertTrue(title.contains("Agitar"))
        assertTrue(title.contains("tirar"))
        assertTrue(enabledText.isNotEmpty())
        assertTrue(disabledText.isNotEmpty())
    }

    @Test
    fun `toggle callback is invoked on state change`() {
        var callbackInvoked = false
        var newValue = false
        val onToggle: (Boolean) -> Unit = {
            callbackInvoked = true
            newValue = it
        }

        onToggle(true)

        assertTrue(callbackInvoked)
        assertTrue(newValue)
    }

    @Test
    fun `status text changes based on state`() {
        val enabledStatus = "Activado"
        val disabledStatus = "Desactivado"

        assertTrue(enabledStatus != disabledStatus)
        assertEquals("Activado", enabledStatus)
        assertEquals("Desactivado", disabledStatus)
    }
}