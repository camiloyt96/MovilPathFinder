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
    val searchQuery: String = ""
)

class BestiaryViewModel : ViewModel() {

    private val repository = MonsterRepository.getInstance(DndApiService.create())

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


    //Limpia el monstruo seleccionado

    fun clearSelectedMonster() {
        _uiState.value = _uiState.value.copy(selectedMonster = null)
    }


    //Limpia el error

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}