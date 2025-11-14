package com.example.pathfinderapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pathfinderapp.data.models.CharacterProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "characters_prefs")

class CharacterRepository(private val context: Context) {

    private val CHARACTERS_JSON = stringPreferencesKey("characters_json")
    private val gson = Gson()
    private val listType = object : TypeToken<List<CharacterProfile>>() {}.type

    suspend fun load(): List<CharacterProfile> {
        val prefs = context.dataStore.data.first()
        val raw = prefs[CHARACTERS_JSON] ?: "[]"
        return runCatching { gson.fromJson<List<CharacterProfile>>(raw, listType) }
            .getOrElse { emptyList() }
    }

    suspend fun save(list: List<CharacterProfile>) {
        val json = gson.toJson(list, listType)
        context.dataStore.edit { it[CHARACTERS_JSON] = json }
    }
}