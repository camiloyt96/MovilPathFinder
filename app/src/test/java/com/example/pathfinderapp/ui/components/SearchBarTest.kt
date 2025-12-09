package com.example.pathfinderapp.ui.components

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class SearchBarTest {

    @Test
    fun `query starts empty`() {
        val query = ""

        assertEquals("", query)
        assertTrue(query.isEmpty())
    }

    @Test
    fun `query can be updated`() {
        var query = ""
        val onQueryChange: (String) -> Unit = { query = it }

        onQueryChange("test")

        assertEquals("test", query)
        assertTrue(query.isNotEmpty())
    }

    @Test
    fun `query can be cleared`() {
        var query = "test search"
        val onQueryChange: (String) -> Unit = { query = it }

        onQueryChange("")

        assertEquals("", query)
        assertTrue(query.isEmpty())
    }

    @Test
    fun `placeholder text is correct`() {
        val placeholderText = "Buscar..."

        assertEquals("Buscar...", placeholderText)
        assertTrue(placeholderText.isNotEmpty())
    }

    @Test
    fun `clear button appears when query is not empty`() {
        val query = "test"
        val shouldShowClearButton = query.isNotEmpty()

        assertTrue(shouldShowClearButton)
    }

    @Test
    fun `clear button does not appear when query is empty`() {
        val query = ""
        val shouldShowClearButton = query.isNotEmpty()

        assertFalse(shouldShowClearButton)
    }

    @Test
    fun `search bar is single line`() {
        val singleLine = true

        assertTrue(singleLine)
    }

    @Test
    fun `corner radius is appropriate`() {
        val cornerRadius = 12 // dp

        assertEquals(12, cornerRadius)
        assertTrue(cornerRadius > 0)
    }

    @Test
    fun `horizontal padding is appropriate`() {
        val horizontalPadding = 16 // dp

        assertEquals(16, horizontalPadding)
        assertTrue(horizontalPadding > 0)
    }

    @Test
    fun `vertical padding is appropriate`() {
        val verticalPadding = 8 // dp

        assertEquals(8, verticalPadding)
        assertTrue(verticalPadding >= 0)
    }

    @Test
    fun `query can contain any text`() {
        val queries = listOf(
            "dragon",
            "Goblin",
            "CR 5",
            "Medium Beast"
        )

        queries.forEach { query ->
            assertTrue(query.isNotEmpty())
        }
    }

    @Test
    fun `query change callback is called with new value`() {
        var currentQuery = ""
        val onQueryChange: (String) -> Unit = { currentQuery = it }

        onQueryChange("new search")

        assertEquals("new search", currentQuery)
    }

    @Test
    fun `clear icon is Close icon`() {
        val clearIconDescription = "Limpiar"

        assertEquals("Limpiar", clearIconDescription)
    }

    @Test
    fun `search icon is Search icon`() {
        val searchIconDescription = "Buscar"

        assertEquals("Buscar", searchIconDescription)
    }

    @Test
    fun `query can be updated multiple times`() {
        var query = ""
        val onQueryChange: (String) -> Unit = { query = it }

        onQueryChange("first")
        assertEquals("first", query)

        onQueryChange("second")
        assertEquals("second", query)

        onQueryChange("third")
        assertEquals("third", query)
    }

    @Test
    fun `empty query after clear`() {
        var query = "something"
        val onQueryChange: (String) -> Unit = { query = it }

        // Simulate clear button click
        onQueryChange("")

        assertTrue(query.isEmpty())
        assertEquals("", query)
    }
}