package com.example.dicodingstory.presentation

import android.view.LayoutInflater
import com.example.dicodingstory.base.BaseActivity
import com.example.dicodingstory.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityAuthenticationBinding =
        ActivityAuthenticationBinding::inflate
}