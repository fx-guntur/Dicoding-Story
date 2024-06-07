package com.example.dicodingstory.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dicodingstory.R
import com.example.dicodingstory.base.BaseActivity
import com.example.dicodingstory.databinding.ActivityMainBinding
import com.example.dicodingstory.presentation.greeting.GreetingActivity
import com.example.dicodingstory.presentation.signin.SigninViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val signinViewModel: SigninViewModel by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigation.apply {
            setupWithNavController(
                navHostFragment.navController
            )
        }
    }

    override fun onStart() {
        super.onStart()

        signinViewModel.getSession().observe(this) {
            if (it.id === "" || it === null) {
                moveToGreeting()
            }
        }
    }

    private fun moveToGreeting() {
        startActivity(Intent(this, GreetingActivity::class.java))
        finish()
    }
}