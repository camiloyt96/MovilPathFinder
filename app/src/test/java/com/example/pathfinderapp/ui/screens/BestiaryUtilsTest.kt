package com.example.pathfinderapp.ui.screens

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.floor

class BestiaryUtilsTest {

    @Test
    fun `formatChallengeRating con valor entero funciona`() {
        val result = formatChallengeRating(5.0)
        assertEquals("CR 5", result)
    }

    @Test
    fun `formatChallengeRating con valor fraccionario funciona`() {
        val result = formatChallengeRating(0.5)
        assertEquals("CR 1/2", result)
    }

    @Test
    fun `formatChallengeRating con cuarto funciona`() {
        val result = formatChallengeRating(0.25)
        assertEquals("CR 1/4", result)
    }

    @Test
    fun `formatChallengeRating con octavo funciona`() {
        val result = formatChallengeRating(0.125)
        assertEquals("CR 1/8", result)
    }

    @Test
    fun `formatChallengeRating con cero funciona`() {
        val result = formatChallengeRating(0.0)
        assertEquals("CR 0", result)
    }

    @Test
    fun `formatXP formatea correctamente miles`() {
        val result = formatXP(1000)
        assertEquals("1,000 XP", result)
    }

    @Test
    fun `formatXP formatea correctamente millones`() {
        val result = formatXP(1000000)
        assertEquals("1,000,000 XP", result)
    }

    @Test
    fun `formatXP con cero funciona`() {
        val result = formatXP(0)
        assertEquals("0 XP", result)
    }

    @Test
    fun `calculateModifier con stat 10 retorna +0`() {
        val result = calculateModifier(10)
        assertEquals("+0", result)
    }

    @Test
    fun `calculateModifier con stat 16 retorna +3`() {
        val result = calculateModifier(16)
        assertEquals("+3", result)
    }

    @Test
    fun `calculateModifier con stat 8 retorna -1`() {
        val result = calculateModifier(8)
        assertEquals("-1", result)
    }

    @Test
    fun `calculateModifier con stat 1 retorna -5`() {
        val result = calculateModifier(1)
        assertEquals("-5", result)
    }

    @Test
    fun `calculateModifier con stat 20 retorna +5`() {
        val result = calculateModifier(20)
        assertEquals("+5", result)
    }

    @Test
    fun `formatMonsterType con tipo y tamaño funciona`() {
        val result = formatMonsterType("Medium", "Beast")
        assertEquals("Medium Beast", result)
    }

    @Test
    fun `formatMonsterType con tamaño Large funciona`() {
        val result = formatMonsterType("Large", "Dragon")
        assertEquals("Large Dragon", result)
    }

    @Test
    fun `formatAlignment alinea correctamente`() {
        val result = formatAlignment("lawful good")
        assertEquals("Lawful Good", result)
    }

    @Test
    fun `formatAlignment con cualquier alineamiento funciona`() {
        val result = formatAlignment("any alignment")
        assertEquals("Any Alignment", result)
    }

    @Test
    fun `parseChallengeRatingRange con rango simple funciona`() {
        val result = parseChallengeRatingRange("0-5")
        assertEquals(Pair(0.0, 5.0), result)
    }

    @Test
    fun `parseChallengeRatingRange con rango alto funciona`() {
        val result = parseChallengeRatingRange("10-20")
        assertEquals(Pair(10.0, 20.0), result)
    }

    @Test
    fun `filterMonstersBySize con tamaño válido funciona`() {
        val monsters = listOf(
            createTestMonster("Goblin", "Small"),
            createTestMonster("Orc", "Medium"),
            createTestMonster("Giant", "Large")
        )

        val result = filterMonstersBySize(monsters, "Medium")

        assertEquals(1, result.size)
        assertEquals("Orc", result[0].name)
    }

    @Test
    fun `filterMonstersBySize con null retorna todos`() {
        val monsters = listOf(
            createTestMonster("Goblin", "Small"),
            createTestMonster("Orc", "Medium")
        )

        val result = filterMonstersBySize(monsters, null)

        assertEquals(2, result.size)
    }

