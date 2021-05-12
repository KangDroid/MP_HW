package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.databinding.FragmentHomeBinding
import com.kangdroid.vocabapplication.recycler.WordRecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor() : Fragment() {
    // View Binding
    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding: FragmentHomeBinding get() = _fragmentHomeBinding!!

    // Home Recycler
    private val wordRecyclerAdapter: WordRecyclerAdapter = WordRecyclerAdapter()

    // Home View Model
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Recycler View
        initRecyclerView()

        // Init Observer
        initObserver()

        // Init Refresher
        initRefresher()

        homeViewModel.setRandomWordList()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // De-reference
        _fragmentHomeBinding = null
    }

    private fun initRecyclerView() {
        fragmentHomeBinding.homeRecyclerView.layoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
        fragmentHomeBinding.homeRecyclerView.adapter = wordRecyclerAdapter
    }

    private fun initObserver() {
        homeViewModel.randomWordList.observe(viewLifecycleOwner) {
            Log.d(this::class.java.simpleName, "Observed Random Word List!")
            Log.d(this::class.java.simpleName, "List size: ${it.size}")
            wordRecyclerAdapter.setRandomWordData(it)
        }
    }

    private fun initRefresher() {
        fragmentHomeBinding.swipeRefresh.setOnRefreshListener {
            Log.d(this::class.java.simpleName, "Refresh Requested")
            homeViewModel.setRandomWordList()
            fragmentHomeBinding.swipeRefresh.isRefreshing = false
        }
    }
}