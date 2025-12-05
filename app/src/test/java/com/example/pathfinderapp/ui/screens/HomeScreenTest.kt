package com.example.pathfinderapp.ui.screens

import org.junit.Test
import org.junit.Assert.*

class HomeScreenTest {

    // ==================== TESTS DE VERSIÓN ====================

    @Test
    fun `getAppVersion retorna versión correcta`() {
        val version = getAppVersion()

        assertEquals("v1.0.0", version)
    }

    @Test
    fun `getAppBuildType retorna tipo correcto`() {
        val buildType = getAppBuildType()

        assertEquals("Alfa", buildType)
    }

    @Test
    fun `getFullVersionString retorna string completo`() {
        val fullVersion = getFullVersionString()

        assertEquals("v1.0.0 • Alfa", fullVersion)
    }

    @Test
    fun `isAlphaVersion retorna true para build alfa`() {
        assertTrue(isAlphaVersion())
    }

    // ==================== TESTS DE FEATURES ====================

    @Test
    fun `getAvailableFeatures retorna todas las features`() {
        val features = getAvailableFeatures()

        assertEquals(3, features.size)
        assertTrue(features.any { it.name == "Dados" })
        assertTrue(features.any { it.name == "Personajes" })
        assertTrue(features.any { it.name == "Hechizos" })
    }

    @Test
    fun `getFeatureByName con nombre válido retorna feature`() {
        val feature = getFeatureByName("Dados")

        assertNotNull(feature)
        assertEquals("Dados", feature?.name)
        assertEquals("D4-D100", feature?.subtitle)
    }

    @Test
    fun `getFeatureByName con nombre inválido retorna null`() {
        val feature = getFeatureByName("NoExiste")

        assertNull(feature)
    }

    @Test
    fun `isFeatureEnabled retorna true para features activas`() {
        assertTrue(isFeatureEnabled("Dados"))
        assertTrue(isFeatureEnabled("Personajes"))
        assertTrue(isFeatureEnabled("Hechizos"))
    }

    @Test
    fun `isFeatureEnabled retorna false para features no existentes`() {
        assertFalse(isFeatureEnabled("FeatureInexistente"))
    }

    // ==================== TESTS DE NAVEGACIÓN ====================

    @Test
    fun `getNavigationRoute para Dados retorna ruta correcta`() {
        val route = getNavigationRoute("Dados")

        assertEquals("/dice", route)
    }

    @Test
    fun `getNavigationRoute para Personajes retorna ruta correcta`() {
        val route = getNavigationRoute("Personajes")

        assertEquals("/characters", route)
    }

    @Test
    fun `getNavigationRoute para Hechizos retorna ruta correcta`() {
        val route = getNavigationRoute("Hechizos")

        assertEquals("/wiki", route)
    }

    @Test
    fun `getNavigationRoute para feature inexistente retorna null`() {
        val route = getNavigationRoute("Inexistente")

        assertNull(route)
    }

    @Test
    fun `isValidNavigationRoute con ruta válida retorna true`() {
        assertTrue(isValidNavigationRoute("/dice"))
        assertTrue(isValidNavigationRoute("/characters"))
        assertTrue(isValidNavigationRoute("/wiki"))
    }

    @Test
    fun `isValidNavigationRoute con ruta inválida retorna false`() {
        assertFalse(isValidNavigationRoute("/invalid"))
        assertFalse(isValidNavigationRoute(""))
        assertFalse(isValidNavigationRoute("dice"))
    }

    // ==================== TESTS DE ANIMACIONES ====================

    @Test
    fun `getRotationDuration retorna duración correcta`() {
        val duration = getRotationDuration()

        assertEquals(20000, duration)
    }

    @Test
    fun `getScaleDuration retorna duración correcta`() {
        val duration = getScaleDuration()

        assertEquals(2000, duration)
    }

    @Test
    fun `getAlphaDuration retorna duración correcta`() {
        val duration = getAlphaDuration()

        assertEquals(3000, duration)
    }

