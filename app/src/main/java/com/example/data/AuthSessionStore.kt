package com.example.data

import android.content.Context

data class SavedUser(
    val email: String,
    val displayName: String,
    val idToken: String
)

class AuthSessionStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("raksha_auth_prefs", Context.MODE_PRIVATE)

    fun saveUser(email: String, displayName: String, idToken: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NAME, displayName)
            .putString(KEY_TOKEN, idToken)
            .apply()
    }

    fun getSavedUser(): SavedUser? {
        if (!prefs.getBoolean(KEY_LOGGED_IN, false)) return null
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val name = prefs.getString(KEY_NAME, "") ?: ""
        val token = prefs.getString(KEY_TOKEN, "") ?: ""
        return SavedUser(email, name, token)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_LOGGED_IN = "is_logged_in"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_NAME = "user_name"
        private const val KEY_TOKEN = "user_id_token"
    }
}