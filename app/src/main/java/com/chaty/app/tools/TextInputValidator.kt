package com.chaty.app.tools

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.chaty.app.R
import com.google.android.material.textfield.TextInputLayout


class TextInputValidator(
    private val textInputLayout: TextInputLayout,
    private val type: Int,
    private val errorMessage: String,
    private val password: TextInputLayout?
) : TextWatcher, View.OnFocusChangeListener {

    private var isValid: Boolean = false
    fun isValid() = isValid

    constructor(textInputLayout: TextInputLayout) : this(
        textInputLayout,
        CHECK_isEMPTY,
        textInputLayout.context.getString(R.string.error_required),
        null
    )

    constructor(textInputLayout: TextInputLayout, errorMessage: String) : this(
        textInputLayout, CHECK_isEMPTY, errorMessage, null
    )

    constructor(textInputLayout: TextInputLayout, type: Int) : this(
        textInputLayout, type, textInputLayout.context.getString(R.string.error_required), null
    )

    constructor(textInputLayout: TextInputLayout, type: Int, errorMessage: String) : this(
        textInputLayout, type, errorMessage, null
    )

    init {
        textInputLayout.editText?.addTextChangedListener(this@TextInputValidator)
        textInputLayout.editText?.onFocusChangeListener = this@TextInputValidator
    }

    fun validate() {
        val text = textInputLayout.editText?.text?.toString()
        when (type) {
            CHECK_isEMPTY -> {
                if (text.isNullOrBlank()) {
                    textInputLayout.error = errorMessage
                    isValid = false
                } else {
                    textInputLayout.error = null
                    isValid = true
                }
            }

            CHECK_EMAIL -> {
                if (text.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    textInputLayout.error = errorMessage
                    isValid = false
                } else {
                    textInputLayout.error = null
                    isValid = true
                }

            }

            CHECK_PASSWORD -> {
                if (text.isNullOrBlank() || text.length < 6) {
                    textInputLayout.error = errorMessage
                    isValid = false
                } else {
                    textInputLayout.error = null
                    isValid = true
                }

            }

            CHECK_CONFIRM_PASSWORD -> {
                val text2 = password?.editText?.text?.toString()
                if (text.isNullOrBlank() || text != text2) {
                    textInputLayout.error = errorMessage
                    isValid = false
                } else {
                    textInputLayout.error = null
                    isValid = true
                }

            }
        }

    }

    companion object {
        const val CHECK_isEMPTY = 0
        const val CHECK_EMAIL = 1
        const val CHECK_PASSWORD = 2
        const val CHECK_CONFIRM_PASSWORD = 3
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        validate()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        validate()

    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validate()
        }


    }
}