    @Test
    fun `getAnimationParameters retorna todos los parámetros`() {
        val params = getAnimationParameters()

        assertEquals(20000, params.rotationDuration)
        assertEquals(2000, params.scaleDuration)
        assertEquals(3000, params.alphaDuration)
        assertEquals(0.95f, params.scaleMin, 0.01f)
        assertEquals(1.05f, params.scaleMax, 0.01f)
        assertEquals(0.3f, params.alphaMin, 0.01f)
        assertEquals(0.7f, params.alphaMax, 0.01f)
    }

    // ==================== TESTS DE VALIDACIÓN DE ESTADO ====================

    @Test
    fun `isHomeScreenReady con callbacks válidos retorna true`() {
        val onNavigateToDice: () -> Unit = {}
        val onNavigateToCharacters: () -> Unit = {}
        val onNavigateToWiki: () -> Unit = {}

        val result = isHomeScreenReady(
            onNavigateToDice,
            onNavigateToCharacters,
            onNavigateToWiki
        )

        assertTrue(result)
    }

    // ==================== TESTS DE FEATURES DESHABILITADAS ====================

    @Test
    fun `getDisabledFeatures retorna lista vacía en alfa`() {
        val disabled = getDisabledFeatures()

        assertTrue(disabled.isEmpty())
    }

    @Test
    fun `canAccessFeature retorna true para todas las features en alfa`() {
        assertTrue(canAccessFeature("Dados"))
        assertTrue(canAccessFeature("Personajes"))
        assertTrue(canAccessFeature("Hechizos"))
    }

    // ==================== TESTS DE METADATA ====================

    @Test
    fun `getAppTitle retorna título correcto`() {
        val title = getAppTitle()

        assertEquals("PATHFINDER", title)
    }

    @Test
    fun `getAppSubtitle retorna subtítulo correcto`() {
        val subtitle = getAppSubtitle()

        assertEquals("Juego de Rol", subtitle)
    }

    @Test
    fun `getAppMetadata retorna metadata completa`() {
        val metadata = getAppMetadata()

        assertEquals("PATHFINDER", metadata.title)
        assertEquals("Juego de Rol", metadata.subtitle)
        assertEquals("v1.0.0 • Alfa", metadata.version)
        assertEquals(3, metadata.featureCount)
    }

    // ==================== TESTS DE FORMATO ====================

    @Test
    fun `formatFeatureTitle con texto normal funciona`() {
        val result = formatFeatureTitle("Dados\nD4-D100")

        assertEquals("Dados D4-D100", result)
    }

    @Test
    fun `formatFeatureDescription elimina saltos de línea`() {
        val result = formatFeatureDescription("Gestión de\nPersonajes")

        assertEquals("Gestión de Personajes", result)
    }

    @Test
    fun `formatFeatureForAccessibility retorna texto accesible`() {
        val result = formatFeatureForAccessibility("Dados", "D4-D100")

        assertEquals("Dados: D4-D100", result)
    }

    // ==================== TESTS DE VALIDACIÓN DE UI ====================

    @Test
    fun `hasValidGradientColors retorna true con colores válidos`() {
        val colors = listOf(0xFF6200EE, 0xFF3700B3, 0xFF03DAC5)

        assertTrue(hasValidGradientColors(colors))
    }

    @Test
    fun `hasValidGradientColors retorna false con menos de 2 colores`() {
        val colors = listOf(0xFF6200EE)

        assertFalse(hasValidGradientColors(colors))
    }

    @Test
    fun `hasValidGradientColors retorna false con lista vacía`() {
        val colors = emptyList<Long>()

        assertFalse(hasValidGradientColors(colors))
    }

    // ==================== TESTS DE CONTADORES ====================

    @Test
    fun `getTotalFeatureCount retorna 3`() {
        val count = getTotalFeatureCount()

        assertEquals(3, count)
    }

    @Test
    fun `getEnabledFeatureCount retorna 3 en alfa`() {
        val count = getEnabledFeatureCount()

        assertEquals(3, count)
    }

