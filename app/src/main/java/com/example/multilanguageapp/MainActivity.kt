package com.example.multilanguageapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.multilanguageapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ChangeLangDialogFragment.LanguageSelectionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogFragment: ChangeLangDialogFragment
    private var selectedLang = ""
    private var selectedLangCode = ""
    private lateinit var settingsManager: SettingsManager

    override fun attachBaseContext(newBase: Context?) {

        newBase?.let { context ->
            settingsManager = SettingsManager(newBase.applicationContext)
            val appLanguage = settingsManager.currentLanguage
            selectedLang = appLanguage.selectedLang
            selectedLangCode = appLanguage.selectedLangCode
            LanguageConfig.changeLanguage(context, appLanguage.selectedLangCode)
        }
        super.attachBaseContext(newBase)

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

        binding.nextIv.setOnClickListener {
            startActivity(Intent(this , ItemActivity::class.java))
        }

        observeLanguageChanges()
    }

    private fun observeLanguageChanges() {
        lifecycleScope.launch {
            settingsManager.observeLanguageChanges()
                .collect {

                    if (selectedLangCode != it.selectedLangCode)
                        restartActivity()
                }
        }
    }

    override fun onLanguageChanged(selectedLang: String, selectedLangCode: String) {
        if (dialogFragment.isAdded)
            dialogFragment.dismiss()

        if (this.selectedLangCode != selectedLangCode) {

            lifecycleScope.launch {

                settingsManager.savePreferredLanguage(AppLanguage(selectedLang, selectedLangCode))

            }

        }

    }

    private fun restartActivity() {

        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)

    }
}