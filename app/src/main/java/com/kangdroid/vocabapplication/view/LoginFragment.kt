package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor(): Fragment() {
    // View Binding
    private var _fragmentLoginBinding: FragmentLoginBinding? = null
    private val fragmentLoginBinding: FragmentLoginBinding get() = _fragmentLoginBinding!!

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

    private fun checkUserName(userName: String) {
        if (userName.isEmpty()) {
            fragmentLoginBinding.userNameInputLayout.error = getString(R.string.required_field)
        }
    }
}