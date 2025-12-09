package com.example.pathfinderapp.ui.utils

import com.example.pathfinderapp.data.models.CharacterClass
import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.data.models.CharacterStats
import com.example.pathfinderapp.data.models.Race
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CharacterExportUtilsTest {

    // Test data
    private lateinit var testRace: Race
    private lateinit var testClass: CharacterClass
    private lateinit var testStats: CharacterStats
    private lateinit var testCharacter: CharacterProfile

    @Before
    fun setUp() {
        testRace = Race(
            name = "Elfo",
            description = "Seres mágicos de larga vida",
            bonuses = mapOf("Destreza" to 2, "Inteligencia" to 1),
            specialTraits = "Visión en la oscuridad, Sentidos agudos"
        )

        testClass = CharacterClass(
            name = "Mago",
            description = "Maestro de las artes arcanas",
            hitDie = "d6",
            primaryStats = "Inteligencia"
        )

        testStats = CharacterStats(
            strength = 10,
            dexterity = 14,
            constitution = 12,
            intelligence = 16,
            wisdom = 13,
            charisma = 8
        )

        testCharacter = CharacterProfile(
            id = "test123",
            name = "Gandalf el Gris",
            race = testRace,
            characterClass = testClass,
            stats = testStats,
            level = 5
        )
    }


    @Test
    fun `toJson genera JSON valido`() {
        val result = CharacterExportUtils.toJson(testCharacter)

        assertNotNull(result)
        assertFalse(result.isEmpty())
        assertTrue(result.startsWith("{"))
        assertTrue(result.endsWith("}"))
    }

    @Test
    fun `toJson contiene el id del personaje`() {
        val result = CharacterExportUtils.toJson(testCharacter)
        val json = JSONObject(result)

        assertTrue(json.has("id"))
        assertEquals("test123", json.getString("id"))
    }

    @Test
    fun `toJson contiene el nombre del personaje`() {
        val result = CharacterExportUtils.toJson(testCharacter)
        val json = JSONObject(result)

        assertTrue(json.has("name"))
        assertEquals("Gandalf el Gris", json.getString("name"))
    }

    @Test
    fun `toJson contiene los datos de raza completos`() {
        val result = CharacterExportUtils.toJson(testCharacter)
        val json = JSONObject(result)

        assertTrue(json.has("race"))
        val race = json.getJSONObject("race")
        assertEquals("Elfo", race.getString("name"))
        assertEquals("Seres mágicos de larga vida", race.getString("description"))
        assertEquals("Visión en la oscuridad, Sentidos agudos", race.getString("specialTraits"))
    }

    @Test
    fun `toJson contiene los bonuses raciales`() {
        val result = CharacterExportUtils.toJson(testCharacter)
        val json = JSONObject(result)
        val race = json.getJSONObject("race")
        val bonuses = race.getJSONObject("bonuses")

        assertEquals(2, bonuses.getInt("Destreza"))
        assertEquals(1, bonuses.getInt("Inteligencia"))
    }

    @Test
    fun `toJson contiene los datos de clase completos`() {
        val result = CharacterExportUtils.toJson(testCharacter)
        val json = JSONObject(result)

        assertTrue(json.has("characterClass"))
        val charClass = json.getJSONObject("characterClass")
        assertEquals("Mago", charClass.getString("name"))
        assertEquals("Maestro de las artes arcanas", charClass.getString("description"))
        assertEquals("d6", charClass.getString("hitDie"))
        assertEquals("Inteligencia", charClass.getString("primaryStats"))
    }

    @Test
    fun `toJson contiene todas las estadisticas`() {
        val result = CharacterExportUtils.toJson(testCharacter)
        val json = JSONObject(result)
        val stats = json.getJSONObject("stats")

        assertEquals(10, stats.getInt("strength"))
        assertEquals(14, stats.getInt("dexterity"))
        assertEquals(12, stats.getInt("constitution"))
        assertEquals(16, stats.getInt("intelligence"))
        assertEquals(13, stats.getInt("wisdom"))
        assertEquals(8, stats.getInt("charisma"))
    }

    @Test
    fun `toJson con personaje con nombre vacio`() {
        val character = testCharacter.copy(name = "")
        val result = CharacterExportUtils.toJson(character)
        val json = JSONObject(result)

        assertEquals("", json.getString("name"))
    }

    @Test
    fun `toJson con personaje con bonuses vacios`() {
        val raceWithoutBonuses = testRace.copy(bonuses = emptyMap())
        val character = testCharacter.copy(race = raceWithoutBonuses)

        val result = CharacterExportUtils.toJson(character)
        val json = JSONObject(result)
        val bonuses = json.getJSONObject("race").getJSONObject("bonuses")

        assertEquals(0, bonuses.length())
    }


    @Test
    fun `toJsonList genera JSON array valido`() {
        val characters = listOf(testCharacter)
        val result = CharacterExportUtils.toJsonList(characters)

        assertNotNull(result)
        assertTrue(result.startsWith("["))
        assertTrue(result.endsWith("]"))
    }

    @Test
    fun `toJsonList con lista vacia genera array vacio`() {
        val result = CharacterExportUtils.toJsonList(emptyList())
        val jsonArray = JSONArray(result)

        assertEquals(0, jsonArray.length())
    }

    @Test
    fun `toJsonList con un personaje genera array de tamaño 1`() {
        val characters = listOf(testCharacter)
        val result = CharacterExportUtils.toJsonList(characters)
        val jsonArray = JSONArray(result)

        assertEquals(1, jsonArray.length())
    }

    @Test
    fun `toJsonList con multiples personajes`() {
        val character2 = testCharacter.copy(id = "test456", name = "Saruman")
        val character3 = testCharacter.copy(id = "test789", name = "Radagast")
        val characters = listOf(testCharacter, character2, character3)

        val result = CharacterExportUtils.toJsonList(characters)
        val jsonArray = JSONArray(result)

        assertEquals(3, jsonArray.length())
    }

    @Test
    fun `toJsonList mantiene el orden de los personajes`() {
        val character2 = testCharacter.copy(id = "test456", name = "Saruman")
        val characters = listOf(testCharacter, character2)

        val result = CharacterExportUtils.toJsonList(characters)
        val jsonArray = JSONArray(result)

        assertEquals("Gandalf el Gris", jsonArray.getJSONObject(0).getString("name"))
        assertEquals("Saruman", jsonArray.getJSONObject(1).getString("name"))
    }

    @Test
    fun `toJsonList cada elemento tiene estructura completa`() {
        val characters = listOf(testCharacter)
        val result = CharacterExportUtils.toJsonList(characters)
        val jsonArray = JSONArray(result)
        val firstChar = jsonArray.getJSONObject(0)

        assertTrue(firstChar.has("id"))
        assertTrue(firstChar.has("name"))
        assertTrue(firstChar.has("race"))
        assertTrue(firstChar.has("characterClass"))
        assertTrue(firstChar.has("stats"))
    }


    @Test
    fun `toJson con caracteres especiales en nombre`() {
        val character = testCharacter.copy(name = "Gandalf \"El Gris\" <Mago>")
        val result = CharacterExportUtils.toJson(character)
        val json = JSONObject(result)

        assertEquals("Gandalf \"El Gris\" <Mago>", json.getString("name"))
    }

    @Test
    fun `toJson con estadisticas en limites extremos`() {
        val extremeStats = CharacterStats(
            strength = 3,
            dexterity = 20,
            constitution = 1,
            intelligence = 18,
            wisdom = 3,
            charisma = 20
        )
        val character = testCharacter.copy(stats = extremeStats)
        val result = CharacterExportUtils.toJson(character)
        val json = JSONObject(result)
        val stats = json.getJSONObject("stats")

        assertEquals(3, stats.getInt("strength"))
        assertEquals(20, stats.getInt("dexterity"))
        assertEquals(1, stats.getInt("constitution"))
    }


    @Test
    fun `toJson genera JSON formateado con indentacion`() {
        val result = CharacterExportUtils.toJson(testCharacter)

        assertTrue(result.contains("\n"))
    }

    @Test
    fun `toJsonList genera JSON formateado con indentacion`() {
        val characters = listOf(testCharacter)
        val result = CharacterExportUtils.toJsonList(characters)

        assertTrue(result.contains("\n"))
    }


    @Test
    fun `toJson preserva todos los campos sin perdida de datos`() {
        val result = CharacterExportUtils.toJson(testCharacter)
        val json = JSONObject(result)

        // Verificar que todos los campos principales existen
        assertTrue(json.has("id"))
        assertTrue(json.has("name"))
        assertTrue(json.has("race"))
        assertTrue(json.has("characterClass"))
        assertTrue(json.has("stats"))
    }

    @Test
    fun `toJson y parse mantienen integridad de datos`() {
        val originalJson = CharacterExportUtils.toJson(testCharacter)
        val parsedJson = JSONObject(originalJson)

        assertEquals(testCharacter.id, parsedJson.getString("id"))
        assertEquals(testCharacter.name, parsedJson.getString("name"))

        val stats = parsedJson.getJSONObject("stats")
        assertEquals(testCharacter.stats.strength, stats.getInt("strength"))
        assertEquals(testCharacter.stats.dexterity, stats.getInt("dexterity"))
    }

    @Test
    fun `toJsonList todos los elementos tienen el mismo formato`() {
        val character2 = testCharacter.copy(id = "test456", name = "Saruman")
        val characters = listOf(testCharacter, character2)

        val result = CharacterExportUtils.toJsonList(characters)
        val jsonArray = JSONArray(result)

        val char1 = jsonArray.getJSONObject(0)
        val char2 = jsonArray.getJSONObject(1)

        assertEquals(char1.length(), char2.length())
        assertTrue(char1.has("id") && char2.has("id"))
        assertTrue(char1.has("name") && char2.has("name"))
        assertTrue(char1.has("race") && char2.has("race"))
    }
}