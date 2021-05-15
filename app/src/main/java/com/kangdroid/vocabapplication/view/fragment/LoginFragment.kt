package com.kangdroid.vocabapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.response.ResponseCode
import com.kangdroid.vocabapplication.databinding.FragmentLoginBinding
import com.kangdroid.vocabapplication.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor(): Fragment() {
    // View Binding
    private var _fragmentLoginBinding: FragmentLoginBinding? = null
    private val fragmentLoginBinding: FragmentLoginBinding get() = _fragmentLoginBinding!!

    // Login View Model
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Text Layout
        initTextLayout()

        // Init buttons
        initButtons()

        // Init Observers
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Make sure de-reference value since Memory leak might occurs.
        _fragmentLoginBinding = null
    }

    private fun initTextLayout() {
        fragmentLoginBinding.userNameInputLayout.editText?.addTextChangedListener {
            it?.let {
                checkUserName(it.toString())
            }
        }
    }

    private fun initButtons() {
        fragmentLoginBinding.joinButton.setOnClickListener {
            loginViewModel.requestRegisterPage()
        }

        fragmentLoginBinding.loginButton.setOnClickListener {
            val userName: String = fragmentLoginBinding.userNameInputLayout.editText?.text.toString()
            val userPassword: String = fragmentLoginBinding.passwordInputLayout.editText?.text.toString()
            loginViewModel.loginUser(userName, userPassword)
        }
    }

    private fun initObserver() {
        loginViewModel.loginSucceed.observe(viewLifecycleOwner) {
            when(it) {
                ResponseCode.LOGIN_PASSWORD_INCORRECT, ResponseCode.LOGIN_USERNAME_NOT_FOUND -> {
                    fragmentLoginBinding.userNameInputLayout.apply {
                        editText?.text = null
                        error = getString(R.string.id_password_mismatch)
                    }
                    fragmentLoginBinding.passwordInputLayout.apply {
                        editText?.text = null
                        error = getString(R.string.id_password_mismatch)
                    }
                }
            }
        }
    }

    private fun checkUserName(userName: String) {
        if (userName.isEmpty()) {
            fragmentLoginBinding.userNameInputLayout.error = getString(R.string.required_field)
        }
    }
}