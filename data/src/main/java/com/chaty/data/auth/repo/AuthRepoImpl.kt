package com.chaty.data.auth.repo

import android.content.Context
import com.chaty.data.tools.buildWithCallbacks
import com.chaty.data.tools.CacheHelper
import com.chaty.domain.auth.models.PhoneAuthOptionsModel
import com.chaty.domain.auth.repo.AuthRepo
import com.chaty.domain.auth.state.PhoneAuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class AuthRepoImpl(private val context: Context, private val dispatcher: CoroutineDispatcher) : AuthRepo {

    private val auth = Firebase.auth

    override suspend fun login(credential: PhoneAuthCredential): Boolean = withContext(dispatcher) {
        auth.signInWithCredential(credential).await().user != null
    }

    override suspend fun login(verificationId: String, code: String): Boolean =
        withContext(dispatcher) {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            auth.signInWithCredential(credential).await().user != null
        }

    override suspend fun logout(): Boolean = withContext(dispatcher){
        auth.signOut()
        CacheHelper.getInstance(context).saveBoolean(CacheHelper.PREFERENCE_USER_LOGGED,false)
    }

    override suspend fun sendOtp(optionsModel: PhoneAuthOptionsModel): Flow<PhoneAuthResult> =
        withContext(dispatcher) {
            callbackFlow {
                trySend(PhoneAuthResult.Loading)
                val builder = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(optionsModel.phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(optionsModel.activity)

                optionsModel.token?.let { builder.setForceResendingToken(it) }

                val options = builder.buildWithCallbacks {
                    trySend(it)
                }
                PhoneAuthProvider.verifyPhoneNumber(options)
                awaitClose { }
            }

        }
}