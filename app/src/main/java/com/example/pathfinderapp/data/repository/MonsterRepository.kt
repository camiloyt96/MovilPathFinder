package com.example.pathfinderapp.data.repository

import com.example.pathfinderapp.data.api.DndApiService
import com.example.pathfinderapp.data.models.Monster
import com.example.pathfinderapp.data.models.MonsterDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MonsterRepository(private val apiService: DndApiService) {

    /**
     * Obtiene la lista completa de monstruos
     */
    suspend fun getMonsters(): Result<List<Monster>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMonsters()
                Result.success(response.results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene el detalle de un monstruo específico
     */
    suspend fun getMonsterDetail(index: String): Result<MonsterDetail> {
        return withContext(Dispatchers.IO) {
            try {
                val monster = apiService.getMonsterDetail(index)
                Result.success(monster)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Busca monstruos por nombre
     */
    suspend fun searchMonsters(query: String): Result<List<Monster>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMonsters()
                val filtered = response.results.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                Result.success(filtered)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: MonsterRepository? = null

        fun getInstance(apiService: DndApiService): MonsterRepository {
            return instance ?: synchronized(this) {
                instance ?: MonsterRepository(apiService).also { instance = it }
            }
        }
    }
}