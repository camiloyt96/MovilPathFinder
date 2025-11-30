package com.example.pathfinderapp.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.example.pathfinderapp.data.models.CharacterProfile
import org.json.JSONObject
import org.json.JSONArray
import java.io.File

object CharacterExportUtils {

    fun toJson(character: CharacterProfile): String {
        return try {
            Log.d("CharacterExport", "Convirtiendo personaje: ${character.name}")

            val json = JSONObject().apply {
                put("id", character.id)
                put("name", character.name)

                put("race", JSONObject().apply {
                    put("name", character.race.name)
                    put("description", character.race.description)
                    put("bonuses", JSONObject(character.race.bonuses))
                    put("specialTraits", character.race.specialTraits)
                })

                put("characterClass", JSONObject().apply {
                    put("name", character.characterClass.name)
                    put("description", character.characterClass.description)
                    put("hitDie", character.characterClass.hitDie)
                    put("primaryStats", character.characterClass.primaryStats)
                })

                put("stats", JSONObject().apply {
                    put("strength", character.stats.strength)
                    put("dexterity", character.stats.dexterity)
                    put("constitution", character.stats.constitution)
                    put("intelligence", character.stats.intelligence)
                    put("wisdom", character.stats.wisdom)
                    put("charisma", character.stats.charisma)
                })
            }

            val result = json.toString(2)
            Log.d("CharacterExport", "JSON generado: ${result.length} chars")
            result

        } catch (e: Exception) {
            Log.e("CharacterExport", "ERROR: ${e.message}", e)
            e.printStackTrace()
            "{}"
        }
    }

    fun toJsonList(characters: List<CharacterProfile>): String {
        return try {
            Log.d("CharacterExport", "Convirtiendo ${characters.size} personajes")

            val jsonArray = JSONArray()
            characters.forEach { character ->
                jsonArray.put(JSONObject().apply {
                    put("id", character.id)
                    put("name", character.name)

                    put("race", JSONObject().apply {
                        put("name", character.race.name)
                        put("description", character.race.description)
                        put("bonuses", JSONObject(character.race.bonuses))
                        put("specialTraits", character.race.specialTraits)
                    })

                    put("characterClass", JSONObject().apply {
                        put("name", character.characterClass.name)
                        put("description", character.characterClass.description)
                        put("hitDie", character.characterClass.hitDie)
                        put("primaryStats", character.characterClass.primaryStats)
                    })

                    put("stats", JSONObject().apply {
                        put("strength", character.stats.strength)
                        put("dexterity", character.stats.dexterity)
                        put("constitution", character.stats.constitution)
                        put("intelligence", character.stats.intelligence)
                        put("wisdom", character.stats.wisdom)
                        put("charisma", character.stats.charisma)
                    })
                })
            }

            val result = jsonArray.toString(2)
            Log.d("CharacterExport", "Lista generada: ${result.length} chars")
            result

        } catch (e: Exception) {
            Log.e("CharacterExport", "ERROR: ${e.message}", e)
            e.printStackTrace()
            "[]"
        }
    }

    fun shareCharacterAsJson(context: Context, character: CharacterProfile) {
        try {
            Log.d("CharacterExport", "Iniciando compartir JSON para: ${character.name}")

            val jsonString = toJson(character)
            val fileName = "${character.name.replace(" ", "_").replace("[^a-zA-Z0-9_]".toRegex(), "")}_character.json"

            val file = File(context.cacheDir, fileName)
            file.writeText(jsonString)

            Log.d("CharacterExport", "Archivo creado: ${file.length()} bytes")

            val authority = "${context.packageName}.fileprovider"
            val uri = FileProvider.getUriForFile(context, authority, file)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Personaje Pathfinder: ${character.name}")
                type = "application/json"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Compartir JSON"))

        } catch (e: Exception) {
            Log.e("CharacterExport", "Error al compartir JSON: ${e.message}", e)
            shareCharacterAsText(context, character)
        }
    }

