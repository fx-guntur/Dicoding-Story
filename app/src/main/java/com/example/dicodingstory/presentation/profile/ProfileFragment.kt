package com.example.dicodingstory.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dicodingstory.base.BaseFragment
import com.example.dicodingstory.data.model.LanguageOption
import com.example.dicodingstory.data.model.ThemeOption
import com.example.dicodingstory.databinding.FragmentProfileBinding
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.utils.helper.observe
import com.example.dicodingstory.utils.helper.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val profileViewModel: ProfileViewModel by viewModel()

    private var currentTheme: ThemeOption = ThemeOption.DARK
    private var currentLanguage: LanguageOption = LanguageOption.ENGLISH

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        observe(profileViewModel.getCurrentTheme(), ::handleThemeChange)
        observe(profileViewModel.getCurrentLanguage(), ::handleLanguageChange)

        binding.apply {
            switch2.setOnCheckedChangeListener { _, isChecked ->
                applyTheme(isChecked)
            }

            logoutBtn.setOnClickListener {
                signOutProcess()
            }

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedLanguage = getSelectedLanguage(checkedId)
                if (selectedLanguage != currentLanguage) {
                    applyLanguage(selectedLanguage.toString())
                }
            }
        }
    }

    override fun initProcess() {}

    private fun signOutProcess() {
        observe(profileViewModel.signOut, ::onSignOutResult)
    }

    private fun onSignOutResult(result: ResultStatus<String>) {
        when (result) {
            is ResultStatus.Loading -> {
                showProgressBar(true)
            }

            is ResultStatus.Success -> {
                showProgressBar(false)
            }

            is ResultStatus.Error -> {
                binding.apply {
                    showProgressBar(false)
                    root.showSnackBar(result.message)
                }
            }
        }
    }

    private fun handleLanguageChange(lang: LanguageOption) {
        currentLanguage = lang
        setRadioLanguage(lang)
    }

    private fun setRadioLanguage(lang: LanguageOption) {
        when (lang.name.lowercase()) {
            "indonesia" -> {
                binding.idRadio.isChecked = true
            }

            "english" -> {
                binding.enRadio.isChecked = true
            }
        }
    }

    private fun handleThemeChange(theme: ThemeOption) {
        currentTheme = theme
        setSwitchTheme(theme)
    }


    private fun setSwitchTheme(theme: ThemeOption) {
        binding.switch2.isChecked = theme == ThemeOption.DARK
    }

    private fun applyTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            profileViewModel.setThemeSetting(ThemeOption.DARK)
        } else {
            profileViewModel.setThemeSetting(ThemeOption.LIGHT)
        }
    }

    private fun applyLanguage(languageCode: String) {
        if (languageCode == "ENGLISH") {
            profileViewModel.setLanguageSetting(LanguageOption.ENGLISH)
        } else {
            profileViewModel.setLanguageSetting(LanguageOption.INDONESIA)
        }
    }

    private fun getSelectedLanguage(checkedId: Int): LanguageOption {
        return when (checkedId) {
            binding.idRadio.id -> LanguageOption.INDONESIA
            binding.enRadio.id -> LanguageOption.ENGLISH
            else -> throw IllegalArgumentException("Unexpected radio button selection")
        }
    }

    private fun showProgressBar(status: Boolean) {
        binding.progressBar5.visibility = if (status) View.VISIBLE else View.GONE
    }
}