package com.example.projekatv2.data

import android.net.Uri
import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportUser
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun register(fullName: String, phoneNumber: String, profileImage: Uri, email: String, password: String): Resource<FirebaseUser>
    suspend fun getUserData(): Resource<SportUser>
    suspend fun getAllUserData(): Resource<List<SportUser>>
    suspend fun getUserSports(userId: String): Resource<List<Sport>>
    fun logout()
}
