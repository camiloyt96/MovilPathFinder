// auth/AuthRepository.kt

package com.example.pathfinderapp.data.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser>

    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser>

    fun getCurrentUser(): FirebaseUser?

    fun logout()
}