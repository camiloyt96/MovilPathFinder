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
        _characters.value = emptyList()

        // TODO: Implementar carga desde Firebase
    }

    fun addCharacter(character: CharacterProfile) {
        viewModelScope.launch {
            val currentList = _characters.value.toMutableList()

            if (currentList.size < 5) {
                currentList.add(character)
                _characters.value = currentList

                saveCharacters(currentList)
            }
        }
    }

    fun removeCharacter(characterId: String) {
        viewModelScope.launch {
            val currentList = _characters.value.toMutableList()
            currentList.removeAll { it.id == characterId }
            _characters.value = currentList

            saveCharacters(currentList)
        }
    }

    private fun saveCharacters(characters: List<CharacterProfile>) {
        // TODO: Implementar guardado en Firebase
    }
}