    fun shareCharacterAsText(context: Context, character: CharacterProfile) {
        try {
            val text = buildCharacterText(character)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "Personaje Pathfinder: ${character.name}")
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            context.startActivity(Intent.createChooser(shareIntent, "Compartir personaje"))
        } catch (e: Exception) {
            Log.e("CharacterExport", "Error al compartir como texto: ${e.message}", e)
        }
    }

    fun shareAllCharactersAsJson(context: Context, characters: List<CharacterProfile>) {
        try {
            Log.d("CharacterExport", "Compartiendo ${characters.size} personajes")

            val jsonString = toJsonList(characters)
            val fileName = "pathfinder_characters.json"

            val file = File(context.cacheDir, fileName)
            file.writeText(jsonString)

            val authority = "${context.packageName}.fileprovider"
            val uri = FileProvider.getUriForFile(context, authority, file)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Mis Personajes de Pathfinder (${characters.size})")
                type = "application/json"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Compartir personajes"))

        } catch (e: Exception) {
            Log.e("CharacterExport", "Error al compartir todos los JSON: ${e.message}", e)
        }
    }

    fun saveCharacterAsJsonFile(context: Context, character: CharacterProfile): Intent {
        val fileName = "${character.name.replace(" ", "_").replace("[^a-zA-Z0-9_]".toRegex(), "")}_character.json"

        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
    }

    fun saveAllCharactersAsJsonFile(context: Context, characters: List<CharacterProfile>): Intent {
        val fileName = "pathfinder_characters.json"

        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
    }

    private fun buildCharacterText(character: CharacterProfile): String {
        val finalStats = calculateFinalStats(character)

        return buildString {
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("PERSONAJE PATHFINDER 1E")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()
            appendLine("👤 NOMBRE: ${character.name}")
            appendLine("🧬 RAZA: ${character.race.name}")
            appendLine("⚔️ CLASE: ${character.characterClass.name} (${character.characterClass.hitDie})")
            appendLine()
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📊 ESTADÍSTICAS BASE")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("Fuerza (FUE):        ${character.stats.strength} (${getModifier(character.stats.strength)})")
            appendLine("Destreza (DES):      ${character.stats.dexterity} (${getModifier(character.stats.dexterity)})")
            appendLine("Constitución (CON):  ${character.stats.constitution} (${getModifier(character.stats.constitution)})")
            appendLine("Inteligencia (INT):  ${character.stats.intelligence} (${getModifier(character.stats.intelligence)})")
            appendLine("Sabiduría (SAB):     ${character.stats.wisdom} (${getModifier(character.stats.wisdom)})")
            appendLine("Carisma (CAR):       ${character.stats.charisma} (${getModifier(character.stats.charisma)})")

            if (character.race.bonuses.isNotEmpty()) {
                appendLine()
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("🎁 BONIFICADORES RACIALES")
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                character.race.bonuses.forEach { (stat, bonus) ->
                    appendLine("$stat: ${if (bonus >= 0) "+" else ""}$bonus")
                }

                appendLine()
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("📊 ESTADÍSTICAS FINALES")
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("Fuerza:        ${finalStats.strength} (${getModifier(finalStats.strength)})")
                appendLine("Destreza:      ${finalStats.dexterity} (${getModifier(finalStats.dexterity)})")
                appendLine("Constitución:  ${finalStats.constitution} (${getModifier(finalStats.constitution)})")
                appendLine("Inteligencia:  ${finalStats.intelligence} (${getModifier(finalStats.intelligence)})")
                appendLine("Sabiduría:     ${finalStats.wisdom} (${getModifier(finalStats.wisdom)})")
                appendLine("Carisma:       ${finalStats.charisma} (${getModifier(finalStats.charisma)})")
            }

            if (character.race.specialTraits.isNotBlank()) {
                appendLine()
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine("✨ RASGOS RACIALES")
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine(character.race.specialTraits)
            }

            appendLine()
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📝 DESCRIPCIÓN DE RAZA")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine(character.race.description)

            appendLine()
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("⚔️ DESCRIPCIÓN DE CLASE")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine(character.characterClass.description)
            appendLine("Estadísticas principales: ${character.characterClass.primaryStats}")
            appendLine()
            appendLine("Generado con Pathfinder Character Creator")
        }
    }

    private data class FinalStats(
        val strength: Int,
        val dexterity: Int,
        val constitution: Int,
        val intelligence: Int,
        val wisdom: Int,
        val charisma: Int
    )

    private fun calculateFinalStats(character: CharacterProfile): FinalStats {
        val bonuses = character.race.bonuses
        return FinalStats(
            strength = character.stats.strength + (bonuses["Fuerza"] ?: 0),
            dexterity = character.stats.dexterity + (bonuses["Destreza"] ?: 0),
            constitution = character.stats.constitution + (bonuses["Constitución"] ?: 0),
            intelligence = character.stats.intelligence + (bonuses["Inteligencia"] ?: 0),
            wisdom = character.stats.wisdom + (bonuses["Sabiduría"] ?: 0),
            charisma = character.stats.charisma + (bonuses["Carisma"] ?: 0)
        )
    }

    private fun getModifier(stat: Int): String {
        val mod = (stat - 10) / 2
        return if (mod >= 0) "+$mod" else "$mod"
    }
}