package com.example.pathfinderapp.data.repository

import com.example.pathfinderapp.data.api.DndApiService
import com.example.pathfinderapp.data.models.Spell
import com.example.pathfinderapp.data.models.SpellDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpellRepository(private val apiService: DndApiService) {


    suspend fun getSpells(): Result<List<Spell>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSpells()
                Result.success(response.results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getSpellDetail(index: String): Result<SpellDetail> {
        return withContext(Dispatchers.IO) {
            try {
                val spell = apiService.getSpellDetail(index)
                Result.success(spell)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


    suspend fun searchSpells(query: String): Result<List<Spell>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSpells()
                val filtered = response.results.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                Result.success(filtered)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


    suspend fun getSpellsByLevel(level: Int): Result<List<SpellDetail>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSpells()
                val spellDetails = response.results.map { spell ->
                    apiService.getSpellDetail(spell.index)
                }
                val filtered = spellDetails.filter { it.level == level }
                Result.success(filtered)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: SpellRepository? = null

        fun getInstance(apiService: DndApiService): SpellRepository {
            return instance ?: synchronized(this) {
                instance ?: SpellRepository(apiService).also { instance = it }
            }
        }
    }
}
