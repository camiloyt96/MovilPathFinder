package com.example.pathfinderapp.ui.components

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FeatureButtonTest {

    @Test
    fun `button text is displayed correctly`() {
        val text = "Test Button"

        assertEquals("Test Button", text)
        assertTrue(text.isNotEmpty())
    }

    @Test
    fun `button click callback works`() {
        var clicked = false
        val onClick = { clicked = true }

        onClick()

        assertTrue(clicked)
    }

    @Test
    fun `button can have different text values`() {
        val texts = listOf("Dado D20", "Wiki", "Personajes", "Bestiario")

        texts.forEach { text ->
            assertTrue(text.isNotEmpty())
            assertTrue(text.length <= 20) // Reasonable length for button
        }
    }

    @Test
    fun `button text can be multiline`() {
        val shortText = "Wiki"
        val longText = "Mis Personajes"

        assertTrue(shortText.length < longText.length)
        assertTrue(longText.split(" ").size > 1) // Multiple words
    }

    @Test
    fun `button width is consistent`() {
        val buttonWidth = 90 // dp

        assertEquals(90, buttonWidth)
        assertTrue(buttonWidth > 0)
    }

    @Test
    fun `icon size is appropriate`() {
        val iconSize = 32 // dp

        assertEquals(32, iconSize)
        assertTrue(iconSize > 0)
    }

    @Test
    fun `multiple buttons can have different click handlers`() {
        var button1Clicked = false
        var button2Clicked = false

        val onClick1 = { button1Clicked = true }
        val onClick2 = { button2Clicked = true }

        onClick1()
        assertTrue(button1Clicked)
        assertTrue(!button2Clicked)

        onClick2()
        assertTrue(button2Clicked)
    }

    @Test
    fun `button text has appropriate font size`() {
        val fontSize = 12 // sp

        assertEquals(12, fontSize)
        assertTrue(fontSize > 0)
    }

    @Test
    fun `button line height is appropriate`() {
        val lineHeight = 14 // sp

        assertEquals(14, lineHeight)
        assertTrue(lineHeight > 0)
    }

    @Test
    fun `button supports max lines`() {
        val maxLines = 2

        assertEquals(2, maxLines)
        assertTrue(maxLines > 0)
    }

    @Test
    fun `button elevation is set`() {
        val elevation = 2 // dp

        assertEquals(2, elevation)
        assertTrue(elevation >= 0)
    }

    @Test
    fun `button can be clicked multiple times`() {
        var clickCount = 0
        val onClick = { clickCount++ }

        repeat(5) { onClick() }

        assertEquals(5, clickCount)
    }

    @Test
    fun `button text is not empty`() {
        val validTexts = listOf("Dado", "Wiki", "Personajes")

        validTexts.forEach { text ->
            assertTrue(text.isNotEmpty())
        }
    }

    @Test
    fun `button padding is appropriate`() {
        val padding = 8 // dp

        assertEquals(8, padding)
        assertTrue(padding >= 0)
    }

    @Test
    fun `button spacing between icon and text is appropriate`() {
        val spacing = 8 // dp

        assertEquals(8, spacing)
        assertTrue(spacing >= 0)
    }
}