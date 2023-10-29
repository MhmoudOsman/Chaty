package com.chaty.app.tools

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import java.util.Locale


fun setLocale(context: Context, language: String): ContextWrapper {
    // updating the language for devices above android nougat
    return ContextWrapper(updateResources(context, language))
}

fun getDefaultLanguage(): String {
    val language = Locale.getDefault().language
    return if (language == Language.AR) {
        language
    }else{
        Language.EN
    }
}


private fun updateResources(context: Context, language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val configuration = context.resources.configuration
    configuration.setLocale(locale)
    val localeList = LocaleList(locale)
    LocaleList.setDefault(localeList)
    configuration.setLocales(localeList)
    configuration.setLayoutDirection(locale)
    return context.createConfigurationContext(configuration)
}
