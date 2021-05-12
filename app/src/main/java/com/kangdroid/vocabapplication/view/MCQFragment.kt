package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.databinding.FragmentMcqBinding
import com.kangdroid.vocabapplication.recycler.MCQRecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.MCQViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MCQFragment @Inject constructor(): Fragment() {
    // View Binding
    private var _fragmentMcq: FragmentMcqBinding? = null
    private val fragmentMcqBinding: FragmentMcqBinding get() = _fragmentMcq!!

    private val mcqRecyclerAdapter: MCQRecyclerAdapter = MCQRecyclerAdapter()

    private val mcqViewModel: MCQViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentMcq = FragmentMcqBinding.inflate(inflater, container, false)
        return fragmentMcqBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        mcqViewModel.prepareQuestionData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMcq = null
    }

    private fun initRecyclerView() {
        fragmentMcqBinding.doMcq.adapter = mcqRecyclerAdapter
        fragmentMcqBinding.doMcq.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initObserver() {
        mcqViewModel.questionData.observe(viewLifecycleOwner) {
            mcqRecyclerAdapter.setQuestionData(it)
        }
    }
}