package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.QuestionType
import com.kangdroid.vocabapplication.databinding.FragmentOeBinding
import com.kangdroid.vocabapplication.recycler.OERecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.LearnViewModel
import com.kangdroid.vocabapplication.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OEFragment @Inject constructor(): Fragment() {
    // View Binding
    private var _fragmentOEBinding: FragmentOeBinding? = null
    private val fragmentOeBinding: FragmentOeBinding get() = _fragmentOEBinding!!

    private val oeRecyclerAdapter: OERecyclerAdapter = OERecyclerAdapter()

    private val learnViewModel: LearnViewModel by activityViewModels()
    private val questionViewModel: QuestionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentOEBinding = FragmentOeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return fragmentOeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init RecyclerView
        initRecyclerView()

        // Init Observer
        initObserver()

        // Init Question
        questionViewModel.createQuestion(QuestionType.QUESTION_OE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mcq_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submitMcq -> {
                Log.d(this::class.java.simpleName, "Submit requested!")
                questionViewModel.markQuestions(oeRecyclerAdapter.getSolvedQuestionData())
                learnViewModel.requestLearnPage()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentOEBinding = null
        questionViewModel.removeQuestionResult()
    }

    private fun initRecyclerView() {
        fragmentOeBinding.doOe.adapter = oeRecyclerAdapter
        fragmentOeBinding.doOe.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initObserver() {
        questionViewModel.totalQuestionList.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d(this::class.java.simpleName, "Observed OE Data: ${it.size}")
                oeRecyclerAdapter.setQuestionData(it)
            }
        }

        questionViewModel.questionResult.observe(viewLifecycleOwner) {
            Log.d(this::class.java.simpleName, "Got Result")
            if (it != null) {
                Toast.makeText(requireContext(), getString(R.string.mcq_result, it.first, it.second), Toast.LENGTH_LONG)
                    .show()
                learnViewModel.requestLearnPage()
            }
        }
    }
}