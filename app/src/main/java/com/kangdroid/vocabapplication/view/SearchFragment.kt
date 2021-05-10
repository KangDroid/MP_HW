package com.kangdroid.vocabapplication.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.FragmentSearchBinding
import com.kangdroid.vocabapplication.recycler.WordRecyclerAdapter
import com.kangdroid.vocabapplication.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment @Inject constructor() : Fragment() {
    // Log Tag
    private val logTag: String = this::class.java.simpleName

    // View Binding
    private var _fragmentSearchBinding: FragmentSearchBinding? = null
    private val fragmentSearchBinding: FragmentSearchBinding get() = _fragmentSearchBinding!!

    // Search View Model
    private val searchViewModel: SearchViewModel by viewModels()

    // Word Search Result Adapter
    private val wordSearchResultAdapter: WordRecyclerAdapter = WordRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return fragmentSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initiate RecyclerView
        initRecyclerView()

        // Init Observer
        initObserver()

        // Initiate word list to view
        searchViewModel.initiateAllWord()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_view, menu)
        val menuItem: MenuItem = menu.findItem(R.id.searchView)
        val searchView: SearchView = (menuItem.actionView as SearchView).apply {
            setOnQueryTextListener(searchViewModel.searchViewTextListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentSearchBinding = null
    }

    private fun initRecyclerView() {
        // Recycler
        fragmentSearchBinding.searchResultRecycler.layoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
        fragmentSearchBinding.searchResultRecycler.adapter = wordSearchResultAdapter
    }

    private fun initObserver() {
        searchViewModel.searchResult.observe(viewLifecycleOwner) { it ->
            Log.d(logTag, "Observed word!")
            Log.d(logTag, "Word Size: ${it.size}")
            wordSearchResultAdapter.setRandomWordData(it.toMutableList())
        }
    }
}