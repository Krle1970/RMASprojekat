package com.example.projekatv2.data

import android.net.Uri
import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.SportUser
import com.example.projekatv2.model.Sport
import com.example.projekatv2.service.DatabaseService
import com.example.projekatv2.service.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageInstance = FirebaseStorage.getInstance()

    private val databaseService = DatabaseService(firestoreInstance)
    private val storageService = StorageService(storageInstance)

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun register(
        fullName: String,
        phoneNumber: String,
        profileImage: Uri,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                val profilePictureUrl = storageService.uploadProfilePicture(result.user!!.uid, profileImage)
                val user = SportUser(
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    profileImage = profilePictureUrl
                )
                databaseService.saveUserData(result.user!!.uid, user)
            }
            Resource.Success(result.user!!)
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserData(): Resource<SportUser> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val uid = currentUser.uid

                val db = FirebaseFirestore.getInstance()

                val userDocRef = db.collection("users").document(uid)
                val userSnapshot = userDocRef.get().await()

                if (userSnapshot.exists()) {
                    val user = userSnapshot.toObject(SportUser::class.java)
                    if (user != null) {
                        Resource.Success(user)
                    } else {
                        Resource.Failure(Exception("Neuspešno mapiranje dokumenta na User"))
                    }
                } else {
                    Resource.Failure(Exception("Korisnikov dokument ne postoji"))
                }
            } else {
                Resource.Failure(Exception("Nema trenutnog korisnika"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAllUserData(): Resource<List<SportUser>> {
        return try {
            val db = FirebaseFirestore.getInstance()
            val usersCollectionRef = db.collection("users")
            val usersSnapshot = usersCollectionRef.get().await()

            if (!usersSnapshot.isEmpty) {
                val usersList = usersSnapshot.documents.mapNotNull { document ->
                    document.toObject(SportUser::class.java)
                }
                Resource.Success(usersList)
            } else {
                Resource.Failure(Exception("Nema korisnika u bazi podataka"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserSports(userId: String): Resource<List<Sport>> {
        return try {
            val sportsCollectionRef = firestoreInstance.collection("sports").whereEqualTo("userId", userId)
            val sportsSnapshot = sportsCollectionRef.get().await()

            if (!sportsSnapshot.isEmpty) {
                val sportsList = sportsSnapshot.documents.mapNotNull { document ->
                    document.toObject(Sport::class.java)
                }
                Resource.Success(sportsList)
            } else {
                Resource.Failure(Exception("Nema sportskih događaja za ovog korisnika"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
