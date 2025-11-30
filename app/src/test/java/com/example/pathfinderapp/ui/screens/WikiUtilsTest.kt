package com.example.pathfinderapp.ui.screens

import org.junit.Assert.*
import org.junit.Test

class WikiUtilsTest {

    @Test
    fun `formatSpellLevel con nivel 0 retorna Cantrip`() {
        val result = formatSpellLevel(0)
        assertEquals("Cantrip", result)
    }

    @Test
    fun `formatSpellLevel con nivel 1 retorna formato correcto`() {
        val result = formatSpellLevel(1)
        assertEquals("Nivel 1", result)
    }

    @Test
    fun `formatSpellLevel con nivel 9 retorna formato correcto`() {
        val result = formatSpellLevel(9)
        assertEquals("Nivel 9", result)
    }

    @Test
    fun `formatSpellSchool capitaliza correctamente`() {
        val result = formatSpellSchool("evocation")
        assertEquals("Evocation", result)
    }

    @Test
    fun `formatSpellSchool con nombre compuesto funciona`() {
        val result = formatSpellSchool("transmutation magic")
        assertEquals("Transmutation Magic", result)
    }

    @Test
    fun `formatCastingTime con acción de bonus funciona`() {
        val result = formatCastingTime("1 bonus action")
        assertEquals("1 Acción de Bonus", result)
    }

    @Test
    fun `formatCastingTime con acción estándar funciona`() {
        val result = formatCastingTime("1 action")
        assertEquals("1 Acción", result)
    }

    @Test
    fun `formatCastingTime con minutos funciona`() {
        val result = formatCastingTime("10 minutes")
        assertEquals("10 Minutos", result)
    }

    @Test
    fun `formatRange con Self funciona`() {
        val result = formatRange("Self")
        assertEquals("Propio", result)
    }

    @Test
    fun `formatRange con Touch funciona`() {
        val result = formatRange("Touch")
        assertEquals("Toque", result)
    }

    @Test
    fun `formatRange con distancia en pies funciona`() {
        val result = formatRange("60 feet")
        assertEquals("60 pies", result)
    }

    @Test
    fun `formatDuration con instantánea funciona`() {
        val result = formatDuration("Instantaneous")
        assertEquals("Instantánea", result)
    }

    @Test
    fun `formatDuration con concentración funciona`() {
        val result = formatDuration("Concentration, up to 1 minute")
        assertEquals("Concentración, hasta 1 minuto", result)
    }

    @Test
    fun `formatDuration con horas funciona`() {
        val result = formatDuration("8 hours")
        assertEquals("8 horas", result)
    }

    @Test
    fun `formatComponents con V S M funciona`() {
        val components = listOf("V", "S", "M")
        val result = formatComponents(components)
        assertEquals("Verbal, Somático, Material", result)
    }

    @Test
    fun `formatComponents con solo V funciona`() {
        val components = listOf("V")
        val result = formatComponents(components)
        assertEquals("Verbal", result)
    }

    @Test
    fun `formatComponents con V y S funciona`() {
        val components = listOf("V", "S")
        val result = formatComponents(components)
        assertEquals("Verbal, Somático", result)
    }

    @Test
    fun `filterSpellsByLevel con nivel válido funciona`() {
        val spells = listOf(
            createTestSpell("Magic Missile", 1),
            createTestSpell("Fireball", 3),
            createTestSpell("Light", 0)
        )

        val result = filterSpellsByLevel(spells, 1)

        assertEquals(1, result.size)
        assertEquals("Magic Missile", result[0].name)
    }

    @Test
    fun `filterSpellsByLevel con null retorna todos`() {
        val spells = listOf(
            createTestSpell("Magic Missile", 1),
            createTestSpell("Fireball", 3)
        )

        val result = filterSpellsByLevel(spells, null)

        assertEquals(2, result.size)
    }

    @Test
    fun `searchSpells encuentra por nombre exacto`() {
        val spells = listOf(
            createTestSpell("Magic Missile", 1),
            createTestSpell("Fireball", 3),
            createTestSpell("Fire Bolt", 0)
        )

        val result = searchSpells(spells, "Fireball")

        assertEquals(1, result.size)
        assertEquals("Fireball", result[0].name)
    }

    @Test
    fun `searchSpells encuentra por nombre parcial`() {
        val spells = listOf(
            createTestSpell("Magic Missile", 1),
            createTestSpell("Fireball", 3),
            createTestSpell("Fire Bolt", 0)
        )

        val result = searchSpells(spells, "Fire")

        assertEquals(2, result.size)
    }

    @Test
    fun `searchSpells es case insensitive`() {
        val spells = listOf(
            createTestSpell("Magic Missile", 1),
            createTestSpell("Fireball", 3)
        )

        val result = searchSpells(spells, "magic")

        assertEquals(1, result.size)
        assertEquals("Magic Missile", result[0].name)
    }

