package com.example.pathfinderapp.data.models

import com.google.gson.annotations.SerializedName

// Respuesta de lista de hechizos
data class SpellListResponse(
    val count: Int,
    val results: List<Spell>
)

// Hechizo básico (para la lista)
data class Spell(
    val index: String,
    val name: String,
    val url: String
)

// Detalle completo de un hechizo
data class SpellDetail(
    val index: String,
    val name: String,
    val desc: List<String>,

    @SerializedName("higher_level")
    val higherLevel: List<String>?,

    val range: String,
    val components: List<String>,
    val material: String?,
    val ritual: Boolean,
    val duration: String,
    val concentration: Boolean,

    @SerializedName("casting_time")
    val castingTime: String,

    val level: Int,

    @SerializedName("attack_type")
    val attackType: String?,

    val damage: SpellDamage?,
    val dc: SpellDC?,

    @SerializedName("area_of_effect")
    val areaOfEffect: AreaOfEffect?,

    val school: School,
    val classes: List<SpellClass>,
    val subclasses: List<SpellSubclass>
)

data class SpellDamage(
    @SerializedName("damage_type")
    val damageType: Reference?,

    @SerializedName("damage_at_slot_level")
    val damageAtSlotLevel: Map<String, String>?,

    @SerializedName("damage_at_character_level")
    val damageAtCharacterLevel: Map<String, String>?
)

data class SpellDC(
    @SerializedName("dc_type")
    val dcType: Reference,

    @SerializedName("dc_success")
    val dcSuccess: String?
)

data class AreaOfEffect(
    val type: String,
    val size: Int
)

data class School(
    val index: String,
    val name: String,
    val url: String
)

data class SpellClass(
    val index: String,
    val name: String,
    val url: String
)

data class SpellSubclass(
    val index: String,
    val name: String,
    val url: String
)
