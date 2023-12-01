package com.chaty.app.base

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.chaty.app.tools.getDefaultLanguage
import com.chaty.app.tools.setLocale
import com.chaty.data.tools.CacheHelper
import com.chaty.data.tools.CacheHelper.Companion.PREFERENCE_LANGUAGE
import com.chaty.data.tools.CacheHelper.Companion.PREFERENCE_THEME
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    lateinit var cacheHelper: CacheHelper

    override fun attachBaseContext(newBase: Context) {
        cacheHelper = CacheHelper.getInstance(newBase.applicationContext)
        AppCompatDelegate.setDefaultNightMode(
            cacheHelper.fetchInteger(
                PREFERENCE_THEME,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        )
        super.attachBaseContext(
            setLocale(
                newBase,
                cacheHelper.fetchString(PREFERENCE_LANGUAGE, getDefaultLanguage())
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