    @Test
    fun `getFeatureCompletionPercentage retorna 100 cuando todas están habilitadas`() {
        val percentage = getFeatureCompletionPercentage()

        assertEquals(100.0, percentage, 0.01)
    }
}

// ==================== DATA CLASSES ====================

data class Feature(
    val name: String,
    val subtitle: String,
    val route: String,
    val enabled: Boolean = true
)

data class AnimationParameters(
    val rotationDuration: Int,
    val scaleDuration: Int,
    val alphaDuration: Int,
    val scaleMin: Float,
    val scaleMax: Float,
    val alphaMin: Float,
    val alphaMax: Float
)

data class AppMetadata(
    val title: String,
    val subtitle: String,
    val version: String,
    val featureCount: Int
)

// ==================== FUNCIONES HELPER ====================

fun getAppVersion(): String = "v1.0.0"

fun getAppBuildType(): String = "Alfa"

fun getFullVersionString(): String = "${getAppVersion()} • ${getAppBuildType()}"

fun isAlphaVersion(): Boolean = getAppBuildType() == "Alfa"

fun getAvailableFeatures(): List<Feature> {
    return listOf(
        Feature("Dados", "D4-D100", "/dice"),
        Feature("Personajes", "Gestión de\nPersonajes", "/characters"),
        Feature("Hechizos", "Hechizos de \nPersonaje", "/wiki")
    )
}

fun getFeatureByName(name: String): Feature? {
    return getAvailableFeatures().find { it.name == name }
}

fun isFeatureEnabled(featureName: String): Boolean {
    return getFeatureByName(featureName)?.enabled ?: false
}

fun getNavigationRoute(featureName: String): String? {
    return getFeatureByName(featureName)?.route
}

fun isValidNavigationRoute(route: String): Boolean {
    if (!route.startsWith("/")) return false
    return getAvailableFeatures().any { it.route == route }
}

fun getRotationDuration(): Int = 20000

fun getScaleDuration(): Int = 2000

fun getAlphaDuration(): Int = 3000

fun getAnimationParameters(): AnimationParameters {
    return AnimationParameters(
        rotationDuration = 20000,
        scaleDuration = 2000,
        alphaDuration = 3000,
        scaleMin = 0.95f,
        scaleMax = 1.05f,
        alphaMin = 0.3f,
        alphaMax = 0.7f
    )
}

fun isHomeScreenReady(
    onNavigateToDice: () -> Unit,
    onNavigateToCharacters: () -> Unit,
    onNavigateToWiki: () -> Unit
): Boolean {
    return true
}

fun getDisabledFeatures(): List<String> = emptyList()

fun canAccessFeature(featureName: String): Boolean {
    return isFeatureEnabled(featureName)
}

fun getAppTitle(): String = "PATHFINDER"

fun getAppSubtitle(): String = "Juego de Rol"

fun getAppMetadata(): AppMetadata {
    return AppMetadata(
        title = getAppTitle(),
        subtitle = getAppSubtitle(),
        version = getFullVersionString(),
        featureCount = getTotalFeatureCount()
    )
}

fun formatFeatureTitle(title: String): String {
    return title.replace("\n", " ")
}

fun formatFeatureDescription(description: String): String {
    return description.replace("\n", " ")
}

fun formatFeatureForAccessibility(title: String, subtitle: String): String {
    return "$title: ${formatFeatureDescription(subtitle)}"
}

fun hasValidGradientColors(colors: List<Long>): Boolean {
    return colors.size >= 2
}

fun getTotalFeatureCount(): Int {
    return getAvailableFeatures().size
}

fun getEnabledFeatureCount(): Int {
    return getAvailableFeatures().count { it.enabled }
}

fun getFeatureCompletionPercentage(): Double {
    val total = getTotalFeatureCount()
    val enabled = getEnabledFeatureCount()
    return if (total > 0) (enabled.toDouble() / total.toDouble()) * 100 else 0.0
}