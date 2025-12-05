package com.example.pathfinderapp.ui.screens

import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.data.models.CharacterStats
import com.example.pathfinderapp.data.models.Race
import com.example.pathfinderapp.data.models.CharacterClass
import org.junit.Test
import org.junit.Assert.*

class CharactersListScreenTest {

    // ==================== TESTS DE LÍMITE DE PERSONAJES ====================

    @Test
    fun `canCreateNewCharacter retorna true con menos de 5 personajes`() {
        assertTrue(canCreateNewCharacter(0))
        assertTrue(canCreateNewCharacter(1))
        assertTrue(canCreateNewCharacter(4))
    }

    @Test
    fun `canCreateNewCharacter retorna false con 5 personajes`() {
        assertFalse(canCreateNewCharacter(5))
    }

    @Test
    fun `canCreateNewCharacter retorna false con más de 5 personajes`() {
        assertFalse(canCreateNewCharacter(6))
        assertFalse(canCreateNewCharacter(10))
    }

    @Test
    fun `getCharacterLimitMessage con menos de 5 personajes retorna null`() {
        assertNull(getCharacterLimitMessage(3))
    }

    @Test
    fun `getCharacterLimitMessage con 5 personajes retorna mensaje de límite`() {
        val message = getCharacterLimitMessage(5)

        assertNotNull(message)
        assertTrue(message!!.contains("límite"))
    }

    @Test
    fun `getRemainingSlots calcula correctamente espacios disponibles`() {
        assertEquals(5, getRemainingSlots(0))
        assertEquals(3, getRemainingSlots(2))
        assertEquals(0, getRemainingSlots(5))
    }

    // ==================== TESTS DE VALIDACIÓN DE LISTA ====================

    @Test
    fun `isCharacterListEmpty retorna true con lista vacía`() {
        val characters = emptyList<CharacterProfile>()

        assertTrue(isCharacterListEmpty(characters))
    }

    @Test
    fun `isCharacterListEmpty retorna false con personajes`() {
        val characters = listOf(createTestCharacter("Valeros"))

        assertFalse(isCharacterListEmpty(characters))
    }

    @Test
    fun `hasCharacters retorna true con al menos un personaje`() {
        val characters = listOf(createTestCharacter("Seoni"))

        assertTrue(hasCharacters(characters))
    }

    @Test
    fun `getCharacterCount retorna cantidad correcta`() {
        val characters = listOf(
            createTestCharacter("Valeros"),
            createTestCharacter("Seoni"),
            createTestCharacter("Kyra")
        )

        assertEquals(3, getCharacterCount(characters))
    }

    // ==================== TESTS DE FORMATO DE TEXTO ====================

    @Test
    fun `formatCharacterCountText retorna formato correcto`() {
        assertEquals("Mis personajes (0/5)", formatCharacterCountText(0))
        assertEquals("Mis personajes (3/5)", formatCharacterCountText(3))
        assertEquals("Mis personajes (5/5)", formatCharacterCountText(5))
    }

    @Test
    fun `getEmptyStateMessage retorna mensaje apropiado`() {
        val message = getEmptyStateMessage()

        assertEquals("Aún no tienes personajes", message.title)
        assertEquals("¡Crea tu primer aventurero!", message.subtitle)
    }

    @Test
    fun `formatCharacterStats retorna string formateado correctamente`() {
        val stats = CharacterStats(
            strength = 16,
            dexterity = 14,
            constitution = 15,
            intelligence = 12,
            wisdom = 10,
            charisma = 8
        )

        val formatted = formatCharacterStats(stats)

        assertTrue(formatted.contains("FUE 16"))
        assertTrue(formatted.contains("DES 14"))
        assertTrue(formatted.contains("CON 15"))
        assertTrue(formatted.contains("INT 12"))
        assertTrue(formatted.contains("SAB 10"))
        assertTrue(formatted.contains("CAR 8"))
    }

    @Test
    fun `formatRacialBonuses retorna string formateado correctamente`() {
        val bonuses = mapOf(
            "Fuerza" to 2,
            "Destreza" to -2,
            "Constitución" to 0
        )

        val formatted = formatRacialBonuses(bonuses)

        assertTrue(formatted.contains("Fuerza +2"))
        assertTrue(formatted.contains("Destreza -2"))
        assertTrue(formatted.contains("Constitución +0"))
    }

    // ==================== TESTS DE BÚSQUEDA Y FILTRADO ====================

