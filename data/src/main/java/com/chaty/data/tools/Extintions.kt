package com.chaty.data.tools

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.chaty.data.user.network.dto.UserDto
import com.chaty.domain.user.models.UserModel
import com.chaty.domain.auth.state.PhoneAuthResult
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun UserModel.asDTO(): UserDto = UserDto(
    id = this.id,
    name = this.name,
    info = this.info,
    image = this.image,
    token = this.token,
    phone = this.phone
)

fun PhoneAuthOptions.Builder.buildWithCallbacks(result: (PhoneAuthResult) -> Unit): PhoneAuthOptions {
    val callback =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                result(PhoneAuthResult.VerificationCompleted(credential))
            }

            override fun onVerificationFailed(e: FirebaseException) {
                result(PhoneAuthResult.VerificationFailed(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                result(PhoneAuthResult.CodeSent(verificationId, token))
            }
        }

    return setCallbacks(callback).build()
}
fun <T> Query.addSnapshotListenerFlow(
    dataType: Class<T>,
    callback: ((List<T>) -> Unit)? = null
): Flow<List<T>> = callbackFlow {
    val listener = EventListener<QuerySnapshot> { snapshot, exception ->
        if (exception != null) {
            val errorMessage = exception.message ?: "FirebaseException"
            cancel(errorMessage, exception)
        }
        if (snapshot != null) {
            val data = snapshot.toObjects(dataType)
            callback?.invoke(data)
            trySend(data)
        } else {
            trySend(emptyList())
        }
    }

    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
}