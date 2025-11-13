// auth/FirebaseAppAuthRepository.kt

package com.example.pathfinderapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAppAuthRepository : AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            userCredential.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Usuario Firebase no disponible después del registro."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            userCredential.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Usuario Firebase no disponible después del login."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout() {
        auth.signOut()
    }
}