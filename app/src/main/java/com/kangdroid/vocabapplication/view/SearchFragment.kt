package com.kangdroid.vocabapplication.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment @Inject constructor() : Fragment() {
    // Log Tag
    private val logTag: String = this::class.java.simpleName

    // View Binding
    private var _fragmentSearchBinding: FragmentSearchBinding? = null
    private val fragmentSearchBinding: FragmentSearchBinding get() = _fragmentSearchBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return fragmentSearchBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_view, menu)
        val menuItem: MenuItem = menu.findItem(R.id.searchView)
        val searchView: SearchView = (menuItem.actionView as SearchView).apply {
            setOnQueryTextListener(
                object:SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Log.d(logTag, "Submitted: $query")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentSearchBinding = null
    }
}