package com.chaty.app.tools

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class CacheHelper(applicationContext: Context) {
    /**
     * Name of the SharedPreferences
     */
    companion object {
        private const val PREFERENCE_NAME = "com.arrowscars.safer"
        const val PREFERENCE_USER_LOGGED = "$PREFERENCE_NAME.UserLogged"
        const val PREFERENCE_LANGUAGE = "$PREFERENCE_NAME.Language"
        const val NOT_FIRST_TIME = "$PREFERENCE_NAME.NotFirstTime"
        const val PREFERENCE_THEME = "$PREFERENCE_NAME.Theme"

        @Volatile
        private var cashHelper: CacheHelper? = null

        fun getInstance(applicationContext: Context): CacheHelper {
            synchronized(this) {
                if (cashHelper == null) cashHelper = CacheHelper(applicationContext)
                return cashHelper as CacheHelper
            }
        }

    }

    /**
     * Singleton Instance of this class.
     * This will be the only instance of this class
     */

    /**
     * Gson instance to serialize and deserialize objects
     */
    private var gson: Gson = Gson()
    private lateinit var sharedPreferences: SharedPreferences

    /**
     * Private Constructor
     */
    init {
        initializeSharedPreference(applicationContext)
    }

    /**
     * method to initialize the shared preference
     *
     * @param applicationContext the application context
     */
    private fun initializeSharedPreference(applicationContext: Context) {
        sharedPreferences =
            applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Method to save objects into shared preference.
     * The object is serialize into json and is stored in shared preference.
     *
     * @param key   the key against which data needs to be stored
     * @param value the object to be saved
     * @return true is object was saved successfully, false if object could not be saved
     */
    fun <T> saveObject(key: String?, value: T): Boolean {
        return sharedPreferences.edit().putString(key, gson.toJson(value)).commit()
    }

    /**
     * Fetched the object stored against the key from shared preference.
     * If object is not available, then null will be returned
     *
     * @param key         the key against which object needs to be fetched
     * @param targetClass the Class to which the object belongs to
     * @return the object stored in the shared preference
     */
    fun <T> fetchObject(key: String?, targetClass: Class<T>): T? {

        val storedJasonData: String? = sharedPreferences.getString(key, null)
        var storedData: T? = null
        if (storedJasonData != null) storedData =
            gson.fromJson(storedJasonData as String?, targetClass)
        return storedData
    }

    /**
     * Method to store String data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    fun saveString(key: String?, value: String?): Boolean {
        return sharedPreferences.edit().putString(key, value).commit()
    }

    /**
     * method to fetch String data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    fun fetchString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun fetchString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    /**
     * Method to store Integer data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    fun saveInteger(key: String?, value: Int?): Boolean {
        return sharedPreferences.edit().putInt(key, value!!).commit()
    }

    /**
     * method to fetch Integer data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    fun fetchInteger(key: String?): Int {
        return sharedPreferences.getInt(key, Int.MIN_VALUE)
    }

    fun fetchInteger(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    /**
     * Method to store Long data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    fun saveLong(key: String?, value: Long?): Boolean {
        return sharedPreferences.edit().putLong(key, value!!).commit()
    }

    /**
     * method to fetch Long data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    fun fetchLong(key: String?): Long {
        return sharedPreferences.getLong(key, Long.MIN_VALUE)
    }

    /**
     * Method to store Boolean data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    fun saveBoolean(key: String?, value: Boolean?): Boolean {
        return sharedPreferences.edit().putBoolean(key, value!!).commit()
    }

    /**
     * method to fetch Boolean data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    fun fetchBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    /**
     * Method to store Float data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    fun saveFloat(key: String?, value: Float?): Boolean {
        return sharedPreferences.edit().putFloat(key, value!!).commit()
    }

    /**
     * method to fetch Float data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    fun fetchFloat(key: String?): Float {
        return sharedPreferences.getFloat(key, Float.MIN_VALUE)
    }

    /**
     * Removes the data present in the shared preference against the specified key
     *
     * @param key the key against which data needs to be removed
     * @return true if data was removed successfully, otherwise returns false
     */
    fun removeData(key: String?): Boolean {
        return sharedPreferences.edit().remove(key).commit()
    }

    /**
     * Method to check whether there is any data stored against the specified key
     *
     * @param key the key to be checked
     * @return true if some data is present against the specified key, otherwise returns false
     */
    fun contains(key: String?): Boolean {
        return sharedPreferences.contains(key)
    }

    /**
     * Method to remove all the data stored in shared preference.
     * Using apply instead of commit here since
     * apply perform the operations in background while commit perform synchronously.
     *
     *
     * If there are lots of data in shared preference using apply is better
     * since deletion will be performed in background
     */
    fun removeAll() {
        sharedPreferences.edit().clear().apply()
    }
}