    @Test
    fun `findCharacterById encuentra personaje existente`() {
        val characters = listOf(
            createTestCharacter("Valeros"),
            createTestCharacter("Seoni")
        )

        val found = findCharacterById(characters, characters[0].id)

        assertNotNull(found)
        assertEquals("Valeros", found?.name)
    }

    @Test
    fun `findCharacterById retorna null con ID inexistente`() {
        val characters = listOf(createTestCharacter("Valeros"))

        val found = findCharacterById(characters, "non-existent-id")

        assertNull(found)
    }

    @Test
    fun `findCharacterByName encuentra personaje por nombre`() {
        val characters = listOf(
            createTestCharacter("Valeros"),
            createTestCharacter("Seoni")
        )

        val found = findCharacterByName(characters, "Seoni")

        assertNotNull(found)
        assertEquals("Seoni", found?.name)
    }

    @Test
    fun `findCharacterByName retorna null con nombre inexistente`() {
        val characters = listOf(createTestCharacter("Valeros"))

        val found = findCharacterByName(characters, "Kyra")

        assertNull(found)
    }

    @Test
    fun `filterCharactersByRace filtra correctamente`() {
        val characters = listOf(
            createTestCharacter("Valeros", raceName = "Humano"),
            createTestCharacter("Seoni", raceName = "Humano"),
            createTestCharacter("Harsk", raceName = "Enano")
        )

        val humans = filterCharactersByRace(characters, "Humano")

        assertEquals(2, humans.size)
        assertTrue(humans.all { it.race.name == "Humano" })
    }

    @Test
    fun `filterCharactersByClass filtra correctamente`() {
        val characters = listOf(
            createTestCharacter("Valeros", className = "Guerrero"),
            createTestCharacter("Seoni", className = "Hechicero"),
            createTestCharacter("Kyra", className = "Clérigo")
        )

        val fighters = filterCharactersByClass(characters, "Guerrero")

        assertEquals(1, fighters.size)
        assertEquals("Guerrero", fighters[0].characterClass.name)
    }

    // ==================== TESTS DE VALIDACIÓN DE EXPORTACIÓN ====================

    @Test
    fun `canExportCharacters retorna true con personajes`() {
        val characters = listOf(createTestCharacter("Valeros"))

        assertTrue(canExportCharacters(characters))
    }

    @Test
    fun `canExportCharacters retorna false sin personajes`() {
        val characters = emptyList<CharacterProfile>()

        assertFalse(canExportCharacters(characters))
    }

    @Test
    fun `getExportFileName genera nombre correcto para personaje`() {
        val character = createTestCharacter("Valeros")

        val fileName = getExportFileName(character)

        assertTrue(fileName.contains("Valeros"))
        assertTrue(fileName.endsWith(".json"))
    }

    @Test
    fun `getExportFileName limpia caracteres especiales`() {
        val character = createTestCharacter("Val@eros#123")

        val fileName = getExportFileName(character)

        assertFalse(fileName.contains("@"))
        assertFalse(fileName.contains("#"))
    }

    @Test
    fun `getExportAllFileName genera nombre correcto para múltiples personajes`() {
        val fileName = getExportAllFileName(3)

        assertTrue(fileName.contains("3"))
        assertTrue(fileName.contains("personajes"))
        assertTrue(fileName.endsWith(".json"))
    }

    // ==================== TESTS DE VALIDACIÓN DE ELIMINACIÓN ====================

    @Test
    fun `canDeleteCharacter siempre retorna true`() {
        val character = createTestCharacter("Valeros")

        assertTrue(canDeleteCharacter(character))
    }

    @Test
    fun `getDeleteConfirmationMessage retorna mensaje apropiado`() {
        val character = createTestCharacter("Valeros")

        val message = getDeleteConfirmationMessage(character)

        assertTrue(message.contains("Valeros"))
        assertTrue(message.contains("eliminar"))
    }

    @Test
    fun `isCharacterDeletable retorna true para cualquier personaje`() {
        val character = createTestCharacter("Seoni")

        assertTrue(isCharacterDeletable(character))
    }

    // ==================== TESTS DE ESTADÍSTICAS ====================

    @Test
    fun `getTotalCharacterStats suma correctamente todas las stats`() {
        val stats = CharacterStats(
            strength = 14,
            dexterity = 12,
            constitution = 16,
            intelligence = 10,
            wisdom = 13,
            charisma = 8
        )

        val total = getTotalCharacterStats(stats)

        assertEquals(73, total)
    }

