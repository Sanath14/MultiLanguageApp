package com.example.multilanguageapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    val getPreferredLanguage: Flow<AppLanguage> = context.applicationContext.dataStore.data
        .map { preferences ->
            AppLanguage(
                preferences[SELECTED_LANGUAGE] ?: "English",
                preferences[SELECTED_LANGUAGE_CODE] ?: "en"
            )
        }

}

data class AppLanguage(val selectedLang: String, val selectedLangCode: String)