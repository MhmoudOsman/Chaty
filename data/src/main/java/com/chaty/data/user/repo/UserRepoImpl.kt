package com.chaty.data.user.repo

import android.content.Context
import android.net.Uri
import com.chaty.data.user.network.dto.UserDto
import com.chaty.domain.user.models.UserModel
import com.chaty.domain.user.repo.UserRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepoImpl(private val context: Context, private val dispatcher: CoroutineDispatcher) :
    UserRepo {

    private val db = Firebase.firestore.collection("Users")
    override suspend fun getUser(): UserModel = withContext(dispatcher) {
        Firebase.auth.currentUser?.let { user ->
            val dto = db.document(user.uid).get().await().toObject(UserDto::class.java)
            dto?.let {
                return@withContext it.asModel()
            } ?: UserDto().asModel()
        } ?: throw NullPointerException("No User Logged")
    }

    override suspend fun saveUser(
        name: String,
        info: String,
        image: String,
        phone: String
    ): Boolean = withContext(dispatcher) {
        Firebase.auth.currentUser?.let { user ->
            val dto = UserDto(
                id = user.uid,
                name = name,
                info = info,
                image = image,
                token = "",
                phone = phone
            )
            val result = db.document(user.uid).set(dto)
            result.await()
            result.isSuccessful
        } ?: throw NullPointerException("No User Logged")
    }

    override suspend fun uploadProfilePhoto(image: Uri): String = withContext(dispatcher) {
        Firebase.auth.currentUser?.let { user->
            val ref = Firebase.storage.reference.child("${user.uid}/ProfilePic.jpg")
            ref.putFile(image).await().storage.downloadUrl.await().toString()
        }?: throw NullPointerException("No User Logged")
    }

}