package com.example.pathfinderapp.ui.viewmodels

import android.content.Context
import com.example.pathfinderapp.data.models.CharacterClass
import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.data.models.CharacterStats
import com.example.pathfinderapp.data.models.Race
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var mockContext: Context
    private val testDispatcher = StandardTestDispatcher()

    // Test data
    private val testRace = Race(
        name = "Human",
        description = "Versatile and ambitious",
        bonuses = mapOf("strength" to 1),
        specialTraits = "Extra skill"
    )

    private val testClass = CharacterClass(
        name = "Wizard",
        description = "Master of magic",
        hitDie = "d6",
        primaryStats = "Intelligence"
    )

    private val testStats = CharacterStats(
        strength = 15,
        dexterity = 14,
        constitution = 13,
        intelligence = 12,
        wisdom = 10,
        charisma = 8
    )

    private val testCharacter1 = CharacterProfile(
        id = "char1",
        name = "Gandalf",
        race = testRace,
        characterClass = testClass,
        stats = testStats,
        level = 10
    )

    private val testCharacter2 = CharacterProfile(
        id = "char2",
        name = "Aragorn",
        race = testRace,
        characterClass = testClass,
        stats = testStats,
        level = 8
    )

    private val testCharacter3 = CharacterProfile(
        id = "char3",
        name = "Legolas",
        race = testRace,
        characterClass = testClass,
        stats = testStats,
        level = 7
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockContext = mockk(relaxed = true)
        viewModel = CharacterViewModel(mockContext)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ==================== PRUEBAS DE ESTADO INICIAL ====================

    @Test
    fun `estado inicial tiene lista de personajes vacia`() {
        assertTrue(viewModel.characters.value.isEmpty())
    }

    @Test
    fun `estado inicial tiene tamaño 0`() {
        assertEquals(0, viewModel.characters.value.size)
    }

    // ==================== PRUEBAS DE AGREGAR PERSONAJES ====================

    @Test
    fun `addCharacter agrega un personaje correctamente`() = runTest {
        viewModel.addCharacter(testCharacter1)
        advanceUntilIdle()

        assertEquals(1, viewModel.characters.value.size)
        assertEquals(testCharacter1, viewModel.characters.value[0])
    }

    @Test
    fun `addCharacter agrega multiples personajes en orden`() = runTest {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)
        viewModel.addCharacter(testCharacter3)
        advanceUntilIdle()

        assertEquals(3, viewModel.characters.value.size)
        assertEquals("Gandalf", viewModel.characters.value[0].name)
        assertEquals("Aragorn", viewModel.characters.value[1].name)
        assertEquals("Legolas", viewModel.characters.value[2].name)
    }

    @Test
    fun `addCharacter puede agregar hasta 5 personajes`() = runTest {
        val characters = (1..5).map {
            testCharacter1.copy(id = "char$it", name = "Character $it")
        }

        characters.forEach { viewModel.addCharacter(it) }
        advanceUntilIdle()

        assertEquals(5, viewModel.characters.value.size)
    }

    @Test
    fun `addCharacter no agrega mas de 5 personajes`() = runTest {
        val characters = (1..6).map {
            testCharacter1.copy(id = "char$it", name = "Character $it")
        }

        characters.forEach { viewModel.addCharacter(it) }
        advanceUntilIdle()

        assertEquals(5, viewModel.characters.value.size)
        assertFalse(viewModel.characters.value.any { it.id == "char6" })
    }

    @Test
    fun `addCharacter preserva todas las propiedades del personaje`() = runTest {
        viewModel.addCharacter(testCharacter1)
        advanceUntilIdle()

        val character = viewModel.characters.value[0]
        assertEquals("char1", character.id)
        assertEquals("Gandalf", character.name)
        assertEquals("Human", character.race.name)
        assertEquals("Wizard", character.characterClass.name)
        assertEquals(10, character.level)
        assertEquals(15, character.stats.strength)
    }

    // ==================== PRUEBAS DE REMOVER PERSONAJES ====================

    @Test
    fun `removeCharacter elimina un personaje correctamente`() = runTest {
        viewModel.addCharacter(testCharacter1)
        advanceUntilIdle()

        viewModel.removeCharacter("char1")
        advanceUntilIdle()

        assertEquals(0, viewModel.characters.value.size)
    }

    @Test
    fun `removeCharacter elimina el personaje correcto por id`() = runTest {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)
        viewModel.addCharacter(testCharacter3)
        advanceUntilIdle()

        viewModel.removeCharacter("char2")
        advanceUntilIdle()

        assertEquals(2, viewModel.characters.value.size)
        assertFalse(viewModel.characters.value.any { it.id == "char2" })
        assertTrue(viewModel.characters.value.any { it.id == "char1" })
        assertTrue(viewModel.characters.value.any { it.id == "char3" })
    }

    @Test
    fun `removeCharacter mantiene el orden de personajes restantes`() = runTest {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)
        viewModel.addCharacter(testCharacter3)
        advanceUntilIdle()

        viewModel.removeCharacter("char2")
        advanceUntilIdle()

        assertEquals("Gandalf", viewModel.characters.value[0].name)
        assertEquals("Legolas", viewModel.characters.value[1].name)
    }

    @Test
    fun `removeCharacter con id inexistente no afecta la lista`() = runTest {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)
        advanceUntilIdle()

        viewModel.removeCharacter("char999")
        advanceUntilIdle()

        assertEquals(2, viewModel.characters.value.size)
    }

    @Test
    fun `removeCharacter en lista vacia no causa error`() = runTest {
        viewModel.removeCharacter("char1")
        advanceUntilIdle()

        assertEquals(0, viewModel.characters.value.size)
    }

    // ==================== PRUEBAS DE OPERACIONES COMBINADAS ====================

    @Test
    fun `agregar y remover personajes mantiene consistencia`() = runTest {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)
        advanceUntilIdle()

        viewModel.removeCharacter("char1")
        viewModel.addCharacter(testCharacter3)
        advanceUntilIdle()

        assertEquals(2, viewModel.characters.value.size)
        assertTrue(viewModel.characters.value.any { it.id == "char2" })
        assertTrue(viewModel.characters.value.any { it.id == "char3" })
        assertFalse(viewModel.characters.value.any { it.id == "char1" })
    }

    @Test
    fun `remover personaje permite agregar hasta el limite nuevamente`() = runTest {
        val characters = (1..5).map {
            testCharacter1.copy(id = "char$it", name = "Character $it")
        }
        characters.forEach { viewModel.addCharacter(it) }
        advanceUntilIdle()

        viewModel.removeCharacter("char1")
        advanceUntilIdle()

        val newCharacter = testCharacter1.copy(id = "char6", name = "Character 6")
        viewModel.addCharacter(newCharacter)
        advanceUntilIdle()

        assertEquals(5, viewModel.characters.value.size)
        assertTrue(viewModel.characters.value.any { it.id == "char6" })
        assertFalse(viewModel.characters.value.any { it.id == "char1" })
    }

    // ==================== PRUEBAS DE CASOS EXTREMOS ====================

    @Test
    fun `agregar personaje con id vacio`() = runTest {
        val character = testCharacter1.copy(id = "")
        viewModel.addCharacter(character)
        advanceUntilIdle()

        assertEquals(1, viewModel.characters.value.size)
        assertEquals("", viewModel.characters.value[0].id)
    }

    @Test
    fun `agregar personaje con nombre vacio`() = runTest {
        val character = testCharacter1.copy(name = "")
        viewModel.addCharacter(character)
        advanceUntilIdle()

        assertEquals(1, viewModel.characters.value.size)
        assertEquals("", viewModel.characters.value[0].name)
    }

    @Test
    fun `remover personajes con id duplicado elimina todos`() = runTest {
        val char1 = testCharacter1
        val char2 = testCharacter1.copy(name = "Gandalf Clone")

        viewModel.addCharacter(char1)
        viewModel.addCharacter(char2)
        viewModel.addCharacter(testCharacter3)
        advanceUntilIdle()

        viewModel.removeCharacter("char1")
        advanceUntilIdle()

        assertEquals(1, viewModel.characters.value.size)
        assertEquals("char3", viewModel.characters.value[0].id)
    }

    // ==================== PRUEBAS DE INMUTABILIDAD ====================

    @Test
    fun `modificar lista externa no afecta el estado interno`() = runTest {
        viewModel.addCharacter(testCharacter1)
        advanceUntilIdle()

        val externalList = viewModel.characters.value.toMutableList()
        externalList.add(testCharacter2)

        assertEquals(1, viewModel.characters.value.size)
        assertEquals(2, externalList.size)
    }

    @Test
    fun `cada emision del StateFlow es una nueva lista`() = runTest {
        viewModel.addCharacter(testCharacter1)
        advanceUntilIdle()
        val list1 = viewModel.characters.value

        viewModel.addCharacter(testCharacter2)
        advanceUntilIdle()
        val list2 = viewModel.characters.value

        assertNotSame(list1, list2)
    }
}