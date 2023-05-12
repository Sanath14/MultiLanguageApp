package com.example.multilanguageapp

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.multilanguageapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), ChangeLangDialogFragment.LanguageSelectionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogFragment: ChangeLangDialogFragment
    private var selectedLang = ""
    private var selectedLangCode = ""
    private lateinit var settingsManager: SettingsManager

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            settingsManager = SettingsManager(newBase.applicationContext)
        }
        var appLanguage: AppLanguage
        var context: Context? = newBase

        lifecycleScope.launch(Dispatchers.Main) {

            appLanguage = settingsManager.getPreferredLanguage.first()
            selectedLang = appLanguage.selectedLang
            selectedLangCode = appLanguage.selectedLangCode
            context =
                newBase?.let { LanguageConfig.changeLanguage(it, appLanguage.selectedLangCode) }

        }

        super.attachBaseContext(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = resources.getString(R.string.welcome_txt)
        dialogFragment = ChangeLangDialogFragment()
        dialogFragment.setListener(this)

        binding.changeLangBtn.setOnClickListener {
            if (!dialogFragment.isAdded) {
                dialogFragment.selectedLang = selectedLang
                dialogFragment.selectedLangCode = selectedLangCode
                dialogFragment.show(supportFragmentManager, ChangeLangDialogFragment.TAG)
            }
        }
    }

    override fun onLanguageChanged(selectedLang: String, selectedLangCode: String) {
        if (dialogFragment.isAdded)
            dialogFragment.dismiss()

        if (this.selectedLangCode != selectedLangCode) {

//            GlobalScope.launch {
//                settingsManager.savePreferredLanguage(AppLanguage(selectedLang, selectedLangCode))
//
//            }
            restartActivity()
        }

    }

    private fun restartActivity() {


        //   lifecycleScope.launch {
        // delay(1500)

        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
        // }
    }
}