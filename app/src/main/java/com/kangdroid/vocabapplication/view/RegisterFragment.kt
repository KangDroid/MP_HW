package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment @Inject constructor(): Fragment() {
    private val passwordRegex: Regex = """^(?=.*[@#$%!\-_?&])(?=\S+$).*""".toRegex()
    private var _registerFragmentBinding: FragmentRegisterBinding? = null
    private val fragmentRegisterBinding: FragmentRegisterBinding get() =_registerFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _registerFragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return fragmentRegisterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Text Layout
        initTextLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Make sure de-reference value since Memory leak might occurs.
        _registerFragmentBinding = null
    }

    private fun initTextLayout() {
        // User name Check
        fragmentRegisterBinding.registerInputName.editText?.addTextChangedListener {
            it?.let {
                checkUserName(it.trim().toString())
            }
        }

        // Password validation
        fragmentRegisterBinding.registerPasswordLayout.editText?.addTextChangedListener {
            it?.let {
                checkPassword(it.trim().toString())
            }
        }

        // Password Check
        fragmentRegisterBinding.registerPasswordCheck.editText?.addTextChangedListener {
            it?.let {
                checkPasswordValidation(it.trim().toString())
            }
        }
    }

    private fun checkUserName(userName: String) {
        if (userName.isEmpty()) {
            fragmentRegisterBinding.registerInputName.error = getString(R.string.required_field)
        }
    }

    private fun checkPassword(userPassword: String) {
        when {
            (userPassword.length < 8) -> fragmentRegisterBinding.registerPasswordLayout.error = getString(R.string.password_length_short)
            !passwordRegex.matches(userPassword) -> fragmentRegisterBinding.registerPasswordLayout.error = getString(R.string.password_no_special)
            else -> fragmentRegisterBinding.registerPasswordLayout.error = null
        }
    }

    private fun checkPasswordValidation(userPassword: String) {
        val inputText: String = userPassword.toString()
        val targetText: String = fragmentRegisterBinding.registerPasswordLayout.editText?.text?.trim().toString()

        when {
            inputText != targetText -> {
                fragmentRegisterBinding.registerPasswordCheck.error = getString(R.string.password_does_not_match)
            }
            else -> fragmentRegisterBinding.registerPasswordCheck.error = null
        }
    }
}