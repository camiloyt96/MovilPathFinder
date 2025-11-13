package com.example.pathfinderapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pathfinderapp.data.models.CharacterProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(private val context: Context) : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterProfile>>(emptyList())
    val characters: StateFlow<List<CharacterProfile>> = _characters.asStateFlow()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        // Aquí cargarías los personajes desde tu fuente de datos
        // Por ahora, inicializa con una lista vacía
        _characters.value = emptyList()

        // TODO: Implementar carga desde SharedPreferences, Room o Firebase
        // Ejemplo con SharedPreferences:
        // val prefs = context.getSharedPreferences("characters", Context.MODE_PRIVATE)
        // val json = prefs.getString("characters_list", "[]")
        // _characters.value = Json.decodeFromString(json)
    }

    fun addCharacter(character: CharacterProfile) {
        viewModelScope.launch {
            val currentList = _characters.value.toMutableList()

            // Verificar que no exceda el límite de 5 personajes
            if (currentList.size < 5) {
                currentList.add(character)
                _characters.value = currentList

                // Guardar en persistencia
                saveCharacters(currentList)
            }
        }
    }

    fun removeCharacter(characterId: String) {
        viewModelScope.launch {
            val currentList = _characters.value.toMutableList()
            currentList.removeAll { it.id == characterId }
            _characters.value = currentList

            // Guardar en persistencia
            saveCharacters(currentList)
        }
    }

    private fun saveCharacters(characters: List<CharacterProfile>) {
        // TODO: Implementar guardado en SharedPreferences, Room o Firebase
        // Ejemplo con SharedPreferences:
        // val prefs = context.getSharedPreferences("characters", Context.MODE_PRIVATE)
        // val json = Json.encodeToString(characters)
        // prefs.edit().putString("characters_list", json).apply()
    }
}
