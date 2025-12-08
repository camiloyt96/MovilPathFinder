package com.example.pathfinderapp.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinderapp.data.models.CharacterProfile
import com.example.pathfinderapp.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(context: Context) : ViewModel() {

    private val repository = CharacterRepository(context)

    private val _characters = MutableStateFlow<List<CharacterProfile>>(emptyList())
    val characters: StateFlow<List<CharacterProfile>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                Log.d("CharacterViewModel", "Cargando personajes...")
                val loaded = repository.load()
                _characters.value = loaded

                Log.d("CharacterViewModel", "Personajes cargados: ${loaded.size}")

            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error cargando personajes: ${e.message}", e)
                _error.value = "Error al cargar personajes: ${e.message}"
                _characters.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCharacter(character: CharacterProfile) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                Log.d("CharacterViewModel", "Agregando personaje: ${character.name} (${character.id})")

                val updated = _characters.value + character
                _characters.value = updated

                repository.save(updated)

                Log.d("CharacterViewModel", "Personaje agregado exitosamente. Total: ${updated.size}")

            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error agregando personaje: ${e.message}", e)
                _error.value = "Error al guardar personaje: ${e.message}"

                // Revertir cambio si falla
                loadCharacters()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeCharacter(characterId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                Log.d("CharacterViewModel", "Eliminando personaje: $characterId")

                val updated = _characters.value.filter { it.id != characterId }
                _characters.value = updated

                repository.deleteCharacter(characterId)
                repository.save(updated)

                Log.d("CharacterViewModel", "Personaje eliminado. Total: ${updated.size}")

            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error eliminando personaje: ${e.message}", e)
                _error.value = "Error al eliminar personaje: ${e.message}"

                // Recargar en caso de error
                loadCharacters()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Sincroniza personajes locales con Firebase (llamar después de login)
     */
    fun syncWithFirebase() {
        viewModelScope.launch {
            try {
                Log.d("CharacterViewModel", "Sincronizando con Firebase...")
                repository.syncLocalToFirebase()
                loadCharacters() // Recargar después de sincronizar
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error sincronizando: ${e.message}", e)
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}