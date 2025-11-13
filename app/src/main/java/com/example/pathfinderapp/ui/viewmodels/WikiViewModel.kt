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

    // Cache de detalles de hechizos para filtrado por nivel
    private val spellDetailsCache = mutableMapOf<String, SpellDetail>()

    init {
        loadSpells()
    }


    //Carga la lista de hechizos

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
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al cargar hechizos: ${exception.message}"
                    )
                }
        }
    }


    //Busca hechizos por nombre

    fun searchSpells(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }


    //Filtra hechizos por nivel
    //@param level Nivel del hechizo (0 para cantrips, 1-9 para niveles, null para todos)

    fun filterByLevel(level: Int?) {
        _uiState.value = _uiState.value.copy(selectedLevel = level)

        if (level == null) {
            // Mostrar todos los hechizos
            applyFilters()
        } else {
            // Cargar detalles para filtrar por nivel
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val spellsToFilter = _uiState.value.spells
                val detailedSpells = mutableListOf<Spell>()

                spellsToFilter.forEach { spell ->
                    // Usar caché si existe
                    val detail = spellDetailsCache[spell.index] ?: run {
                        repository.getSpellDetail(spell.index)
                            .getOrNull()
                            ?.also { spellDetailsCache[spell.index] = it }
                    }

                    if (detail?.level == level) {
                        detailedSpells.add(spell)
                    }
                }

                _uiState.value = _uiState.value.copy(
                    filteredSpells = detailedSpells,
                    isLoading = false
                )

                applyFilters()
            }
        }
    }


    //Aplica búsqueda y filtros combinados

    private fun applyFilters() {
        val query = _uiState.value.searchQuery
        val level = _uiState.value.selectedLevel

        var filtered = _uiState.value.spells

        // Filtrar por búsqueda
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        // Si hay un nivel seleccionado y tenemos caché, filtrar por nivel
        if (level != null) {
            filtered = filtered.filter { spell ->
                spellDetailsCache[spell.index]?.level == level
            }
        }

        _uiState.value = _uiState.value.copy(filteredSpells = filtered)
    }


    //Carga el detalle de un hechizo específico

    fun loadSpellDetail(index: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Usar caché si existe
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

    //Limpia el hechizo seleccionado

    fun clearSelectedSpell() {
        _uiState.value = _uiState.value.copy(selectedSpell = null)
    }


    //Limpia el error

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }


    //Limpia todos los filtros

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedLevel = null,
            filteredSpells = _uiState.value.spells
        )
    }
}
