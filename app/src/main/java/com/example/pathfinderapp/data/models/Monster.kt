package com.example.pathfinderapp.data.models

import com.google.gson.annotations.SerializedName

// Respuesta de lista de monstruos
data class MonsterListResponse(
    val count: Int,
    val results: List<Monster>
)

// Monstruo básico (para la lista)
data class Monster(
    val index: String,
    val name: String,
    val url: String
)

// Detalle completo de un monstruo
data class MonsterDetail(
    val index: String,
    val name: String,
    val size: String,
    val type: String,
    val alignment: String,

    @SerializedName("armor_class")
    val armorClass: List<ArmorClass>?,

    @SerializedName("hit_points")
    val hitPoints: Int,

    @SerializedName("hit_dice")
    val hitDice: String,

    @SerializedName("hit_points_roll")
    val hitPointsRoll: String?,

    val speed: Speed,

    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,

    val proficiencies: List<Proficiency>?,

    @SerializedName("damage_vulnerabilities")
    val damageVulnerabilities: List<String>?,

    @SerializedName("damage_resistances")
    val damageResistances: List<String>?,

    @SerializedName("damage_immunities")
    val damageImmunities: List<String>?,

    @SerializedName("condition_immunities")
    val conditionImmunities: List<ConditionImmunity>?,

    val senses: Senses,
    val languages: String,

    @SerializedName("challenge_rating")
    val challengeRating: Double,

    val proficiency_bonus: Int?,
    val xp: Int,

    @SerializedName("special_abilities")
    val specialAbilities: List<SpecialAbility>?,

    val actions: List<Action>?,

    @SerializedName("legendary_actions")
    val legendaryActions: List<Action>?
)

data class ArmorClass(
    val type: String,
    val value: Int
)

data class Speed(
    val walk: String?,
    val swim: String?,
    val fly: String?,
    val burrow: String?,
    val climb: String?
)

data class Proficiency(
    val value: Int,
    val proficiency: Reference
)

data class Reference(
    val index: String,
    val name: String,
    val url: String
)

data class ConditionImmunity(
    val index: String,
    val name: String,
    val url: String
)

data class Senses(
    val passive_perception: Int,
    val blindsight: String?,
    val darkvision: String?,
    val tremorsense: String?,
    val truesight: String?
)

data class SpecialAbility(
    val name: String,
    val desc: String,
    val usage: Usage?
)

data class Usage(
    val type: String,
    val times: Int?,
    @SerializedName("rest_types")
    val restTypes: List<String>?
)

data class Action(
    val name: String,
    val desc: String,
    val attack_bonus: Int?,
    val damage: List<Damage>?,
    val dc: DC?
)

data class Damage(
    @SerializedName("damage_type")
    val damageType: Reference?,
    @SerializedName("damage_dice")
    val damageDice: String?
)

data class DC(
    @SerializedName("dc_type")
    val dcType: Reference,
    @SerializedName("dc_value")
    val dcValue: Int,
    @SerializedName("success_type")
    val successType: String
)