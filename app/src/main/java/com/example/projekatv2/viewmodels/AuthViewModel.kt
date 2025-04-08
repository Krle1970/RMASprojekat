package com.example.projekatv2.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projekatv2.data.Resource
import com.example.projekatv2.data.AuthRepository
import com.example.projekatv2.data.AuthRepositoryImp
import com.example.projekatv2.model.SportUser
import com.example.projekatv2.model.Sport
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImp()

    // Flows for authentication, registration, and user data
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    private val _currentUserFlow = MutableStateFlow<Resource<SportUser>?>(null)
    val currentUserFlow: StateFlow<Resource<SportUser>?> = _currentUserFlow

    private val _allUsers = MutableStateFlow<Resource<List<SportUser>>?>(null)
    val allUsers: StateFlow<Resource<List<SportUser>>?> = _allUsers

    private val _userSports = MutableStateFlow<Resource<List<Sport>>?>(null)
    val userSports: StateFlow<Resource<List<Sport>>?> = _userSports

    // Current logged in Firebase user
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    // Fetch current user's data
    fun getUserData() = viewModelScope.launch {
        _currentUserFlow.value = Resource.Loading
        val result = repository.getUserData()
        _currentUserFlow.value = result
    }

    fun rateUser(userId: String, rating: Int) {
        // Primer kako biste mogli koristiti Firebase Firestore za ažuriranje ocene korisnika
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)

        // Ažuriranje polja "rating" u bazi
        userRef.update("rating", rating)
            .addOnSuccessListener {
                Log.d("rateUser", "Successfully updated user rating")
            }
            .addOnFailureListener { e ->
                Log.e("rateUser", "Error updating user rating", e)
            }
    }

    // Fetch all users' data
    fun getAllUserData() = viewModelScope.launch {
        _allUsers.value = Resource.Loading
        val result = repository.getAllUserData()
        _allUsers.value = result
    }

    // Fetch all sports associated with the user
    fun getUserSports(userId: String) = viewModelScope.launch {
        _userSports.value = Resource.Loading
        val result = repository.getUserSports(userId)
        _userSports.value = result
    }

    // Perform login
    fun login(email: String, password: String) = viewModelScope.launch {
        Log.d("AuthViewModel", "Login process started with email: $email")

        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)

        Log.d("AuthViewModel", "Login result: ${result.toString()}")
        _loginFlow.value = result

        if (result is Resource.Failure) {
            Log.d("AuthViewModel", "Login failed: ${result.exception.message}")
        } else if (result is Resource.Success) {
            Log.d("AuthViewModel", "Login successful: ${result.result?.email}")
        }
    }

    // Perform registration
    fun register(fullName: String, phoneNumber: String, profileImage: Uri, email: String, password: String) = viewModelScope.launch {
        Log.d("AuthViewModel", "Registration process started with email: $email")

        _registerFlow.value = Resource.Loading
        val result = repository.register(fullName, phoneNumber, profileImage, email, password)

        Log.d("AuthViewModel", "Registration result: ${result.toString()}")
        _registerFlow.value = result

        if (result is Resource.Failure) {
            Log.d("AuthViewModel", "Registration failed: ${result.exception.message}")
        } else if (result is Resource.Success) {
            Log.d("AuthViewModel", "Registration successful: ${result.result?.email}")
        }
    }

    // Logout current user
    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _registerFlow.value = null
        _currentUserFlow.value = null
        _userSports.value = null
    }
}

class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
