package com.example.pathfinderapp.data.api

import com.example.pathfinderapp.data.models.Monster
import com.example.pathfinderapp.data.models.MonsterDetail
import com.example.pathfinderapp.data.models.MonsterListResponse
import com.example.pathfinderapp.data.models.Spell
import com.example.pathfinderapp.data.models.SpellDetail
import com.example.pathfinderapp.data.models.SpellListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DndApiService {

    // Endpoints de Monstruos
    @GET("monsters")
    suspend fun getMonsters(): MonsterListResponse

    @GET("monsters/{index}")
    suspend fun getMonsterDetail(@Path("index") index: String): MonsterDetail

    // Endpoints de Hechizos
    @GET("spells")
    suspend fun getSpells(): SpellListResponse

    @GET("spells/{index}")
    suspend fun getSpellDetail(@Path("index") index: String): SpellDetail

    companion object {
        private const val BASE_URL = "https://www.dnd5eapi.co/api/"

        fun create(): DndApiService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DndApiService::class.java)
        }
    }
}
