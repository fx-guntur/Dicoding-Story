package com.example.dicodingstory.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dicodingstory.R
import com.example.dicodingstory.base.BaseFragment
import com.example.dicodingstory.databinding.FragmentSigninBinding
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.presentation.MainActivity
import com.example.dicodingstory.utils.helper.observe
import com.example.dicodingstory.utils.helper.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SigninFragment : BaseFragment<FragmentSigninBinding>() {
    private val signInViewModel: SigninViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSigninBinding {
        return FragmentSigninBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        binding.apply {
            loginBtn.setOnClickListener {
                userSignInRequest()
            }
            signupTV.setOnClickListener {
                moveToSignUp()
            }
        }

    }

    private fun userSignInRequest() {
        val email = binding.emailEditText.text.toString()
        val password = binding.customPasswordEditText.text.toString()

        when {
            email.isEmpty() -> binding.emailEditText.error = getString(R.string.validation_is_empty)

            password.isEmpty() -> {
                binding.customPasswordEditText.error = getString(R.string.validation_is_empty)
            }

            password.length < 8 -> {
                binding.customPasswordEditText.error = getString(R.string.validation_password)
            }

            else -> {
                userSignInProcess(email, password)
            }
        }
    }

    private fun userSignInProcess(email: String, pass: String) {
        observe(signInViewModel.signIn(email, pass), ::signInStatus)
    }

    private fun signInStatus(resultStatus: ResultStatus<String>) {
        when (resultStatus) {
            is ResultStatus.Loading -> {
                showProgressBar(true)
            }

            is ResultStatus.Success -> {
                showProgressBar(false)
                binding.root.showSnackBar(
                    getString(R.string.sign_in_success)
                )
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            is ResultStatus.Error -> {
                showProgressBar(false)
                binding.root.showSnackBar(
                    getString(R.string.sign_in_failed)
                )
            }
        }
    }

    private fun showProgressBar(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun moveToSignUp() {
        findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
    }

    override fun initProcess() {}
}