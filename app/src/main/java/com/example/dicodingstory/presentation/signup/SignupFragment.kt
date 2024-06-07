package com.example.dicodingstory.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dicodingstory.R
import com.example.dicodingstory.base.BaseFragment
import com.example.dicodingstory.databinding.FragmentSignupBinding
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.utils.helper.observe
import com.example.dicodingstory.utils.helper.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFragment : BaseFragment<FragmentSignupBinding>() {
    private val signupViewModel: SignupViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        binding.apply {
            signinTV.setOnClickListener {
                moveToSignUp()
            }
            registerBtn.setOnClickListener {
                userSignUpRequest()
            }
        }
    }

    private fun userSignUpRequest() {
        val email = binding.emailEditText.text.toString()
        val password = binding.customPasswordEditText.text.toString()
        val fullName = binding.fullNameEditText.text.toString()

        when {
            email.isEmpty() -> binding.emailEditText.error = getString(R.string.validation_is_empty)

            fullName.isEmpty() -> binding.emailEditText.error =
                getString(R.string.validation_is_empty)

            password.isEmpty() -> {
                binding.customPasswordEditText.error = getString(R.string.validation_is_empty)
            }

            password.length < 8 -> {
                binding.customPasswordEditText.error = getString(R.string.validation_password)
            }

            else -> {
                userSignUpProcess(fullName, email, password)
            }
        }
    }

    private fun userSignUpProcess(name: String, email: String, pass: String) {
        observe(signupViewModel.signUp(name, email, pass), ::userSignUpStatus)
    }

    private fun userSignUpStatus(resultStatus: ResultStatus<String>) {
        when (resultStatus) {
            is ResultStatus.Loading -> {
                showProgressBar(true)
            }

            is ResultStatus.Success -> {
                showProgressBar(false)
                binding.root.showSnackBar(
                    getString(R.string.sign_up_success)
                )
                moveToSignUp()
            }

            is ResultStatus.Error -> {
                showProgressBar(false)
                binding.root.showSnackBar(
                    resultStatus.message
                )
            }
        }
    }

    private fun showProgressBar(status: Boolean) {
        binding.progressBar2.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun moveToSignUp() {
        findNavController().popBackStack()
    }

    override fun initProcess() {}

    companion object
}