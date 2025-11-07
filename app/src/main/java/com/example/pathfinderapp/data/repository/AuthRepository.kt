// auth/AuthRepository.kt

package com.example.pathfinderapp.data.repository

import com.google.firebase.auth.FirebaseUser

// Interfaz para la capa de autenticación
interface AuthRepository {
    // Función para registrar un nuevo usuario con email y contraseña
    // Retorna un Result que contiene un FirebaseUser en caso de éxito, o una excepción en caso de fallo
    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>

    // Puedes añadir más funciones aquí, como login, resetear contraseña, etc.
    // suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>
    // suspend fun signOut(): Result<Unit>
}
