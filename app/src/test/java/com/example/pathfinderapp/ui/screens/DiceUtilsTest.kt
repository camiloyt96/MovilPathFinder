package com.example.pathfinderapp.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Test

class DiceUtilsTest {
    @Test
    fun `critico perfecto en D20 retorna mensaje correcto`() {

        val message = getResultMessage(20, 20)

        assertEquals(" ¡CRÍTICO PERFECTO! ", message)
    }

    @Test
    fun `critico perfecto en D6 retorna mensaje correcto`() {
        val message = getResultMessage(6, 6)

        assertEquals(" ¡CRÍTICO PERFECTO! ", message)
    }

    @Test
    fun `critico perfecto en D100 retorna mensaje correcto`() {
        val message = getResultMessage(100, 100)

        assertEquals(" ¡CRÍTICO PERFECTO! ", message)
    }

    @Test
    fun `fallo critico retorna mensaje correcto`() {
        val message = getResultMessage(1, 20)

        assertEquals(" ¡FALLO CRÍTICO! ", message)
    }

    @Test
    fun `fallo critico en cualquier dado retorna mensaje correcto`() {
        val messageD6 = getResultMessage(1, 6)
        val messageD100 = getResultMessage(1, 100)

        assertEquals(" ¡FALLO CRÍTICO! ", messageD6)
        assertEquals(" ¡FALLO CRÍTICO! ", messageD100)
    }

    @Test
    fun `tirada excelente en D20 retorna mensaje correcto`() {
        val message18 = getResultMessage(18, 20)
        val message19 = getResultMessage(19, 20)

        assertEquals(" ¡Excelente tirada!", message18)
        assertEquals(" ¡Excelente tirada!", message19)
    }

    @Test
    fun `tirada muy buena en D20 retorna mensaje correcto`() {
        val message15 = getResultMessage(15, 20)
        val message17 = getResultMessage(17, 20)

        assertEquals(" ¡Muy buena tirada!", message15)
        assertEquals(" ¡Muy buena tirada!", message17)
    }

    @Test
    fun `tirada decente en D20 retorna mensaje correcto`() {
        val message = getResultMessage(10, 20)

        assertEquals(" Tirada decente", message)
    }

    @Test
    fun `tirada regular en D20 retorna mensaje correcto`() {
        val message = getResultMessage(6, 20)

        assertEquals(" Tirada regular", message)
    }

    @Test
    fun `tirada baja en D20 retorna mensaje correcto`() {
        val message2 = getResultMessage(2, 20)
        val message5 = getResultMessage(5, 20)

        assertEquals(" Tirada baja...", message2)
        assertEquals(" Tirada baja...", message5)
    }

    @Test
    fun `tirada en D6 excelente retorna mensaje correcto`() {
        val message = getResultMessage(5, 6)

        assertEquals(" ¡Excelente tirada!", message)
    }

    @Test
    fun `tirada en D6 muy buena retorna mensaje correcto`() {
        val message = getResultMessage(4, 6)

        assertEquals(" ¡Muy buena tirada!", message)
    }

    @Test
    fun `tirada en D6 baja retorna mensaje correcto`() {
        val message = getResultMessage(2, 6)

        assertEquals(" Tirada regular", message)
    }

    @Test
    fun `tirada en D100 critico perfecto retorna mensaje correcto`() {
        val message = getResultMessage(100, 100)

        assertEquals(" ¡CRÍTICO PERFECTO! ", message)
    }

    @Test
    fun `tirada en D100 excelente retorna mensaje correcto`() {
        val message90 = getResultMessage(90, 100)
        val message99 = getResultMessage(99, 100)

        assertEquals(" ¡Excelente tirada!", message90)
        assertEquals(" ¡Excelente tirada!", message99)
    }

    @Test
    fun `tirada en D100 muy buena retorna mensaje correcto`() {
        val message75 = getResultMessage(75, 100)
        val message89 = getResultMessage(89, 100)

        assertEquals(" ¡Muy buena tirada!", message75)
        assertEquals(" ¡Muy buena tirada!", message89)
    }

