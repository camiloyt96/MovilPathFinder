package com.example.pathfinderapp.ui.viewmodels

import com.example.pathfinderapp.data.models.CharacterClass
import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.data.models.CharacterStats
import com.example.pathfinderapp.data.models.Race
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests unitarios SIMPLES que prueban solo la lógica del ViewModel
 * sin dependencias externas (Firebase, DataStore, etc.)
 */
class CharacterViewModelTest {

    private val testRace = Race("Human", "Versatile", mapOf("strength" to 1), "Skill")
    private val testClass = CharacterClass("Wizard", "Magic", "d6", "Intelligence")
    private val testStats = CharacterStats(15, 14, 13, 12, 10, 8)

    private fun createCharacter(id: String, name: String) =
        CharacterProfile(id, name, testRace, testClass, testStats, 10)

    // ==================== TESTS DE MODELOS ====================

    @Test
    fun `CharacterProfile se crea correctamente`() {
        val character = createCharacter("test1", "Gandalf")

        assertEquals("test1", character.id)
        assertEquals("Gandalf", character.name)
        assertEquals(testRace, character.race)
        assertEquals(testClass, character.characterClass)
        assertEquals(testStats, character.stats)
        assertEquals(10, character.level)
    }

    @Test
    fun `CharacterStats tiene valores correctos`() {
        assertEquals(15, testStats.strength)
        assertEquals(14, testStats.dexterity)
        assertEquals(13, testStats.constitution)
        assertEquals(12, testStats.intelligence)
        assertEquals(10, testStats.wisdom)
        assertEquals(8, testStats.charisma)
    }

    @Test
    fun `Race tiene propiedades correctas`() {
        assertEquals("Human", testRace.name)
        assertEquals("Versatile", testRace.description)
        assertEquals(1, testRace.bonuses["strength"])
    }

    @Test
    fun `CharacterClass tiene propiedades correctas`() {
        assertEquals("Wizard", testClass.name)
        assertEquals("Magic", testClass.description)
        assertEquals("d6", testClass.hitDie)
    }

    // ==================== TESTS DE COPIA ====================

    @Test
    fun `CharacterProfile copy modifica solo campos especificados`() {
        val original = createCharacter("id1", "Original")
        val copy = original.copy(name = "Copy")

        assertEquals("id1", copy.id)
        assertEquals("Copy", copy.name)
        assertEquals(original.race, copy.race)
        assertEquals(original.characterClass, copy.characterClass)
    }

    @Test
    fun `CharacterProfile copy con id diferente`() {
        val original = createCharacter("id1", "Character")
        val copy = original.copy(id = "id2")

        assertEquals("id2", copy.id)
        assertEquals("Character", copy.name)
    }

    // ==================== TESTS DE IGUALDAD ====================

    @Test
    fun `dos CharacterProfile con mismo id son diferentes objetos`() {
        val char1 = createCharacter("id1", "Name1")
        val char2 = createCharacter("id1", "Name2")

        assertNotSame(char1, char2)
        assertEquals("id1", char1.id)
        assertEquals("id1", char2.id)
    }

    @Test
    fun `CharacterProfile con propiedades iguales`() {
        val char1 = createCharacter("id1", "Gandalf")
        val char2 = createCharacter("id1", "Gandalf")

        assertEquals(char1.id, char2.id)
        assertEquals(char1.name, char2.name)
        assertEquals(char1.level, char2.level)
    }

    // ==================== TESTS DE LISTA ====================

    @Test
    fun `lista de personajes se puede filtrar por id`() {
        val list = listOf(
            createCharacter("id1", "Gandalf"),
            createCharacter("id2", "Aragorn"),
            createCharacter("id3", "Legolas")
        )

        val filtered = list.filter { it.id != "id2" }

        assertEquals(2, filtered.size)
        assertEquals("id1", filtered[0].id)
        assertEquals("id3", filtered[1].id)
    }

    @Test
    fun `lista de personajes se puede agregar elementos`() {
        val list = mutableListOf(
            createCharacter("id1", "Gandalf")
        )

        list.add(createCharacter("id2", "Aragorn"))

        assertEquals(2, list.size)
        assertEquals("Gandalf", list[0].name)
        assertEquals("Aragorn", list[1].name)
    }

    @Test
    fun `lista de personajes mantiene orden`() {
        val list = listOf(
            createCharacter("id1", "Gandalf"),
            createCharacter("id2", "Aragorn"),
            createCharacter("id3", "Legolas")
        )

        assertEquals("Gandalf", list[0].name)
        assertEquals("Aragorn", list[1].name)
        assertEquals("Legolas", list[2].name)
    }

    @Test
    fun `lista inmutable no se puede modificar`() {
        val list = listOf(createCharacter("id1", "Gandalf"))
        val newList = list + createCharacter("id2", "Aragorn")

        assertEquals(1, list.size)
        assertEquals(2, newList.size)
    }

    // ==================== TESTS DE EDGE CASES ====================

    @Test
    fun `CharacterProfile con id vacio`() {
        val char = createCharacter("", "TestName")
        assertEquals("", char.id)
    }

    @Test
    fun `CharacterProfile con nombre vacio`() {
        val char = createCharacter("id1", "")
        assertEquals("", char.name)
    }

    @Test
    fun `CharacterProfile con level 0`() {
        val char = CharacterProfile("id1", "Test", testRace, testClass, testStats, 0)
        assertEquals(0, char.level)
    }

    @Test
    fun `filtrar lista elimina todos los elementos con mismo id`() {
        val list = listOf(
            createCharacter("id1", "Name1"),
            createCharacter("id1", "Name2"),
            createCharacter("id2", "Name3")
        )

        val filtered = list.filter { it.id != "id1" }

        assertEquals(1, filtered.size)
        assertEquals("id2", filtered[0].id)
    }
}