    @Test
    fun `getAverageStatValue calcula promedio correctamente`() {
        val stats = CharacterStats(
            strength = 12,
            dexterity = 12,
            constitution = 12,
            intelligence = 12,
            wisdom = 12,
            charisma = 12
        )

        val average = getAverageStatValue(stats)

        assertEquals(12.0, average, 0.01)
    }

    @Test
    fun `getHighestStat retorna stat más alta`() {
        val stats = CharacterStats(
            strength = 18,
            dexterity = 14,
            constitution = 12,
            intelligence = 10,
            wisdom = 10,
            charisma = 8
        )

        val highest = getHighestStat(stats)

        assertEquals("Fuerza", highest.name)
        assertEquals(18, highest.value)
    }

    @Test
    fun `getLowestStat retorna stat más baja`() {
        val stats = CharacterStats(
            strength = 14,
            dexterity = 12,
            constitution = 10,
            intelligence = 8,
            wisdom = 10,
            charisma = 16
        )

        val lowest = getLowestStat(stats)

        assertEquals("Inteligencia", lowest.name)
        assertEquals(8, lowest.value)
    }

    // ==================== TESTS DE COMPARTIR ====================

    @Test
    fun `getShareIntentType retorna tipo correcto para JSON`() {
        val type = getShareIntentType("json")

        assertEquals("application/json", type)
    }

    @Test
    fun `getShareIntentType retorna tipo correcto para texto`() {
        val type = getShareIntentType("text")

        assertEquals("text/plain", type)
    }

    @Test
    fun `isValidShareFormat retorna true para formatos válidos`() {
        assertTrue(isValidShareFormat("json"))
        assertTrue(isValidShareFormat("text"))
    }

    @Test
    fun `isValidShareFormat retorna false para formatos inválidos`() {
        assertFalse(isValidShareFormat("xml"))
        assertFalse(isValidShareFormat("pdf"))
        assertFalse(isValidShareFormat(""))
    }

    // ==================== TESTS DE ORDENAMIENTO ====================

    @Test
    fun `sortCharactersByName ordena alfabéticamente`() {
        val characters = listOf(
            createTestCharacter("Zara"),
            createTestCharacter("Aldo"),
            createTestCharacter("Mira")
        )

        val sorted = sortCharactersByName(characters)

        assertEquals("Aldo", sorted[0].name)
        assertEquals("Mira", sorted[1].name)
        assertEquals("Zara", sorted[2].name)
    }

    @Test
    fun `sortCharactersByLevel ordena por nivel descendente`() {
        val characters = listOf(
            createTestCharacter("Valeros", level = 3),
            createTestCharacter("Seoni", level = 7),
            createTestCharacter("Kyra", level = 5)
        )

        val sorted = sortCharactersByLevel(characters)

        assertEquals(7, sorted[0].level)
        assertEquals(5, sorted[1].level)
        assertEquals(3, sorted[2].level)
    }

    @Test
    fun `sortCharactersByRace agrupa por raza`() {
        val characters = listOf(
            createTestCharacter("A", raceName = "Humano"),
            createTestCharacter("B", raceName = "Enano"),
            createTestCharacter("C", raceName = "Humano")
        )

        val sorted = sortCharactersByRace(characters)

        assertEquals("Enano", sorted[0].race.name)
        assertEquals("Humano", sorted[1].race.name)
        assertEquals("Humano", sorted[2].race.name)
    }
}

// ==================== DATA CLASSES ====================

data class EmptyStateMessage(
    val title: String,
    val subtitle: String
)

data class StatInfo(
    val name: String,
    val value: Int
)

// ==================== FUNCIONES HELPER ====================

fun canCreateNewCharacter(currentCount: Int): Boolean {
    return currentCount < 5
}

fun getCharacterLimitMessage(count: Int): String? {
    return if (count >= 5) {
        "Has alcanzado el límite de personajes. Elimina uno para crear otro."
    } else null
}

fun getRemainingSlots(currentCount: Int): Int {
    return (5 - currentCount).coerceAtLeast(0)
}

fun isCharacterListEmpty(characters: List<CharacterProfile>): Boolean {
    return characters.isEmpty()
}

fun hasCharacters(characters: List<CharacterProfile>): Boolean {
    return characters.isNotEmpty()
}

fun getCharacterCount(characters: List<CharacterProfile>): Int {
    return characters.size
}

fun formatCharacterCountText(count: Int): String {
    return "Mis personajes ($count/5)"
}

fun getEmptyStateMessage(): EmptyStateMessage {
    return EmptyStateMessage(
        title = "Aún no tienes personajes",
        subtitle = "¡Crea tu primer aventurero!"
    )
}

