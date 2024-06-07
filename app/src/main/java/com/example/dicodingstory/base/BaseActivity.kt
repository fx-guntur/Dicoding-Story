package com.example.dicodingstory.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.example.dicodingstory.data.model.LanguageOption
import com.example.dicodingstory.data.model.ThemeOption
import com.example.dicodingstory.presentation.profile.ProfileViewModel
import com.example.dicodingstory.utils.helper.observe
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity<viewBinding : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: viewBinding
        private set

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater).apply {
            setContentView(root)
        }

        setupEdgeToEdge()
        onViewBindingCreated(savedInstanceState)
        observe(profileViewModel.getCurrentTheme(), ::setTheme)
        observe(profileViewModel.getCurrentLanguage(), ::setLanguage)
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
        binding.apply {
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.updatePadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )

                WindowInsetsCompat.CONSUMED
            }
        }
    }

    protected open fun onViewBindingCreated(savedInstanceState: Bundle?) {}

    private fun setTheme(themeOption: ThemeOption) {
        when (themeOption) {
            ThemeOption.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeOption.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setLanguage(langOption: LanguageOption) {
        var valueLanguage = if (langOption == LanguageOption.SYSTEM) {
            LanguageOption.ENGLISH.value
        } else {
            langOption.value
        }

        val localeList = LocaleListCompat.forLanguageTags(valueLanguage)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    abstract val bindingInflater: (LayoutInflater) -> viewBinding
}