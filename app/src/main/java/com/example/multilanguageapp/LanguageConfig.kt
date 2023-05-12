package com.example.multilanguageapp

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

class LanguageConfig {

    companion object {
        fun changeLanguage(context: Context, languageCode: String): ContextWrapper {
            var context1: Context = context
            val resources: Resources = context1.resources
            val configuration: Configuration = resources.configuration
            val systemLocale: Locale =
                configuration.locales.get(0)
            if (languageCode != "" && !systemLocale.language.equals(languageCode)) {
                val locale = Locale(languageCode)
                Locale.setDefault(locale)
                configuration.setLocale(locale)
                context1 = context1.createConfigurationContext(configuration)
            }
            return ContextWrapper(context1)
        }
    }
}