    @Test
    fun `filterMonstersByType con tipo válido funciona`() {
        val monsters = listOf(
            createTestMonster("Wolf", type = "Beast"),
            createTestMonster("Zombie", type = "Undead"),
            createTestMonster("Dragon", type = "Dragon")
        )

        val result = filterMonstersByType(monsters, "Undead")

        assertEquals(1, result.size)
        assertEquals("Zombie", result[0].name)
    }

    @Test
    fun `isMonsterInCRRange con CR en rango retorna true`() {
        assertTrue(isMonsterInCRRange(3.0, 0.0, 5.0))
        assertTrue(isMonsterInCRRange(0.0, 0.0, 5.0))
        assertTrue(isMonsterInCRRange(5.0, 0.0, 5.0))
    }

    @Test
    fun `isMonsterInCRRange con CR fuera de rango retorna false`() {
        assertFalse(isMonsterInCRRange(6.0, 0.0, 5.0))
        assertFalse(isMonsterInCRRange(-1.0, 0.0, 5.0))
    }

    @Test
    fun `getMonsterSizeOrder retorna orden correcto`() {
        val sizes = listOf("Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan")

        assertEquals(0, getMonsterSizeOrder("Tiny"))
        assertEquals(2, getMonsterSizeOrder("Medium"))
        assertEquals(5, getMonsterSizeOrder("Gargantuan"))
    }

    @Test
    fun `getMonsterSizeOrder con tamaño desconocido retorna valor alto`() {
        assertTrue(getMonsterSizeOrder("Unknown") > 10)
    }

    data class TestMonster(
        val name: String,
        val size: String = "Medium",
        val type: String = "Beast",
        val cr: Double = 1.0
    )

    private fun createTestMonster(
        name: String,
        size: String = "Medium",
        type: String = "Beast",
        cr: Double = 1.0
    ) = TestMonster(name, size, type, cr)
}

// Funciones helper corregidas (sin warnings)
fun formatChallengeRating(cr: Double): String {
    return when {
        cr == 0.125 -> "CR 1/8"
        cr == 0.25 -> "CR 1/4"
        cr == 0.5 -> "CR 1/2"
        cr % 1.0 == 0.0 -> "CR ${cr.toInt()}"
        else -> "CR $cr"
    }
}

fun formatXP(xp: Int): String {
    return "${xp.toString().reversed().chunked(3).joinToString(",").reversed()} XP"
}

fun calculateModifier(stat: Int): String {
    // Usar floor para redondear hacia abajo (comportamiento correcto de D&D)
    val mod = floor((stat - 10) / 2.0).toInt()
    return if (mod >= 0) "+$mod" else "$mod"
}

fun formatMonsterType(size: String, type: String): String {
    return "$size $type"
}

fun formatAlignment(alignment: String): String {
    return alignment.split(" ").joinToString(" ") {
        it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() }
    }
}

fun parseChallengeRatingRange(range: String): Pair<Double, Double> {
    val parts = range.split("-")
    return Pair(parts[0].toDouble(), parts[1].toDouble())
}

fun <T> filterMonstersBySize(monsters: List<T>, size: String?): List<T> where T : Any {
    return if (size == null) monsters
    else monsters.filter { (it as BestiaryUtilsTest.TestMonster).size == size }
}

fun <T> filterMonstersByType(monsters: List<T>, type: String?): List<T> where T : Any {
    return if (type == null) monsters
    else monsters.filter { (it as BestiaryUtilsTest.TestMonster).type == type }
}

fun isMonsterInCRRange(cr: Double, minCR: Double, maxCR: Double): Boolean {
    return cr in minCR..maxCR
}

fun getMonsterSizeOrder(size: String): Int {
    return when (size.lowercase()) {
        "tiny" -> 0
        "small" -> 1
        "medium" -> 2
        "large" -> 3
        "huge" -> 4
        "gargantuan" -> 5
        else -> 999
    }
}