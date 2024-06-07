package com.example.dicodingstory.presentation.greeting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import com.example.dicodingstory.base.BaseActivity
import com.example.dicodingstory.databinding.ActivityGreetingBinding
import com.example.dicodingstory.presentation.AuthenticationActivity

class GreetingActivity : BaseActivity<ActivityGreetingBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityGreetingBinding =
        ActivityGreetingBinding::inflate

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        binding.apply {
            startButton.setOnClickListener {
                startActivity(Intent(this@GreetingActivity, AuthenticationActivity::class.java))
                finish()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


}