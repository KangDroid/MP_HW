package com.kangdroid.vocabapplication.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.QuestionType
import com.kangdroid.vocabapplication.databinding.FragmentMcqBinding
import com.kangdroid.vocabapplication.recycler.MCQRecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.LearnViewModel
import com.kangdroid.vocabapplication.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MCQFragment @Inject constructor(): Fragment() {
    // View Binding
    private var _fragmentMcq: FragmentMcqBinding? = null
    private val fragmentMcqBinding: FragmentMcqBinding get() = _fragmentMcq!!

    private val mcqRecyclerAdapter: MCQRecyclerAdapter = MCQRecyclerAdapter()

    private val questionViewModel: QuestionViewModel by activityViewModels()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        questionViewModel.createQuestion(QuestionType.QUESTION_MCQ)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mcq_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submitMcq -> {
                Log.d(this::class.java.simpleName, "Submit requested!")
                questionViewModel.markQuestions(mcqRecyclerAdapter.getSolvedQuestionData())
                learnViewModel.requestLearnPage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMcq = null
        questionViewModel.removeQuestionResult()
    }

    private fun initRecyclerView() {
        fragmentMcqBinding.doMcq.adapter = mcqRecyclerAdapter
        fragmentMcqBinding.doMcq.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initObserver() {
        questionViewModel.totalQuestionList.observe(viewLifecycleOwner) {
            if (it != null) {
                mcqRecyclerAdapter.setQuestionData(it)
            }
        }

        questionViewModel.questionResult.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), getString(R.string.mcq_result, it.first, it.second), Toast.LENGTH_LONG)
                    .show()
                learnViewModel.requestLearnPage()
            }
        }
    }
}