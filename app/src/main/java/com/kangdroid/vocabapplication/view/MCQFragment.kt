package com.kangdroid.vocabapplication.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.FragmentMcqBinding
import com.kangdroid.vocabapplication.recycler.MCQRecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.LearnViewModel
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
    private val learnViewModel: LearnViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentMcq = FragmentMcqBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return fragmentMcqBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback {
            learnViewModel.requestLearnPage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        mcqViewModel.prepareQuestionData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mcq_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submitMcq -> {
                Log.d(this::class.java.simpleName, "Submit requested!")
                mcqViewModel.markQuestions(mcqRecyclerAdapter.getSolvedQuestionData())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMcq = null
        mcqViewModel.removeQuestionResult()
    }

    private fun initRecyclerView() {
        fragmentMcqBinding.doMcq.adapter = mcqRecyclerAdapter
        fragmentMcqBinding.doMcq.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initObserver() {
        mcqViewModel.questionData.observe(viewLifecycleOwner) {
            mcqRecyclerAdapter.setQuestionData(it)
        }

        mcqViewModel.questionResult.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), getString(R.string.mcq_result, it.first, it.second), Toast.LENGTH_LONG)
                    .show()
                learnViewModel.requestLearnPage()
            }
        }
    }
}