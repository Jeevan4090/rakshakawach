package com.example.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppLanguage {
    private const val PREFS_NAME = "raksha_lang_prefs"
    private const val KEY_IS_HINDI = "is_hindi"

    var isHindi by mutableStateOf(false)
        private set

    fun init(context: Context) {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isHindi = prefs.getBoolean(KEY_IS_HINDI, false)
    }

    fun toggle(context: Context) {
        isHindi = !isHindi
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_IS_HINDI, isHindi)
            .apply()
    }
}

fun tr(en: String, hi: String): String = if (AppLanguage.isHindi) hi else en