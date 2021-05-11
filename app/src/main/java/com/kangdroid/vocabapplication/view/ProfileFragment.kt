package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment @Inject constructor() : Fragment() {
    private var _fragmentProfileBinding: FragmentProfileBinding? = null
    private val fragmentRegisterBinding: FragmentProfileBinding get() = _fragmentProfileBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return fragmentRegisterBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentProfileBinding = null
    }
}