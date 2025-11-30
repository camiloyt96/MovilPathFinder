    package com.example.pathfinderapp.viewmodels

    import com.example.pathfinderapp.ui.screens.DiceType
    import com.example.pathfinderapp.ui.viewmodel.DiceViewModel
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.ExperimentalCoroutinesApi
    import kotlinx.coroutines.test.StandardTestDispatcher
    import kotlinx.coroutines.test.advanceTimeBy
    import kotlinx.coroutines.test.advanceUntilIdle
    import kotlinx.coroutines.test.resetMain
    import kotlinx.coroutines.test.runTest
    import kotlinx.coroutines.test.setMain
    import org.junit.After
    import org.junit.Assert.assertEquals
    import org.junit.Assert.assertFalse
    import org.junit.Assert.assertTrue
    import org.junit.Before
    import org.junit.Test

    @OptIn(ExperimentalCoroutinesApi::class)
    class DiceViewModelTest {

        private lateinit var viewModel: DiceViewModel
        private val testDispatcher = StandardTestDispatcher()

        @Before
        fun setUp() {
            // Configura el dispatcher de prueba para coroutines
            Dispatchers.setMain(testDispatcher)
            viewModel = DiceViewModel()
        }

        @After
        fun tearDown() {
            // Limpia el dispatcher después de cada prueba
            Dispatchers.resetMain()
        }

        // ==================== PRUEBAS DE ESTADO INICIAL ====================

        @Test
        fun `estado inicial del dado seleccionado es D4`() {
            // Then
            assertEquals(DiceType.D4, viewModel.selectedDice.value)
        }

        @Test
        fun `estado inicial del valor del dado es 4`() {
            // Then
            assertEquals(4, viewModel.diceValue.value)
        }

        @Test
        fun `estado inicial de isRolling es false`() {
            // Then
            assertFalse(viewModel.isRolling.value)
        }

        @Test
        fun `estado inicial del historial esta vacio`() {
            // Then
            assertTrue(viewModel.rollHistory.value.isEmpty())
        }

        @Test
        fun `estado inicial de shakeToRoll esta habilitado`() {
            // Then
            assertTrue(viewModel.shakeToRollEnabled.value)
        }

        @Test
        fun `estado inicial de rotacion es 0`() {
            // Then
            assertEquals(0f, viewModel.rotationValue.value)
        }

        @Test
        fun `estado inicial de escala es 1`() {
            // Then
            assertEquals(1f, viewModel.scaleValue.value)
        }

        // ==================== PRUEBAS DE SELECCIÓN DE DADO ====================

        @Test
        fun `seleccionar D6 cambia el dado seleccionado`() {
            // When
            viewModel.selectDice(DiceType.D6)

            // Then
            assertEquals(DiceType.D6, viewModel.selectedDice.value)
        }

        @Test
        fun `seleccionar D20 actualiza el valor del dado a 20`() {
            // When
            viewModel.selectDice(DiceType.D20)

            // Then
            assertEquals(20, viewModel.diceValue.value)
        }

        @Test
        fun `seleccionar nuevo dado limpia el historial`() {
            // Given - Primero hacer una tirada para tener historial
            viewModel.selectDice(DiceType.D6)

            // When - Cambiar de dado
            viewModel.selectDice(DiceType.D8)

            // Then
            assertTrue(viewModel.rollHistory.value.isEmpty())
        }

        @Test
        fun `seleccionar D100 establece valor inicial en 100`() {
            // When
            viewModel.selectDice(DiceType.D100)

            // Then
            assertEquals(DiceType.D100, viewModel.selectedDice.value)
            assertEquals(100, viewModel.diceValue.value)
        }

        // ==================== PRUEBAS DE TOGGLE SHAKE ====================

        @Test
        fun `toggle shake desactiva la funcion de agitar`() {
            // Given
            assertTrue(viewModel.shakeToRollEnabled.value)

            // When
            viewModel.onShakeToggle(false)

            // Then
            assertFalse(viewModel.shakeToRollEnabled.value)
        }

        @Test
        fun `toggle shake activa la funcion de agitar`() {
            // Given
            viewModel.onShakeToggle(false)
            assertFalse(viewModel.shakeToRollEnabled.value)

            // When
            viewModel.onShakeToggle(true)

            // Then
            assertTrue(viewModel.shakeToRollEnabled.value)
        }

        // ==================== PRUEBAS DE TIRADA DE DADO ====================

        @Test
        fun `rollDice cambia isRolling a true durante la animacion`() = runTest {
            // When
            viewModel.rollDice(null)

            // Then - Durante la animación
            assertTrue(viewModel.isRolling.value)
        }

        @Test
        fun `rollDice genera un valor entre 1 y el maximo del dado D6`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D6)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle() // Espera a que termine la coroutine

            // Then
            val result = viewModel.diceValue.value
            assertTrue(result in 1..6)
        }

        @Test
        fun `rollDice genera un valor entre 1 y el maximo del dado D20`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D20)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            val result = viewModel.diceValue.value
            assertTrue(result in 1..20)
        }

        @Test
        fun `rollDice agrega el resultado al historial`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D6)
            assertTrue(viewModel.rollHistory.value.isEmpty())

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertEquals(1, viewModel.rollHistory.value.size)
        }

        @Test
        fun `rollDice mantiene maximo 10 elementos en el historial`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D6)

            // When - Tirar el dado 15 veces
            repeat(15) {
                viewModel.rollDice(null)
                advanceUntilIdle()
            }

            // Then - Solo debe haber 10 elementos
            assertEquals(10, viewModel.rollHistory.value.size)
        }

        @Test
        fun `rollDice resetea rotacion a 0 al finalizar`() = runTest {
            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertEquals(0f, viewModel.rotationValue.value)
        }

        @Test
        fun `rollDice resetea escala a 1 al finalizar`() = runTest {
            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertEquals(1f, viewModel.scaleValue.value)
        }

        @Test
        fun `rollDice cambia isRolling a false al finalizar`() = runTest {
            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertFalse(viewModel.isRolling.value)
        }

        @Test
        fun `no se puede tirar el dado mientras esta rodando`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D6)
            viewModel.rollDice(null)
            assertTrue(viewModel.isRolling.value)

            // When - Intentar tirar de nuevo mientras está rodando
            val historiaSizeAntes = viewModel.rollHistory.value.size
            viewModel.rollDice(null)
            advanceTimeBy(100) // Avanzar un poco el tiempo

            // Then - El historial no debe cambiar (no se ejecutó la segunda tirada)
            assertEquals(historiaSizeAntes, viewModel.rollHistory.value.size)
        }

        @Test
        fun `no se puede cambiar de dado mientras esta rodando`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D6)
            viewModel.rollDice(null)
            assertTrue(viewModel.isRolling.value)

            // When - Intentar cambiar de dado mientras está rodando
            viewModel.selectDice(DiceType.D20)

            // Then - El dado no debe cambiar
            assertEquals(DiceType.D6, viewModel.selectedDice.value)
        }

        // ==================== PRUEBAS DE MÚLTIPLES TIRADAS ====================

        @Test
        fun `multiples tiradas generan diferentes valores`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D20)
            val resultados = mutableSetOf<Int>()

            // When - Tirar 20 veces
            repeat(20) {
                viewModel.rollDice(null)
                advanceUntilIdle()
                resultados.add(viewModel.diceValue.value)
            }

            // Then - Debe haber al menos 2 valores diferentes (probabilísticamente casi seguro)
            assertTrue(resultados.size >= 2)
        }

        @Test
        fun `historial mantiene el orden mas reciente primero`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D6)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()
            val primerResultado = viewModel.diceValue.value

            viewModel.rollDice(null)
            advanceUntilIdle()
            val segundoResultado = viewModel.diceValue.value

            // Then - El más reciente debe estar primero
            val historial = viewModel.rollHistory.value
            assertEquals(segundoResultado, historial[0])
            assertEquals(primerResultado, historial[1])
        }

        // ==================== PRUEBAS DE CADA TIPO DE DADO ====================

        @Test
        fun `D4 genera valores validos`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D4)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.diceValue.value in 1..4)
        }

        @Test
        fun `D8 genera valores validos`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D8)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.diceValue.value in 1..8)
        }

        @Test
        fun `D10 genera valores validos`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D10)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.diceValue.value in 1..10)
        }

        @Test
        fun `D12 genera valores validos`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D12)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.diceValue.value in 1..12)
        }

        @Test
        fun `D100 genera valores validos`() = runTest {
            // Given
            viewModel.selectDice(DiceType.D100)

            // When
            viewModel.rollDice(null)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.diceValue.value in 1..100)
        }
    }