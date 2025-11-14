package com.example.pathfinderapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinderapp.data.api.DndApiService
import com.example.pathfinderapp.data.models.Monster
import com.example.pathfinderapp.data.models.MonsterDetail
import com.example.pathfinderapp.data.repository.MonsterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BestiaryUiState(
    val monsters: List<Monster> = emptyList(),
    val filteredMonsters: List<Monster> = emptyList(),
    val selectedMonster: MonsterDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedSize: String? = null,
    val selectedType: String? = null,
    val selectedChallengeRange: ClosedFloatingPointRange<Double>? = null
)


class BestiaryViewModel : ViewModel() {

    private val repository = MonsterRepository.getInstance(DndApiService.create())

    private val monsterDetailsCache = mutableMapOf<String, MonsterDetail>()

    private val _uiState = MutableStateFlow(BestiaryUiState())
    val uiState: StateFlow<BestiaryUiState> = _uiState.asStateFlow()

    init {
        loadMonsters()
    }


    //Carga la lista de monstruos

    fun loadMonsters() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getMonsters()
                .onSuccess { monsters ->
                    _uiState.value = _uiState.value.copy(
                        monsters = monsters,
                        filteredMonsters = monsters,
                        isLoading = false
                    )

                    // Aquí se llama a precargar detalles
                    preloadMonsterDetails(monsters)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al cargar monstruos: ${exception.message}"
                    )
                }
        }
    }



    //Busca monstruos por nombre

    fun searchMonsters(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        val filtered = if (query.isEmpty()) {
            _uiState.value.monsters
        } else {
            _uiState.value.monsters.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = _uiState.value.copy(filteredMonsters = filtered)
    }


    //Carga el detalle de un monstruo específico

    fun loadMonsterDetail(index: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getMonsterDetail(index)
                .onSuccess { monster ->
                    _uiState.value = _uiState.value.copy(
                        selectedMonster = monster,
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

    private fun applyFilters() {
        var filtered = _uiState.value.monsters

        // Filtro de búsqueda
        val query = _uiState.value.searchQuery
        if (query.isNotEmpty()) {
            filtered = filtered.filter { it.name.contains(query, ignoreCase = true) }
        }

        // Filtrado por detalles usando cache
        filtered = filtered.filter { monster ->
            val detail = monsterDetailsCache[monster.index] ?: return@filter true

            val sizeMatch = _uiState.value.selectedSize?.let { it == detail.size } ?: true
            val typeMatch = _uiState.value.selectedType?.let { it.equals(detail.type, true) } ?: true
            val crMatch = _uiState.value.selectedChallengeRange?.let { detail.challengeRating in it } ?: true

            sizeMatch && typeMatch && crMatch
        }

        _uiState.value = _uiState.value.copy(filteredMonsters = filtered)
    }

    fun filterBySize(size: String?) {
        _uiState.value = _uiState.value.copy(selectedSize = size)
        applyFilters()
    }

    fun filterByType(type: String?) {
        _uiState.value = _uiState.value.copy(selectedType = type)
        applyFilters()
    }

    fun filterByChallengeRange(range: ClosedFloatingPointRange<Double>?) {
        _uiState.value = _uiState.value.copy(selectedChallengeRange = range)
        applyFilters()
    }
    private fun preloadMonsterDetails(monsters: List<Monster>) {
        viewModelScope.launch {
            monsters.forEach { monster ->
                if (!monsterDetailsCache.containsKey(monster.index)) {
                    repository.getMonsterDetail(monster.index)
                        .onSuccess { detail ->
                            monsterDetailsCache[monster.index] = detail
                        }
                }
            }
        }
    }




    //Limpia el monstruo seleccionado

    fun clearSelectedMonster() {
        _uiState.value = _uiState.value.copy(selectedMonster = null)
    }


    //Limpia el error

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
