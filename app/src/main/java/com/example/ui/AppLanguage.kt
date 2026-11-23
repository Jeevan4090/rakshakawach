package com.example.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLang(val label: String, val shortCode: String) {
    ENGLISH("English", "EN"),
    HINDI("हिन्दी", "हिं"),
    BENGALI("বাংলা", "বাং")
}

object AppLanguage {
    private const val PREFS = "raksha_prefs"
    private const val KEY = "app_lang"
    private var appContext: Context? = null

    var current by mutableStateOf(AppLang.ENGLISH)
        private set

    val isHindi: Boolean get() = current == AppLang.HINDI
    val isBengali: Boolean get() = current == AppLang.BENGALI

    fun init(context: Context) {
        appContext = context.applicationContext
        val saved = appContext
            ?.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            ?.getString(KEY, null)
        current = AppLang.values().firstOrNull { it.name == saved } ?: AppLang.ENGLISH
    }

    fun set(lang: AppLang) {
        current = lang
        appContext
            ?.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            ?.edit()
            ?.putString(KEY, lang.name)
            ?.apply()
    }

    fun cycle() {
        set(
            when (current) {
                AppLang.ENGLISH -> AppLang.HINDI
                AppLang.HINDI -> AppLang.BENGALI
                AppLang.BENGALI -> AppLang.ENGLISH
            }
        )
    }
}

fun tr(en: String, hi: String, bn: String = en): String = when (AppLanguage.current) {
    AppLang.ENGLISH -> en
    AppLang.HINDI -> hi
    AppLang.BENGALI -> bn
}