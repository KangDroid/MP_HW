package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.data.entity.question.QuestionIdentifier
import com.kangdroid.vocabapplication.databinding.FragmentLearnBinding
import com.kangdroid.vocabapplication.recycler.LearnRecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.LearnViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LearnFragment @Inject constructor(): Fragment() {
    // View Binding
    private var _fragmentLearnBinding: FragmentLearnBinding? = null
    private val fragmentLearnBinding: FragmentLearnBinding get() = _fragmentLearnBinding!!

    // Recycler Adapter
    private val learnRecyclerAdapter: LearnRecyclerAdapter = LearnRecyclerAdapter() {
        when (it) {
            QuestionIdentifier.QUESTION_MCQ -> learnViewModel.requestMCQ()
            else -> {}
        }
    }

    private val learnViewModel: LearnViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentLearnBinding = FragmentLearnBinding.inflate(inflater, container, false)
        return fragmentLearnBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Recycler View
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentLearnBinding = null
    }

    private fun initRecycler() {
        fragmentLearnBinding.learnRecyclerView.adapter = learnRecyclerAdapter
        fragmentLearnBinding.learnRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}