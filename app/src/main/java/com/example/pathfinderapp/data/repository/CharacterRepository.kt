package com.example.pathfinderapp.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pathfinderapp.data.models.CharacterProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

private val Context.dataStore by preferencesDataStore(name = "characters_prefs")

class CharacterRepository(private val context: Context) {

    private val CHARACTERS_JSON = stringPreferencesKey("characters_json")
    private val gson = Gson()
    private val listType = object : TypeToken<List<CharacterProfile>>() {}.type

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Carga personajes desde Firebase si hay usuario autenticado,
     * sino carga desde DataStore local
     */
    suspend fun load(): List<CharacterProfile> {
        val currentUser = auth.currentUser

        return if (currentUser != null) {
            // Cargar desde Firebase
            loadFromFirebase(currentUser.uid)
        } else {
            // Cargar desde DataStore local
            loadFromLocal()
        }
    }

    /**
     * Guarda personajes en Firebase si hay usuario autenticado,
     * sino guarda en DataStore local
     */
    suspend fun save(list: List<CharacterProfile>) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Guardar en Firebase
            saveToFirebase(currentUser.uid, list)
        } else {
            // Guardar en local
            saveToLocal(list)
        }
    }

    /**
     * Carga personajes desde Firebase Firestore
     */
    private suspend fun loadFromFirebase(userId: String): List<CharacterProfile> {
        return try {
            Log.d("CharacterRepository", "Cargando personajes desde Firebase para usuario: $userId")

            val snapshot = firestore
                .collection("users")
                .document(userId)
                .collection("characters")
                .get()
                .await()

            val characters = snapshot.documents.mapNotNull { doc ->
                try {
                    val json = doc.getString("data") ?: return@mapNotNull null
                    gson.fromJson(json, CharacterProfile::class.java)
                } catch (e: Exception) {
                    Log.e("CharacterRepository", "Error parseando personaje ${doc.id}: ${e.message}")
                    null
                }
            }

            Log.d("CharacterRepository", "Personajes cargados: ${characters.size}")
            characters

        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error cargando desde Firebase: ${e.message}", e)
            // Si falla Firebase, intentar cargar desde local como backup
            loadFromLocal()
        }
    }

    /**
     * Guarda personajes en Firebase Firestore
     */
    private suspend fun saveToFirebase(userId: String, list: List<CharacterProfile>) {
        try {
            Log.d("CharacterRepository", "Guardando ${list.size} personajes en Firebase")

            val batch = firestore.batch()
            val userCharactersRef = firestore
                .collection("users")
                .document(userId)
                .collection("characters")

            // Eliminar personajes existentes
            val existingDocs = userCharactersRef.get().await()
            existingDocs.documents.forEach { doc ->
                batch.delete(doc.reference)
            }

            // Agregar nuevos personajes
            list.forEach { character ->
                val docRef = userCharactersRef.document(character.id)
                val data = hashMapOf(
                    "data" to gson.toJson(character),
                    "name" to character.name,
                    "race" to character.race.name,
                    "class" to character.characterClass.name,
                    "level" to character.level,
                    "timestamp" to System.currentTimeMillis()
                )
                batch.set(docRef, data)
            }

            batch.commit().await()
            Log.d("CharacterRepository", "Personajes guardados exitosamente en Firebase")

            // También guardar en local como backup
            saveToLocal(list)

        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error guardando en Firebase: ${e.message}", e)
            // Si falla Firebase, al menos guardar en local
            saveToLocal(list)
        }
    }

    /**
     * Carga personajes desde DataStore local
     */
    private suspend fun loadFromLocal(): List<CharacterProfile> {
        val prefs = context.dataStore.data.first()
        val raw = prefs[CHARACTERS_JSON] ?: "[]"
        return runCatching { gson.fromJson<List<CharacterProfile>>(raw, listType) }
            .getOrElse {
                Log.e("CharacterRepository", "Error cargando desde local")
                emptyList()
            }
    }

    /**
     * Guarda personajes en DataStore local
     */
    private suspend fun saveToLocal(list: List<CharacterProfile>) {
        val json = gson.toJson(list, listType)
        context.dataStore.edit { it[CHARACTERS_JSON] = json }
        Log.d("CharacterRepository", "Personajes guardados en local como backup")
    }

    /**
     * Sincroniza personajes locales con Firebase al iniciar sesión
     */
    suspend fun syncLocalToFirebase() {
        val currentUser = auth.currentUser ?: return

        try {
            val localCharacters = loadFromLocal()
            if (localCharacters.isNotEmpty()) {
                Log.d("CharacterRepository", "Sincronizando ${localCharacters.size} personajes locales a Firebase")
                saveToFirebase(currentUser.uid, localCharacters)
            }
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error sincronizando: ${e.message}", e)
        }
    }

    /**
     * Elimina un personaje específico
     */
    suspend fun deleteCharacter(characterId: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            try {
                firestore
                    .collection("users")
                    .document(currentUser.uid)
                    .collection("characters")
                    .document(characterId)
                    .delete()
                    .await()

                Log.d("CharacterRepository", "Personaje $characterId eliminado de Firebase")
            } catch (e: Exception) {
                Log.e("CharacterRepository", "Error eliminando personaje: ${e.message}")
            }
        }

        // También eliminar de local
        val characters = loadFromLocal().filter { it.id != characterId }
        saveToLocal(characters)
    }
}