package com.example.multilanguageapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import com.example.multilanguageapp.databinding.FragmentChangeLangDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangeLangDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentChangeLangDialogBinding

    var selectedLang = ""
    var selectedLangCode = ""
    private lateinit var listener: LanguageSelectionListener
    fun setListener(listener: LanguageSelectionListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChangeLangDialogBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        when (selectedLang) {
            resources.getString(R.string.english) -> {
                binding.englishRadioBtn.isChecked = true
            }

            resources.getString(R.string.hindi) -> {
                binding.hindiRadioBtn.isChecked = true
            }

            resources.getString(R.string.marathi) -> {
                binding.marathiRadioBtn.isChecked = true
            }
        }
        binding.langRadioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.englishRadioBtn -> {
                    selectedLang = resources.getString(R.string.english)
                    selectedLangCode = "en"
                }

                R.id.hindiRadioBtn -> {
                    selectedLang = resources.getString(R.string.hindi)
                    selectedLangCode = "hi"
                }

                R.id.marathiRadioBtn -> {
                    selectedLang = resources.getString(R.string.marathi)
                    selectedLangCode = "mr"
                }
            }
        }

        binding.submitBtn.setOnClickListener {
            listener.onLanguageChanged(selectedLang,selectedLangCode)
        }

    }

    companion object{
         const val TAG = "ChangeLangDialogFragmen"
    }

    interface LanguageSelectionListener {
        fun onLanguageChanged(selectedLang: String,selectedLangCode: String)
    }
}