fun formatCharacterStats(stats: CharacterStats): String {
    return "FUE ${stats.strength}  | DES ${stats.dexterity}  | CON ${stats.constitution}\n" +
            "INT ${stats.intelligence} | SAB ${stats.wisdom} | CAR ${stats.charisma}"
}

fun formatRacialBonuses(bonuses: Map<String, Int>): String {
    return bonuses.entries.joinToString(", ") { (stat, bonus) ->
        val sign = if (bonus >= 0) "+" else ""
        "$stat $sign$bonus"
    }
}

fun findCharacterById(characters: List<CharacterProfile>, id: String): CharacterProfile? {
    return characters.find { it.id == id }
}

fun findCharacterByName(characters: List<CharacterProfile>, name: String): CharacterProfile? {
    return characters.find { it.name == name }
}

fun filterCharactersByRace(characters: List<CharacterProfile>, raceName: String): List<CharacterProfile> {
    return characters.filter { it.race.name == raceName }
}

fun filterCharactersByClass(characters: List<CharacterProfile>, className: String): List<CharacterProfile> {
    return characters.filter { it.characterClass.name == className }
}

fun canExportCharacters(characters: List<CharacterProfile>): Boolean {
    return characters.isNotEmpty()
}

fun getExportFileName(character: CharacterProfile): String {
    val cleanName = character.name.replace(Regex("[^A-Za-z0-9]"), "_")
    return "${cleanName}_character.json"
}

fun getExportAllFileName(count: Int): String {
    return "mis_${count}_personajes.json"
}

fun canDeleteCharacter(character: CharacterProfile): Boolean {
    return true
}

fun getDeleteConfirmationMessage(character: CharacterProfile): String {
    return "¿Estás seguro de que quieres eliminar a ${character.name}? Esta acción no se puede deshacer."
}

fun isCharacterDeletable(character: CharacterProfile): Boolean {
    return true
}

fun getTotalCharacterStats(stats: CharacterStats): Int {
    return stats.strength + stats.dexterity + stats.constitution +
            stats.intelligence + stats.wisdom + stats.charisma
}

fun getAverageStatValue(stats: CharacterStats): Double {
    return getTotalCharacterStats(stats) / 6.0
}

fun getHighestStat(stats: CharacterStats): StatInfo {
    val statMap = mapOf(
        "Fuerza" to stats.strength,
        "Destreza" to stats.dexterity,
        "Constitución" to stats.constitution,
        "Inteligencia" to stats.intelligence,
        "Sabiduría" to stats.wisdom,
        "Carisma" to stats.charisma
    )

    val highest = statMap.maxByOrNull { it.value }!!
    return StatInfo(highest.key, highest.value)
}

fun getLowestStat(stats: CharacterStats): StatInfo {
    val statMap = mapOf(
        "Fuerza" to stats.strength,
        "Destreza" to stats.dexterity,
        "Constitución" to stats.constitution,
        "Inteligencia" to stats.intelligence,
        "Sabiduría" to stats.wisdom,
        "Carisma" to stats.charisma
    )

    val lowest = statMap.minByOrNull { it.value }!!
    return StatInfo(lowest.key, lowest.value)
}

fun getShareIntentType(format: String): String {
    return when (format.lowercase()) {
        "json" -> "application/json"
        "text" -> "text/plain"
        else -> "text/plain"
    }
}

fun isValidShareFormat(format: String): Boolean {
    return format.lowercase() in listOf("json", "text")
}

fun sortCharactersByName(characters: List<CharacterProfile>): List<CharacterProfile> {
    return characters.sortedBy { it.name }
}

fun sortCharactersByLevel(characters: List<CharacterProfile>): List<CharacterProfile> {
    return characters.sortedByDescending { it.level }
}

fun sortCharactersByRace(characters: List<CharacterProfile>): List<CharacterProfile> {
    return characters.sortedBy { it.race.name }
}

// ==================== TEST HELPER ====================

fun createTestCharacter(
    name: String,
    raceName: String = "Humano",
    className: String = "Guerrero",
    level: Int = 1
): CharacterProfile {
    val race = Race(
        name = raceName,
        description = "Test race",
        bonuses = mapOf("Fuerza" to 2),
        specialTraits = "Test traits"
    )

    val charClass = CharacterClass(
        name = className,
        description = "Test class",
        hitDie = "d10",
        primaryStats = "Fuerza"
    )

    return CharacterProfile(
        name = name,
        race = race,
        characterClass = charClass,
        stats = CharacterStats(),
        level = level
    )
}