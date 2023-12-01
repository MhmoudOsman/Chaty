package com.chaty.domain.user.usecase

import android.content.Context
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import androidx.loader.content.CursorLoader
import com.chaty.domain.user.models.UserModel
import com.chaty.domain.user.repo.UserRepo

class GetContactsUseCase(
    private val repo: UserRepo, private val context: Context,
) {
    suspend operator fun invoke(): List<UserModel> {
        val data = repo.getUsers()
        val contacts = mutableSetOf<UserModel>()
        CursorLoader(
            context,
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        ).loadInBackground()
            ?.run {
                if (count > 0) {
                    while (moveToNext()) {
                        getStringOrNull(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            ?.let { phone ->
                                data.find { it.phone.removePrefix("+2") == phone.removePrefix("+2") }
                                    ?.let {
                                        val name = getStringOrNull(getColumnIndex(ContactsContract.Data.DISPLAY_NAME)) ?: phone
                                        contacts.add(it.copy(displayName = name))

                                    }
                            }
                    }
                }
                close()
            }
        return contacts.toList()
    }
}