package com.example.carlog.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class AuthRepository (private val auth: FirebaseAuth = Firebase.auth){

    // kullanıcı kaydı kulanıcı girişi kullanıcı çıkış
    suspend fun registerUser(email: String, password: String) : Result<String>{
        return  try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Kayıt sırasında bir hata oluştu.")
            Result.success(userId)
        }catch (e: Exception){
            Result.failure(e)
        }

    }

    suspend fun loginUser(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Giriş yapılamadı.")
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun logoutUser(){
        auth.signOut()
    }
}
