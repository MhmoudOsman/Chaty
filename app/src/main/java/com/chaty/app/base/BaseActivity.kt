package com.chaty.app.base

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.chaty.app.tools.Language
import com.chaty.app.tools.setLocale
import com.chaty.data.tools.CacheHelper
import com.chaty.data.tools.CacheHelper.Companion.NOT_FIRST_TIME
import com.chaty.data.tools.CacheHelper.Companion.PREFERENCE_LANGUAGE
import com.chaty.data.tools.CacheHelper.Companion.PREFERENCE_THEME
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {


    override fun attachBaseContext(newBase: Context) {
        val cacheHelper = CacheHelper.getInstance(newBase.applicationContext)
        if (!cacheHelper.fetchBoolean(NOT_FIRST_TIME)) {
            cacheHelper.saveString(PREFERENCE_LANGUAGE, Language.AR)
            cacheHelper.saveBoolean(NOT_FIRST_TIME, true)
        }
        AppCompatDelegate.setDefaultNightMode(
            cacheHelper.fetchInteger(
                PREFERENCE_THEME,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        )
        super.attachBaseContext(
            setLocale(
                newBase,
                cacheHelper.fetchString(PREFERENCE_LANGUAGE, Language.AR)
            )
        )
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun refreshActivity() {
        startActivity(intent)
        finish()
    }

}