package com.kangdroid.vocabapplication.view.fragment

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.QuestionType
import com.kangdroid.vocabapplication.databinding.FragmentListenBinding
import com.kangdroid.vocabapplication.recycler.ListenOERecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.LearnViewModel
import com.kangdroid.vocabapplication.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListenOEFragment @Inject constructor() : Fragment() {
    private var _fragmentListen: FragmentListenBinding? = null
    private val fragmentListenBinding: FragmentListenBinding get() = _fragmentListen!!

    private val questionViewModel: QuestionViewModel by activityViewModels()
    private val learnViewModel: LearnViewModel by activityViewModels()

    @Inject
    lateinit var textToSpeech: TextToSpeech

    private val listenChoiceRecyclerAdapter: ListenOERecyclerAdapter by lazy {
        ListenOERecyclerAdapter {
            textToSpeech.speak(it.targetWord.word, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentListen = FragmentListenBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return fragmentListenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObserver()
        questionViewModel.createQuestion(QuestionType.QUESTION_LISTEN_OE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mcq_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submitMcq -> {
                Log.d(this::class.java.simpleName, "Submit requested!")
                questionViewModel.markQuestions(listenChoiceRecyclerAdapter.listenQuestionData)
                learnViewModel.requestLearnPage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentListen = null
        questionViewModel.removeQuestionResult()
    }

    private fun initRecyclerView() {
        fragmentListenBinding.doListen.adapter = listenChoiceRecyclerAdapter
        fragmentListenBinding.doListen.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initObserver() {
        questionViewModel.totalQuestionList.observe(viewLifecycleOwner) {
            if (it != null) {
                listenChoiceRecyclerAdapter.listenQuestionData = it
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