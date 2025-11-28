package com.example.pathfinderapp.data.models

import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val race: Race,
    val characterClass: CharacterClass,
    val stats: CharacterStats
)

data class Race(
    val name: String,
    val description: String,
    val bonuses: Map<String, Int>,
    val specialTraits: String
)

data class CharacterClass(
    val name: String,
    val description: String,
    val hitDie: String,
    val primaryStats: String
)

data class CharacterStats(
    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val intelligence: Int = 10,
    val wisdom: Int = 10,
    val charisma: Int = 10
)