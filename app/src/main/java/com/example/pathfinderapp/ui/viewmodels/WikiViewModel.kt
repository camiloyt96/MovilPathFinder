package com.example.pathfinderapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinderapp.data.api.DndApiService
import com.example.pathfinderapp.data.models.Spell
import com.example.pathfinderapp.data.models.SpellDetail
import com.example.pathfinderapp.data.repository.SpellRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WikiUiState(
    val spells: List<Spell> = emptyList(),
    val filteredSpells: List<Spell> = emptyList(),
    val selectedSpell: SpellDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedLevel: Int? = null // null = todos los niveles
)

class WikiViewModel : ViewModel() {

    private val repository = SpellRepository.getInstance(DndApiService.create())

    private val _uiState = MutableStateFlow(WikiUiState())
    val uiState: StateFlow<WikiUiState> = _uiState.asStateFlow()

    private val spellDetailsCache = mutableMapOf<String, SpellDetail>()

    init {
        loadSpells()
    }

    fun loadSpells() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getSpells()
                .onSuccess { spells ->
                    _uiState.value = _uiState.value.copy(
                        spells = spells,
                        filteredSpells = spells,
                        isLoading = false
                    )

                    // Pre-cargar todos los detalles en cache
                    preloadSpellDetails(spells)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al cargar hechizos: ${exception.message}"
                    )
                }
        }
    }

    private fun preloadSpellDetails(spells: List<Spell>) {
        viewModelScope.launch {
            spells.forEach { spell ->
                if (!spellDetailsCache.containsKey(spell.index)) {
                    repository.getSpellDetail(spell.index)
                        .onSuccess { detail ->
                            spellDetailsCache[spell.index] = detail
                        }
                }
            }
        }
    }

    fun searchSpells(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun filterByLevel(level: Int?) {
        _uiState.value = _uiState.value.copy(selectedLevel = level)
        applyFilters()
    }

    private fun applyFilters() {
        val query = _uiState.value.searchQuery
        val level = _uiState.value.selectedLevel

        var filtered = _uiState.value.spells

        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        if (level != null) {
            filtered = filtered.filter { spell ->
                spellDetailsCache[spell.index]?.level == level
            }
        }

        _uiState.value = _uiState.value.copy(filteredSpells = filtered)
    }

    fun loadSpellDetail(index: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val cachedDetail = spellDetailsCache[index]
            if (cachedDetail != null) {
                _uiState.value = _uiState.value.copy(
                    selectedSpell = cachedDetail,
                    isLoading = false
                )
                return@launch
            }

            repository.getSpellDetail(index)
                .onSuccess { spell ->
                    spellDetailsCache[index] = spell
                    _uiState.value = _uiState.value.copy(
                        selectedSpell = spell,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al cargar detalle: ${exception.message}"
                    )
                }
        }
    }

    fun clearSelectedSpell() {
        _uiState.value = _uiState.value.copy(selectedSpell = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedLevel = null,
            filteredSpells = _uiState.value.spells
        )
    }
}

