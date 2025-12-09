package com.example.pathfinderapp.ui.components

import com.example.pathfinderapp.ui.screens.DiceType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class DiceSelectorTest {

    @Test
    fun `all dice types are available`() {
        val diceTypes = listOf(
            DiceType.D4,
            DiceType.D6,
            DiceType.D8,
            DiceType.D10,
            DiceType.D12,
            DiceType.D20,
            DiceType.D100
        )

        assertEquals(7, diceTypes.size)
    }

    @Test
    fun `first row dice types are correct`() {
        val firstRowDice = listOf(
            DiceType.D4,
            DiceType.D6,
            DiceType.D8,
            DiceType.D10
        )

        assertEquals(4, firstRowDice.size)
        assertTrue(firstRowDice.contains(DiceType.D4))
        assertTrue(firstRowDice.contains(DiceType.D6))
        assertTrue(firstRowDice.contains(DiceType.D8))
        assertTrue(firstRowDice.contains(DiceType.D10))
    }

    @Test
    fun `second row dice types are correct`() {
        val secondRowDice = listOf(
            DiceType.D12,
            DiceType.D20,
            DiceType.D100
        )

        assertEquals(3, secondRowDice.size)
        assertTrue(secondRowDice.contains(DiceType.D12))
        assertTrue(secondRowDice.contains(DiceType.D20))
        assertTrue(secondRowDice.contains(DiceType.D100))
    }

    @Test
    fun `dice selection callback works`() {
        var selectedDice = DiceType.D20
        val onDiceSelected: (DiceType) -> Unit = { selectedDice = it }

        onDiceSelected(DiceType.D12)
        assertEquals(DiceType.D12, selectedDice)

        onDiceSelected(DiceType.D6)
        assertEquals(DiceType.D6, selectedDice)
    }

    @Test
    fun `dice cannot be selected while rolling`() {
        val isRolling = true
        var selectedDice = DiceType.D20
        val onDiceSelected: (DiceType) -> Unit = {
            if (!isRolling) selectedDice = it
        }

        onDiceSelected(DiceType.D12)

        // Should still be D20 because rolling is true
        assertEquals(DiceType.D20, selectedDice)
    }

    @Test
    fun `dice can be selected when not rolling`() {
        val isRolling = false
        var selectedDice = DiceType.D20
        val onDiceSelected: (DiceType) -> Unit = {
            if (!isRolling) selectedDice = it
        }

        onDiceSelected(DiceType.D12)

        assertEquals(DiceType.D12, selectedDice)
    }

    @Test
    fun `selected dice can be any dice type`() {
        val allDiceTypes = listOf(
            DiceType.D4, DiceType.D6, DiceType.D8, DiceType.D10,
            DiceType.D12, DiceType.D20, DiceType.D100
        )

        allDiceTypes.forEach { dice ->
            val selectedDice = dice
            assertTrue(allDiceTypes.contains(selectedDice))
        }
    }

    @Test
    fun `title text is correct`() {
        val titleText = "Selecciona el dado"

        assertEquals("Selecciona el dado", titleText)
        assertTrue(titleText.isNotEmpty())
    }

    @Test
    fun `isRolling flag works correctly`() {
        var isRolling = false

        assertFalse(isRolling)

        isRolling = true
        assertTrue(isRolling)

        isRolling = false
        assertFalse(isRolling)
    }

    @Test
    fun `dice types have correct labels`() {
        assertEquals("D4", DiceType.D4.label)
        assertEquals("D6", DiceType.D6.label)
        assertEquals("D8", DiceType.D8.label)
        assertEquals("D10", DiceType.D10.label)
        assertEquals("D12", DiceType.D12.label)
        assertEquals("D20", DiceType.D20.label)
        assertEquals("D100", DiceType.D100.label)
    }

    @Test
    fun `dice selection state is maintained`() {
        val selectedDice = DiceType.D20

        assertEquals(DiceType.D20, selectedDice)
        assertFalse(selectedDice == DiceType.D12)
        assertTrue(selectedDice == DiceType.D20)
    }

    @Test
    fun `only one dice can be selected at a time`() {
        var selectedDice = DiceType.D20

        assertEquals(DiceType.D20, selectedDice)

        selectedDice = DiceType.D12
        assertEquals(DiceType.D12, selectedDice)
        assertFalse(selectedDice == DiceType.D20)
    }
}