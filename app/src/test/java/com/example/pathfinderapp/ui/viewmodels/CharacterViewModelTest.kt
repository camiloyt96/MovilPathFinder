package com.example.pathfinderapp.ui.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.pathfinderapp.data.models.CharacterClass
import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.data.models.CharacterStats
import com.example.pathfinderapp.data.models.Race
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var mockContext: Context
    private lateinit var mockDataStore: DataStore<Preferences>
    private val testDispatcher = UnconfinedTestDispatcher()

    private val testRace = Race("Human", "Versatile", mapOf("strength" to 1), "Skill")
    private val testClass = CharacterClass("Wizard", "Magic", "d6", "Intelligence")
    private val testStats = CharacterStats(15, 14, 13, 12, 10, 8)

    private val testCharacter1 = CharacterProfile("char1", "Gandalf", testRace, testClass, testStats, 10)
    private val testCharacter2 = CharacterProfile("char2", "Aragorn", testRace, testClass, testStats, 8)
    private val testCharacter3 = CharacterProfile("char3", "Legolas", testRace, testClass, testStats, 7)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mock Firebase
        mockkStatic(FirebaseApp::class)
        mockkStatic(FirebaseAuth::class)
        mockkStatic(FirebaseFirestore::class)

        val mockFirebaseApp = mockk<FirebaseApp>(relaxed = true)
        val mockAuth = mockk<FirebaseAuth>(relaxed = true)
        val mockFirestore = mockk<FirebaseFirestore>(relaxed = true)

        every { FirebaseApp.initializeApp(any()) } returns mockFirebaseApp
        every { FirebaseAuth.getInstance() } returns mockAuth
        every { FirebaseFirestore.getInstance() } returns mockFirestore
        every { mockAuth.currentUser } returns null  // Sin usuario = modo local

        // Mock Context y DataStore
        mockContext = mockk(relaxed = true)
        mockDataStore = mockk(relaxed = true)

        // Mock DataStore para devolver preferencias vacías
        every { mockDataStore.data } returns flowOf(emptyPreferences())
        coEvery { mockDataStore.updateData(any()) } returns emptyPreferences()

        // Mock del property delegate
        mockkStatic("com.example.pathfinderapp.data.repository.CharacterRepositoryKt")

        // Crear ViewModel
        viewModel = CharacterViewModel(mockContext)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    // ==================== ESTADO INICIAL ====================

    @Test
    fun `estado inicial lista vacia`() {
        assertTrue(viewModel.characters.value.isEmpty())
    }

    // ==================== AGREGAR ====================

    @Test
    fun `addCharacter agrega un personaje`() {
        viewModel.addCharacter(testCharacter1)

        val list = viewModel.characters.value
        assertEquals(1, list.size)
        assertEquals("Gandalf", list[0].name)
    }

    @Test
    fun `addCharacter agrega multiples personajes`() {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)
        viewModel.addCharacter(testCharacter3)

        val list = viewModel.characters.value
        assertEquals(3, list.size)
        assertEquals("Gandalf", list[0].name)
        assertEquals("Aragorn", list[1].name)
        assertEquals("Legolas", list[2].name)
    }

    @Test
    fun `addCharacter preserva propiedades`() {
        viewModel.addCharacter(testCharacter1)

        val c = viewModel.characters.value[0]
        assertEquals("char1", c.id)
        assertEquals(15, c.stats.strength)
        assertEquals("Wizard", c.characterClass.name)
    }

    // ==================== REMOVER ====================

    @Test
    fun `removeCharacter elimina por id`() {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)

        viewModel.removeCharacter("char1")

        val list = viewModel.characters.value
        assertEquals(1, list.size)
        assertEquals("char2", list[0].id)
    }

    @Test
    fun `removeCharacter id inexistente no afecta`() {
        viewModel.addCharacter(testCharacter1)

        viewModel.removeCharacter("nope")

        assertEquals(1, viewModel.characters.value.size)
    }

    @Test
    fun `removeCharacter mantiene orden`() {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)
        viewModel.addCharacter(testCharacter3)

        viewModel.removeCharacter("char2")

        val list = viewModel.characters.value
        assertEquals(2, list.size)
        assertEquals("Gandalf", list[0].name)
        assertEquals("Legolas", list[1].name)
    }

    // ==================== OPERACIONES COMBINADAS ====================

    @Test
    fun `agregar y remover mantiene consistencia`() {
        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(testCharacter2)

        viewModel.removeCharacter("char1")
        viewModel.addCharacter(testCharacter3)

        val list = viewModel.characters.value
        assertEquals(2, list.size)
        assertTrue(list.any { it.id == "char2" })
        assertTrue(list.any { it.id == "char3" })
    }

    // ==================== EXTREMOS ====================

    @Test
    fun `agregar con id vacio`() {
        val c = testCharacter1.copy(id = "")
        viewModel.addCharacter(c)

        val list = viewModel.characters.value
        assertEquals(1, list.size)
        assertEquals("", list[0].id)
    }

    @Test
    fun `remover id duplicado elimina todos`() {
        val clone = testCharacter1.copy(name = "Clone")

        viewModel.addCharacter(testCharacter1)
        viewModel.addCharacter(clone)
        viewModel.addCharacter(testCharacter3)

        viewModel.removeCharacter("char1")

        val list = viewModel.characters.value
        assertEquals(1, list.size)
        assertEquals("char3", list[0].id)
    }

    // ==================== INMUTABILIDAD ====================

    @Test
    fun `lista externa no afecta estado interno`() {
        viewModel.addCharacter(testCharacter1)

        val external = viewModel.characters.value.toMutableList()
        external.add(testCharacter2)

        assertEquals(1, viewModel.characters.value.size)
        assertEquals(2, external.size)
    }

    @Test
    fun `cada emision es lista distinta`() {
        viewModel.addCharacter(testCharacter1)
        val l1 = viewModel.characters.value

        viewModel.addCharacter(testCharacter2)
        val l2 = viewModel.characters.value

        assertNotSame(l1, l2)
    }

    // ==================== TESTS ADICIONALES ====================

    @Test
    fun `isLoading es false al inicio`() {
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `error es null al inicio`() {
        assertNull(viewModel.error.value)
    }

    @Test
    fun `clearError limpia el error`() {
        viewModel.clearError()
        assertNull(viewModel.error.value)
    }
}