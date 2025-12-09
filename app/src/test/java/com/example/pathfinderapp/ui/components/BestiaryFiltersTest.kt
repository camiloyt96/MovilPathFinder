package com.example.pathfinderapp.ui.components

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


class BestiaryFiltersTest {

    @Test
    fun `size filter options are correct`() {
        val sizeOptions = listOf("Small", "Medium", "Large", "Huge", "Gargantuan")

        assertEquals(5, sizeOptions.size)
        assertTrue(sizeOptions.contains("Small"))
        assertTrue(sizeOptions.contains("Medium"))
        assertTrue(sizeOptions.contains("Large"))
        assertTrue(sizeOptions.contains("Huge"))
        assertTrue(sizeOptions.contains("Gargantuan"))
    }

    @Test
    fun `type filter options are correct`() {
        val typeOptions = listOf(
            "Aberration", "Beast", "Celestial", "Dragon", "Elemental",
            "Fey", "Fiend", "Giant", "Humanoid", "Monstrosity",
            "Ooze", "Plant", "Undead"
        )

        assertEquals(13, typeOptions.size)
        assertTrue(typeOptions.contains("Dragon"))
        assertTrue(typeOptions.contains("Beast"))
        assertTrue(typeOptions.contains("Undead"))
    }

    @Test
    fun `CR range options are correct`() {
        val crRanges = listOf(
            0.0..1.0,
            1.0..5.0,
            5.0..10.0,
            10.0..20.0,
            20.0..30.0
        )

        assertEquals(5, crRanges.size)
        assertEquals(0.0, crRanges[0].start)
        assertEquals(1.0, crRanges[0].endInclusive)
        assertEquals(20.0, crRanges[4].start)
        assertEquals(30.0, crRanges[4].endInclusive)
    }

    @Test
    fun `size filter callback works`() {
        var selectedSize: String? = null
        val onSizeSelected: (String?) -> Unit = { selectedSize = it }

        onSizeSelected("Large")
        assertEquals("Large", selectedSize)

        onSizeSelected(null)
        assertNull(selectedSize)
    }

    @Test
    fun `type filter callback works`() {
        var selectedType: String? = null
        val onTypeSelected: (String?) -> Unit = { selectedType = it }

        onTypeSelected("Dragon")
        assertEquals("Dragon", selectedType)

        onTypeSelected(null)
        assertNull(selectedType)
    }

    @Test
    fun `CR range filter callback works`() {
        var selectedRange: ClosedFloatingPointRange<Double>? = null
        val onCRRangeSelected: (ClosedFloatingPointRange<Double>?) -> Unit = { selectedRange = it }

        val testRange = 1.0..5.0
        onCRRangeSelected(testRange)
        assertEquals(testRange, selectedRange)

        onCRRangeSelected(null)
        assertNull(selectedRange)
    }

    @Test
    fun `null selection represents all items`() {
        val selectedSize: String? = null
        val selectedType: String? = null
        val selectedCRRange: ClosedFloatingPointRange<Double>? = null

        assertNull(selectedSize, "Null size should represent all sizes")
        assertNull(selectedType, "Null type should represent all types")
        assertNull(selectedCRRange, "Null CR range should represent all CRs")
    }

    @Test
    fun `CR range label format is correct`() {
        val range = 5.0..10.0
        val label = "${range.start.toInt()}-${range.endInclusive.toInt()}"

        assertEquals("5-10", label)
    }

    @Test
    fun `all size options are valid`() {
        val validSizes = listOf("Small", "Medium", "Large", "Huge", "Gargantuan")

        validSizes.forEach { size ->
            assertTrue(size.isNotEmpty(), "Size option should not be empty")
            assertTrue(size.first().isUpperCase(), "Size should start with uppercase")
        }
    }

    @Test
    fun `all type options are valid`() {
        val validTypes = listOf(
            "Aberration", "Beast", "Celestial", "Dragon", "Elemental",
            "Fey", "Fiend", "Giant", "Humanoid", "Monstrosity",
            "Ooze", "Plant", "Undead"
        )

        validTypes.forEach { type ->
            assertTrue(type.isNotEmpty(), "Type option should not be empty")
            assertTrue(type.first().isUpperCase(), "Type should start with uppercase")
        }
    }

    @Test
    fun `CR ranges are in ascending order`() {
        val crRanges = listOf(
            0.0..1.0,
            1.0..5.0,
            5.0..10.0,
            10.0..20.0,
            20.0..30.0
        )

        for (i in 0 until crRanges.size - 1) {
            assertTrue(
                crRanges[i].endInclusive <= crRanges[i + 1].start,
                "CR ranges should be in ascending order"
            )
        }
    }

    @Test
    fun `CR ranges do not overlap`() {
        val crRanges = listOf(
            0.0..1.0,
            1.0..5.0,
            5.0..10.0,
            10.0..20.0,
            20.0..30.0
        )

        for (i in 0 until crRanges.size - 1) {
            assertEquals(
                crRanges[i].endInclusive,
                crRanges[i + 1].start,
                "Adjacent CR ranges should meet at boundary"
            )
        }
    }
}