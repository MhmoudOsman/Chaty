package com.chaty.app.messages_screen.state

import com.chaty.domain.auth.models.PhoneAuthOptionsModel

sealed class ContactUiIntent{
    data object Clear : ContactUiIntent()
    data object GetContacts : ContactUiIntent()

}