    @Test
    fun `mensajes para D4 funcionan correctamente`() {
        assertEquals(" ¡CRÍTICO PERFECTO! ", getResultMessage(4, 4))
        assertEquals(" ¡FALLO CRÍTICO! ", getResultMessage(1, 4))
        assertEquals(" ¡Excelente tirada!", getResultMessage(3, 4))
        assertEquals(" Tirada decente", getResultMessage(2, 4))
    }

    @Test
    fun `mensajes para D8 funcionan correctamente`() {
        assertEquals(" ¡CRÍTICO PERFECTO! ", getResultMessage(8, 8))
        assertEquals(" ¡FALLO CRÍTICO! ", getResultMessage(1, 8))
        assertEquals(" ¡Excelente tirada!", getResultMessage(7, 8))
        assertEquals(" ¡Muy buena tirada!", getResultMessage(6, 8))
    }

    @Test
    fun `mensajes para D10 funcionan correctamente`() {
        assertEquals(" ¡CRÍTICO PERFECTO! ", getResultMessage(10, 10))
        assertEquals(" ¡FALLO CRÍTICO! ", getResultMessage(1, 10))
        assertEquals(" ¡Excelente tirada!", getResultMessage(9, 10))
        assertEquals(" ¡Muy buena tirada!", getResultMessage(8, 10))
    }

    @Test
    fun `mensajes para D12 funcionan correctamente`() {
        assertEquals(" ¡CRÍTICO PERFECTO! ", getResultMessage(12, 12))
        assertEquals(" ¡FALLO CRÍTICO! ", getResultMessage(1, 12))
        assertEquals(" ¡Excelente tirada!", getResultMessage(11, 12))
        assertEquals(" ¡Muy buena tirada!", getResultMessage(9, 12))
    }

    @Test
    fun `DiceType D4 tiene valores correctos`() {
        assertEquals(4, DiceType.D4.sides)
        assertEquals("D4", DiceType.D4.label)
    }

    @Test
    fun `DiceType D6 tiene valores correctos`() {
        assertEquals(6, DiceType.D6.sides)
        assertEquals("D6", DiceType.D6.label)
    }

    @Test
    fun `DiceType D8 tiene valores correctos`() {
        assertEquals(8, DiceType.D8.sides)
        assertEquals("D8", DiceType.D8.label)
    }

    @Test
    fun `DiceType D10 tiene valores correctos`() {
        assertEquals(10, DiceType.D10.sides)
        assertEquals("D10", DiceType.D10.label)
    }

    @Test
    fun `DiceType D12 tiene valores correctos`() {
        assertEquals(12, DiceType.D12.sides)
        assertEquals("D12", DiceType.D12.label)
    }

    @Test
    fun `DiceType D20 tiene valores correctos`() {
        assertEquals(20, DiceType.D20.sides)
        assertEquals("D20", DiceType.D20.label)
    }

    @Test
    fun `DiceType D100 tiene valores correctos`() {
        assertEquals(100, DiceType.D100.sides)
        assertEquals("D100", DiceType.D100.label)
    }

    @Test
    fun `DiceType tiene 7 tipos de dados`() {
        assertEquals(7, DiceType.values().size)
    }

    @Test
    fun `todos los valores de D20 tienen un mensaje definido`() {
        for (value in 1..20) {
            val message = getResultMessage(value, 20)
            assert(message.isNotEmpty()) { "El valor $value no tiene mensaje" }
        }
    }

    @Test
    fun `todos los valores de D6 tienen un mensaje definido`() {
        for (value in 1..6) {
            val message = getResultMessage(value, 6)
            assert(message.isNotEmpty()) { "El valor $value no tiene mensaje" }
        }
    }

    @Test
    fun `valores fuera de rango devuelven resultado apropiado`() {
        val message0 = getResultMessage(0, 20)

        val message21 = getResultMessage(21, 20)

        assertEquals(" Tirada baja...", message0)
        assertEquals(" ¡Excelente tirada!", message21)
    }
}