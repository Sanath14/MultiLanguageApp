package com.example.multilanguageapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "language_preference"
)

class SettingsManager(private val context: Context) {


    companion object {
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
        val SELECTED_LANGUAGE_CODE = stringPreferencesKey("selected_language_code")
    }

    suspend fun savePreferredLanguage(appLanguage: AppLanguage) {
        context.applicationContext.dataStore.edit { preferences ->
            preferences[SELECTED_LANGUAGE] = appLanguage.selectedLang
            preferences[SELECTED_LANGUAGE_CODE] = appLanguage.selectedLangCode
        }
    }

    // We use the map operator to transform the preferences into an AppLanguage object
    private val languageFlow: Flow<AppLanguage> = context.applicationContext.dataStore.data
        .map { preferences ->
            AppLanguage(
                preferences[SELECTED_LANGUAGE] ?: "English",
                preferences[SELECTED_LANGUAGE_CODE] ?: "en"
            )
        }

    //. runBlocking suspends the coroutine until the first value is emitted by the flow and returns that value.
    val currentLanguage: AppLanguage
        get() = runBlocking { languageFlow.first() }

    // This allows external components to observe language changes by collecting values from this flow
    fun observeLanguageChanges(): Flow<AppLanguage> {
        return languageFlow
    }

}

data class AppLanguage(val selectedLang: String, val selectedLangCode: String)