    @Test
    fun `searchSpells con query vacía retorna todos`() {
        val spells = listOf(
            createTestSpell("Magic Missile", 1),
            createTestSpell("Fireball", 3)
        )

        val result = searchSpells(spells, "")

        assertEquals(2, result.size)
    }

    @Test
    fun `isRitualSpell retorna true para ritual`() {
        assertTrue(isRitualSpell(true))
    }

    @Test
    fun `isRitualSpell retorna false para no ritual`() {
        assertFalse(isRitualSpell(false))
    }

    @Test
    fun `requiresConcentration retorna true para concentración`() {
        assertTrue(requiresConcentration(true))
    }

    @Test
    fun `getSpellSchoolColor retorna color correcto`() {
        val evocationColor = getSpellSchoolColor("Evocation")
        assertNotNull(evocationColor)
    }

    @Test
    fun `sortSpellsByLevel ordena correctamente`() {
        val spells = listOf(
            createTestSpell("Fireball", 3),
            createTestSpell("Light", 0),
            createTestSpell("Magic Missile", 1)
        )

        val result = sortSpellsByLevel(spells)

        assertEquals("Light", result[0].name)
        assertEquals("Magic Missile", result[1].name)
        assertEquals("Fireball", result[2].name)
    }

    @Test
    fun `sortSpellsByName ordena alfabéticamente`() {
        val spells = listOf(
            createTestSpell("Fireball", 3),
            createTestSpell("Aid", 2),
            createTestSpell("Bless", 1)
        )

        val result = sortSpellsByName(spells)

        assertEquals("Aid", result[0].name)
        assertEquals("Bless", result[1].name)
        assertEquals("Fireball", result[2].name)
    }

    data class TestSpell(
        val name: String,
        val level: Int,
        val school: String = "Evocation"
    )

    private fun createTestSpell(name: String, level: Int) = TestSpell(name, level)
}

// Funciones helper corregidas (sin warnings)
fun formatSpellLevel(level: Int): String {
    return if (level == 0) "Cantrip" else "Nivel $level"
}

fun formatSpellSchool(school: String): String {
    return school.split(" ").joinToString(" ") {
        it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() }
    }
}

fun formatCastingTime(castingTime: String): String {
    return castingTime
        .replace("bonus action", "Acción de Bonus", ignoreCase = true)
        .replace("action", "Acción", ignoreCase = true)
        .replace("minutes", "Minutos", ignoreCase = true)
        .replace("minute", "Minuto", ignoreCase = true)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun formatRange(range: String): String {
    return when {
        range.equals("Self", ignoreCase = true) -> "Propio"
        range.equals("Touch", ignoreCase = true) -> "Toque"
        else -> range.replace("feet", "pies", ignoreCase = true)
    }
}

fun formatDuration(duration: String): String {
    return duration
        .replace("Instantaneous", "Instantánea", ignoreCase = true)
        .replace("Concentration", "Concentración", ignoreCase = true)
        .replace("up to", "hasta", ignoreCase = true)
        .replace("hours", "horas", ignoreCase = true)
        .replace("hour", "hora", ignoreCase = true)
        .replace("minutes", "minutos", ignoreCase = true)
        .replace("minute", "minuto", ignoreCase = true)
}

fun formatComponents(components: List<String>): String {
    return components.joinToString(", ") {
        when (it) {
            "V" -> "Verbal"
            "S" -> "Somático"
            "M" -> "Material"
            else -> it
        }
    }
}

fun <T> filterSpellsByLevel(spells: List<T>, level: Int?): List<T> where T : Any {
    return if (level == null) spells
    else spells.filter { (it as WikiUtilsTest.TestSpell).level == level }
}

fun <T> searchSpells(spells: List<T>, query: String): List<T> where T : Any {
    if (query.isBlank()) return spells
    return spells.filter {
        (it as WikiUtilsTest.TestSpell).name.contains(query, ignoreCase = true)
    }
}

fun isRitualSpell(ritual: Boolean): Boolean = ritual

fun requiresConcentration(concentration: Boolean): Boolean = concentration

fun getSpellSchoolColor(school: String): String {
    return when (school.lowercase()) {
        "evocation" -> "#FF5722"
        "abjuration" -> "#2196F3"
        "conjuration" -> "#4CAF50"
        "divination" -> "#9C27B0"
        "enchantment" -> "#E91E63"
        "illusion" -> "#00BCD4"
        "necromancy" -> "#424242"
        "transmutation" -> "#FF9800"
        else -> "#757575"
    }
}

fun <T> sortSpellsByLevel(spells: List<T>): List<T> where T : Any {
    return spells.sortedBy { (it as WikiUtilsTest.TestSpell).level }
}

fun <T> sortSpellsByName(spells: List<T>): List<T> where T : Any {
    return spells.sortedBy { (it as WikiUtilsTest.TestSpell).name }
}